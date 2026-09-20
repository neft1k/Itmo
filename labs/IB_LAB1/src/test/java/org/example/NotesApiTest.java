package org.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.example.security.SecurityConfig;
import org.example.user.AppUser;
import org.example.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.datasource.url=jdbc:h2:mem:notes-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.password=test-database-only",
        "app.jwt.secret=dGVzdC1vbmx5LWtleS1uZXZlci11c2UtaW4tcHJvZHVjdGlvbi0xMjM=",
        "app.bootstrap.username=alice",
        "app.bootstrap.password=alice-test-password"
})
class NotesApiTest {
    @LocalServerPort private int port;
    @Autowired private ObjectMapper json;
    @Autowired private JdbcTemplate jdbc;
    @Autowired private UserRepository users;
    @Autowired private PasswordEncoder passwords;
    @Autowired private JwtEncoder encoder;
    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void clearNotes() {
        jdbc.update("DELETE FROM notes");
        if (users.findByUsername("bob").isEmpty()) {
            users.insert(new AppUser(UUID.randomUUID().toString(), "bob", passwords.encode("bob-test-password")));
        }
    }

    @Test
    void loginReturnsTokenAndNeverCreatesSession() throws Exception {
        var response = request("POST", "/auth/login", null,
                json.writeValueAsString(Map.of("username", "alice", "password", "alice-test-password")));
        assertThat(response.statusCode()).isEqualTo(200);
        var body = json.readTree(response.body());
        assertThat(body.get("tokenType").asString()).isEqualTo("Bearer");
        assertThat(body.get("expiresIn").asInt()).isEqualTo(900);
        assertThat(body.get("accessToken").asString().split("\\.")).hasSize(3);
        assertThat(response.headers().firstValue("set-cookie")).isEmpty();
        assertThat(response.headers().firstValue("cache-control").orElseThrow()).contains("no-store");
    }

    @Test
    void passwordsAreStoredAsBcryptHashes() {
        var hash = users.findByUsername("alice").orElseThrow().passwordHash();
        assertThat(hash).startsWith("$2").doesNotContain("alice-test-password");
        assertThat(passwords.matches("alice-test-password", hash)).isTrue();
    }

    @Test
    void bothDataEndpointsRequireToken() throws Exception {
        assertThat(request("GET", "/api/data", null, null).statusCode()).isEqualTo(401);
        assertThat(request("POST", "/api/notes", null, "{\"title\":\"a\",\"content\":\"b\"}")
                .statusCode()).isEqualTo(401);
    }

    @Test
    void wrongPasswordAndSqlInjectionCannotLogin() throws Exception {
        for (String username : new String[]{"alice", "missing", "alice' OR '1'='1' --"}) {
            var response = request("POST", "/auth/login", null,
                    json.writeValueAsString(Map.of("username", username, "password", "wrong-password")));
            assertThat(response.statusCode()).isEqualTo(401);
            assertThat(response.body()).isEqualTo("{\"error\":\"Invalid credentials\"}");
        }
    }

    @Test
    void createAndReadOwnNotes() throws Exception {
        String token = login("alice", "alice-test-password");
        var created = request("POST", "/api/notes", token,
                json.writeValueAsString(Map.of("title", "Лабораторная", "content", "Сделать заметки")));
        assertThat(created.statusCode()).isEqualTo(201);
        var note = json.readTree(created.body());
        assertThat(note.get("id").asString()).isNotBlank();
        assertThat(note.get("createdAt").asString()).isNotBlank();
        var response = request("GET", "/api/data", token, null);
        assertThat(response.statusCode()).isEqualTo(200);
        var list = json.readTree(response.body());
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).get("id")).isEqualTo(note.get("id"));
        assertThat(list.get(0).get("content").asString()).isEqualTo("Сделать заметки");
        assertThat(response.body()).doesNotContain("password", "ownerId");
    }

    @Test
    void usersCannotReadEachOthersNotes() throws Exception {
        String alice = login("alice", "alice-test-password");
        String bob = login("bob", "bob-test-password");
        assertThat(request("POST", "/api/notes", alice,
                "{\"title\":\"Private\",\"content\":\"Only Alice\"}").statusCode()).isEqualTo(201);
        assertThat(request("GET", "/api/data", bob, null).body()).isEqualTo("[]");
        assertThat(request("GET", "/api/data", alice, null).body()).contains("Only Alice");
    }

    @Test
    void clientCannotAssignOwner() throws Exception {
        String token = login("alice", "alice-test-password");
        var response = request("POST", "/api/notes", token,
                "{\"title\":\"a\",\"content\":\"b\",\"ownerId\":\"bob\"}");
        assertThat(response.statusCode()).isEqualTo(400);
        assertThat(request("GET", "/api/data", token, null).body()).isEqualTo("[]");
    }

    @Test
    void xssIsEscapedInBothResponsesButStoredAsOriginalText() throws Exception {
        String token = login("alice", "alice-test-password");
        String payload = "<script>alert(1)</script> & \"test\"";
        var created = request("POST", "/api/notes", token,
                json.writeValueAsString(Map.of("title", payload, "content", payload)));
        assertThat(created.statusCode()).isEqualTo(201);
        JsonNode note = json.readTree(created.body());
        String escaped = "&lt;script&gt;alert(1)&lt;/script&gt; &amp; &quot;test&quot;";
        assertThat(note.get("title").asString()).isEqualTo(escaped);
        assertThat(note.get("content").asString()).isEqualTo(escaped);
        var read = request("GET", "/api/data", token, null);
        assertThat(json.readTree(read.body()).get(0).get("content").asString()).isEqualTo(escaped);
        assertThat(read.headers().firstValue("content-type").orElseThrow()).startsWith("application/json");
        assertThat(read.headers().firstValue("x-content-type-options").orElseThrow()).isEqualTo("nosniff");
        assertThat(jdbc.queryForObject("SELECT content FROM notes WHERE id = ?", String.class,
                note.get("id").asString())).isEqualTo(payload);
    }

    @Test
    void sqlPayloadInNoteIsStoredAsData() throws Exception {
        String token = login("alice", "alice-test-password");
        String payload = "'); DROP TABLE app_users; --";
        var response = request("POST", "/api/notes", token,
                json.writeValueAsString(Map.of("title", "SQL", "content", payload)));
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(jdbc.queryForObject("SELECT content FROM notes", String.class)).isEqualTo(payload);
        assertThat(login("alice", "alice-test-password")).isNotBlank();
    }

    @Test
    void invalidAndExpiredTokensAreRejected() throws Exception {
        String valid = login("alice", "alice-test-password");
        String[] parts = valid.split("\\.");
        String changedSignature = (parts[2].startsWith("A") ? "B" : "A") + parts[2].substring(1);
        String tampered = parts[0] + "." + parts[1] + "." + changedSignature;
        String expired = signedToken(SecurityConfig.ISSUER, "alice", Instant.now().minusSeconds(300));
        String wrongIssuer = signedToken("another-api", "alice", Instant.now().plusSeconds(900));
        String missingExpiry = signedToken(SecurityConfig.ISSUER, "alice", null);
        for (String token : new String[]{"invalid", tampered, expired, wrongIssuer, missingExpiry}) {
            assertThat(request("GET", "/api/data", token, null).statusCode()).isEqualTo(401);
        }
    }

    @Test
    void unknownUserTokenIsRejected() throws Exception {
        var token = signedToken(SecurityConfig.ISSUER, "deleted-user", Instant.now().plusSeconds(900));
        assertThat(request("GET", "/api/data", token, null).statusCode()).isEqualTo(401);
    }

    @Test
    void invalidInputIsRejectedWithoutEchoingPayload() throws Exception {
        String token = login("alice", "alice-test-password");
        for (String body : new String[]{"{", "{}", "{\"title\":\" \",\"content\":\"x\"}",
                json.writeValueAsString(Map.of("title", "a".repeat(201), "content", "x")),
                json.writeValueAsString(Map.of("title", "x", "content", "a".repeat(10001)))}) {
            var response = request("POST", "/api/notes", token, body);
            assertThat(response.statusCode()).isEqualTo(400);
            assertThat(response.body()).isEqualTo("{\"error\":\"Invalid request\"}");
        }
    }

    private String login(String username, String password) throws Exception {
        var response = request("POST", "/auth/login", null,
                json.writeValueAsString(Map.of("username", username, "password", password)));
        assertThat(response.statusCode()).isEqualTo(200);
        return json.readTree(response.body()).get("accessToken").asString();
    }

    private String signedToken(String issuer, String subject, Instant expiry) {
        var claims = JwtClaimsSet.builder().issuer(issuer).subject(subject);
        if (expiry != null) {
            claims.expiresAt(expiry);
        }
        return encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),
                claims.build())).getTokenValue();
    }

    private HttpResponse<String> request(String method, String path, String token, String body) throws Exception {
        var builder = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + path));
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
        if (body != null) {
            builder.header("Content-Type", "application/json");
        }
        builder.method(method, body == null ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(body));
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }
}

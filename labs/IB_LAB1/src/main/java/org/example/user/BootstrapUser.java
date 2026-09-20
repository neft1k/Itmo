package org.example.user;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BootstrapUser implements ApplicationRunner {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final String username;
    private final String password;

    public BootstrapUser(UserRepository users, PasswordEncoder encoder,
                         @Value("${app.bootstrap.username}") String username,
                         @Value("${app.bootstrap.password}") String password) {
        this.users = users;
        this.encoder = encoder;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!username.matches("[a-zA-Z0-9_-]{1,64}") || password.length() < 12
                || password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("APP_USERNAME: 1-64 letters, digits, '_' or '-'; "
                    + "APP_PASSWORD: at least 12 characters and at most 72 UTF-8 bytes");
        }
        if (users.findByUsername(username).isEmpty()) {
            users.insert(new AppUser(UUID.randomUUID().toString(), username, encoder.encode(password)));
        }
    }
}

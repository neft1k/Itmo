package org.example.note;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.example.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.HtmlUtils;

@RestController
public class NoteController {
    private final NoteRepository notes;
    private final UserRepository users;

    public NoteController(NoteRepository notes, UserRepository users) {
        this.notes = notes;
        this.users = users;
    }

    @GetMapping("/api/data")
    public List<NoteResponse> list(@AuthenticationPrincipal Jwt jwt) {
        return notes.findByOwnerId(ownerId(jwt)).stream().map(NoteResponse::from).toList();
    }

    @PostMapping("/api/notes")
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateNoteRequest request) {
        var note = new Note(UUID.randomUUID().toString(), ownerId(jwt),
                request.title(), request.content(), Instant.now());
        notes.insert(note);
        return NoteResponse.from(note);
    }

    private String ownerId(Jwt jwt) {
        return users.findByUsername(jwt.getSubject())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED)).id();
    }

    public record CreateNoteRequest(@NotBlank @Size(max = 200) String title,
                                    @NotBlank @Size(max = 10000) String content) { }

    public record NoteResponse(String id, String title, String content, Instant createdAt) {
        static NoteResponse from(Note note) {
            return new NoteResponse(
                    note.id(),
                    HtmlUtils.htmlEscape(note.title(), "UTF-8"),
                    HtmlUtils.htmlEscape(note.content(), "UTF-8"),
                    note.createdAt());
        }
    }
}

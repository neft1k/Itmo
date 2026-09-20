package org.example.note;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NoteRepository {
    private final JdbcTemplate jdbc;

    public NoteRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void insert(Note note) {
        jdbc.update("INSERT INTO notes (id, owner_id, title, content, created_at) VALUES (?, ?, ?, ?, ?)",
                note.id(), note.ownerId(), note.title(), note.content(),
                note.createdAt().atOffset(ZoneOffset.UTC));
    }

    public List<Note> findByOwnerId(String ownerId) {
        return jdbc.query("""
                SELECT id, owner_id, title, content, created_at FROM notes
                WHERE owner_id = ? ORDER BY created_at DESC, id
                """, (rs, row) -> new Note(rs.getString("id"), rs.getString("owner_id"),
                rs.getString("title"), rs.getString("content"),
                rs.getObject("created_at", OffsetDateTime.class).toInstant()), ownerId);
    }
}

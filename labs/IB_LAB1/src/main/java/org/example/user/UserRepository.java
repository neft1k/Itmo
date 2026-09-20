package org.example.user;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<AppUser> findByUsername(String username) {
        return jdbc.query("SELECT id, username, password_hash FROM app_users WHERE username = ?",
                (rs, row) -> new AppUser(rs.getString("id"), rs.getString("username"),
                        rs.getString("password_hash")), username).stream().findFirst();
    }

    public void insert(AppUser user) {
        jdbc.update("INSERT INTO app_users (id, username, password_hash) VALUES (?, ?, ?)",
                user.id(), user.username(), user.passwordHash());
    }
}

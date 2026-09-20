package org.example.user;

public record AppUser(String id, String username, String passwordHash) {
    @Override
    public String toString() {
        return "AppUser[id=" + id + "]";
    }
}

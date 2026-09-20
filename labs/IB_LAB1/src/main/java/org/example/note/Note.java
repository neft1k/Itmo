package org.example.note;

import java.time.Instant;

public record Note(String id, String ownerId, String title, String content, Instant createdAt) { }

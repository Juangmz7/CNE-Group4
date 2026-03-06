package com.cne_project.cne_project.model.dto.post;

public record PostViewDTO(
        String id,
        String authorId,
        String authorUsername,
        String preview,
        Long creation
) {
}

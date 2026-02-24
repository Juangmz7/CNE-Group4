package com.cne_project.cne_project.model.dto.post;

public record PostResponseDTO(
        String id,
        String authorId,
        String authorName,
        String content,
        Integer rating,
        Long creation
) {
}

package com.cne_project.cne_project.model.dto.post;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @param replyToId  If the post is a reply, it's included otherwise no
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostResponseDTO(
        String id,
        String replyToId,
        String authorId,
        String authorName,
        String content,
        Integer rating,
        Long creation
) {
}

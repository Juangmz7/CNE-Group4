package com.cne_project.cne_project.model.dto.post;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record PostRequestDTO(
        @NotBlank(message = "Content cannot be empty")
        String content
) {
}

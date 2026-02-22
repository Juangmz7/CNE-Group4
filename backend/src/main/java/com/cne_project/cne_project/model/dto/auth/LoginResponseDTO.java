package com.cne_project.cne_project.model.dto.auth;

public record LoginResponseDTO(
        String username,
        String password,
        String accessToken,
        String refreshToken
) {
}

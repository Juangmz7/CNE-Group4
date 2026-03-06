package com.cne_project.cne_project.model.dto.auth;


public record UserProfileResponseDTO (
        String userId,
        String username,
        long userKarma
) {
}

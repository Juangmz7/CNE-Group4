package com.cne_project.cne_project.controller;

import com.cne_project.cne_project.model.dto.auth.UserProfileResponseDTO;
import com.cne_project.cne_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDTO> getUserProfile(
            @PathVariable UUID userId
    ) {
        var response = userService.getUserProfile(userId.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/karma/increase")
    public ResponseEntity<Void> increaseUserKarma(
            @RequestBody UUID userId
    ) {
        userService.increaseUserKarma(userId.toString());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/karma/decrease")
    public ResponseEntity<Void> decreaseUserKarma(
            @RequestBody UUID userId
    ) {
        userService.decreaseUserKarma(userId.toString());
        return ResponseEntity.noContent().build();
    }
}

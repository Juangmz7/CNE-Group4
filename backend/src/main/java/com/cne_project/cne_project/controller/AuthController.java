package com.cne_project.cne_project.controller;

import com.cne_project.cne_project.model.dto.auth.LoginRequestDTO;
import com.cne_project.cne_project.model.dto.auth.LoginResponseDTO;
import com.cne_project.cne_project.model.dto.auth.RegisterRequestDTO;
import com.cne_project.cne_project.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @RequestMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @RequestMapping("/register")
    public ResponseEntity<Void> register(@Valid RegisterRequestDTO request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

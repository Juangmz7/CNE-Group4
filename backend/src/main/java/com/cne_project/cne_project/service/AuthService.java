package com.cne_project.cne_project.service;

import com.cne_project.cne_project.config.exception.DuplicateEntityException;
import com.cne_project.cne_project.model.dto.auth.LoginRequestDTO;
import com.cne_project.cne_project.model.dto.auth.LoginResponseDTO;
import com.cne_project.cne_project.model.dto.auth.RegisterRequestDTO;
import com.cne_project.cne_project.model.entity.User;
import com.cne_project.cne_project.repository.UserRepository;
import com.cne_project.cne_project.utils.TokenPayload;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public LoginResponseDTO login (LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        log.info("User authenticated, generating access response");

       String accessToken = generateAccessToken(user);

        return new LoginResponseDTO(
                user.getUsername(), user.getPassword(), accessToken
        );
    }

    private String generateAccessToken(User user) {
        return jwtService.generateToken(
                TokenPayload.builder().
                        userId(user.getId())
                        .build()
        );
    }

    public void register(@Valid RegisterRequestDTO request) {
        boolean exists = userRepository
                .findByUsername(request.username())
                .isPresent();

        if (exists) {
            throw new DuplicateEntityException(
                    "User already exists"
            );
        }

        String hashedPassword = passwordEncoder
                .encode(request.password());

        // Create a new user managed by the model
        User user = User.builder()
                .email(request.email())
                .username(request.username())
                .password(hashedPassword)
                .build();

        userRepository.save(user);

        log.info("User registered successfully");
    }
}

package com.cne_project.cne_project.repository;

import com.cne_project.cne_project.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DbInitializer {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public DbInitializer(PasswordEncoder passwordEncoder, UserRepository userRepository, PostRepository postRepository) {
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void clearAll() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @PostConstruct
    public void init() {
        clearAll();
    }
}

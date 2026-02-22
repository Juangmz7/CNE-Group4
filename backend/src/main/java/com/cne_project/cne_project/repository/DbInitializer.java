package com.cne_project.cne_project.repository;

import com.cne_project.cne_project.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DbInitializer {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public DbInitializer(UserRepository userRepository, PostRepository postRepository) {
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

package com.cne_project.cne_project.service;

import com.cne_project.cne_project.model.dto.auth.UserProfileResponseDTO;
import com.cne_project.cne_project.model.entity.User;
import com.cne_project.cne_project.model.entity.UserKarma;
import com.cne_project.cne_project.repository.UserKarmaRepository;
import com.cne_project.cne_project.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserKarmaRepository userKarmaRepository;
    private final AuthService authService;
    private final UserRepository userRepository;

    public UserProfileResponseDTO getUserProfile (String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return new UserProfileResponseDTO(
                userId, user.getUsername(), user.getKarma()
        );
    }

}

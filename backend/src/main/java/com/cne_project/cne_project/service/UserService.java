package com.cne_project.cne_project.service;

import com.cne_project.cne_project.model.entity.UserKarma;
import com.cne_project.cne_project.repository.UserKarmaRepository;
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


    @Transactional
    public void increaseUserKarma(String userId) {
        validateCurrentUser(userId);

        UserKarma userKarma = userKarmaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userKarma.increaseKarma();

        userKarmaRepository.save(userKarma);

        log.info("UserKarma for user id {} increase success.", userId);
    }

    @Transactional
    public void decreaseUserKarma(String userId) {
        validateCurrentUser(userId);

        UserKarma userKarma = userKarmaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userKarma.decreaseKarma();

        userKarmaRepository.save(userKarma);

        log.info("UserKarma for user id {} decrease success.", userId);

    }

    private void validateCurrentUser(String userId) {
        String currentUserId = authService.currentUserId();

        if (!userId.equals(currentUserId)) {
            throw new IllegalStateException("You cannot perform this action");
        }
    }
}

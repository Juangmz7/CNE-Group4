package com.cne_project.cne_project.service;

import com.cne_project.cne_project.model.dto.post.PostRequestDTO;
import com.cne_project.cne_project.model.dto.post.PostResponseDTO;
import com.cne_project.cne_project.model.entity.Post;
import com.cne_project.cne_project.model.entity.User;
import com.cne_project.cne_project.repository.PostRepository;
import com.cne_project.cne_project.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final AuthService authService;
    private final UserRepository userRepository;

    public PostResponseDTO createPost(PostRequestDTO request) {
        String userId = authService.currentUserId();
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Current user not found"));

        var post = Post.builder()
                .content(request.content())
                .owner(currentUser)
                .reply(null)
                .build();

        var savedPost = postRepository.save(post);

        log.debug(post.toString());

        return new PostResponseDTO(
                savedPost.getId(),
                currentUser.getId(),
                currentUser.getUsername(),
                savedPost.getContent(),
                savedPost.getRating(),
                savedPost.getCreation()
        );
    }
}

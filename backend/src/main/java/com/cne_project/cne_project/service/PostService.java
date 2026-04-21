package com.cne_project.cne_project.service;

import com.cne_project.cne_project.model.dto.post.PostRequestDTO;
import com.cne_project.cne_project.model.dto.post.PostResponseDTO;
import com.cne_project.cne_project.model.dto.post.PostViewDTO;
import com.cne_project.cne_project.model.entity.Post;
import com.cne_project.cne_project.model.entity.User;
import com.cne_project.cne_project.repository.PostRepository;
import com.cne_project.cne_project.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final AuthService authService;
    private final UserRepository userRepository;

    public PostResponseDTO createPost(PostRequestDTO request) {
        var currentUser = getCurrentUser();

        var post = createPostInstance(
                request.content(), currentUser, null
        );

        var savedPost = postRepository.save(post);

        log.info("Post with id: {} saved successfully", savedPost.getId());

        return toPostResponseDTO(savedPost, currentUser);
    }

    public PostResponseDTO replyPost(
            PostRequestDTO request,
            String postId
    ) {
        var currentUser = getCurrentUser();

        Post replyTo = fetchPostById(postId);

        var post = createPostInstance(
                request.content(), currentUser, replyTo
        );

        var savedPost = postRepository.save(post);
        log.info("Post reply with id: {} saved successfully", postId);

        return toPostResponseDTO(savedPost, currentUser);
    }

    @Transactional(readOnly = true)
    public PostResponseDTO getPostById(String postId) {
        Post post = fetchPostById(postId);
        log.info("Returning post with id {}", postId);
        return toPostResponseDTO(post, getCurrentUser());
    }

    @Transactional
    public void increasePostRating(String postId) {
        Post post = fetchPostById(postId);

        checkIfSameUser(post.getOwner().getId());

        post.setRating(post.getRating() + 1);

        postRepository.save(post);

        log.info("Rating for post id {} increase succeeded.", postId);
    }


    @Transactional
    public void decreasePostRating(String postId) {
        Post post = fetchPostById(postId);

        checkIfSameUser(post.getOwner().getId());

        post.setRating(post.getRating() - 1);

        postRepository.save(post);

        log.info("Rating for post id {} decrease succeeded.", postId);

    }

    private void checkIfSameUser(String userId) {
        String currentUserId = authService.currentUserId();

        if (userId.equals(currentUserId)) {
            throw new IllegalStateException("You cannot perform this action");
        }
    }



    public PostResponseDTO updatePost(@Valid PostRequestDTO request, String postId) {
        var postToUpdate = fetchPostById(postId);

        validatePostAccess(postToUpdate);

        var currentuser =  getCurrentUser();
        if (!hasPostChanged(postToUpdate, request)) {
            return toPostResponseDTO(postToUpdate,  currentuser);
        }

        postToUpdate.setContent(request.content());
        var updatedPost = postRepository.save(postToUpdate);

        log.info("Post with id {} updated successfully", postId);

        return toPostResponseDTO(updatedPost, currentuser);
    }

    public void deletePost(String postId) {
        var postToDelete = fetchPostById(postId);

        validatePostAccess(postToDelete);

        postRepository.delete(postToDelete);
        log.info("Post with id {} deleted successfully", postId);
    }

    private boolean hasPostChanged(Post postToUpdate, PostRequestDTO request) {
        return !postToUpdate.getContent().equals(request.content());
    }

    private void validatePostAccess(Post post) {
        var currentUser = getCurrentUser();

        // Check whether who is updating the post is the owner
        if (!post.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not the owner of this post");
        }
    }

    private Post createPostInstance(String content, User currentUser, Post replyTo) {
        return Post.builder()
                .content(content)
                .owner(currentUser)
                .reply(replyTo)
                .creation(System.currentTimeMillis())
                .build();
    }

    private User getCurrentUser() {
        String userId = authService.currentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Current user not found"));
    }

    private PostResponseDTO toPostResponseDTO(Post post, User user) {
        return new PostResponseDTO(
                post.getId(),
                post.getReply() == null ? null : post.getReply().getId(),
                user.getId(),
                user.getUsername(),
                post.getContent(),
                post.getRating(),
                post.getCreation()
        );
    }

    private Post fetchPostById(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }


    public List<PostViewDTO> getAllPosts() {
        return postRepository.findPostsNotRepliedTo()
                .stream()
                .map(post -> {
                    String content = post.getContent();
                    String preview = (content != null && content.length() > 30)
                            ? content.substring(0, 30)
                            : content;

                    return new PostViewDTO(
                            post.getId(),
                            post.getOwner().getId(),
                            post.getOwner().getUsername(),
                            preview,
                            post.getCreation()
                    );
                })
                .toList();
    }

    public List<PostViewDTO> getAllPostsBy(String userId) {
        return postRepository.findPostsOwnedBy(userId)
                .stream()
                .map(post -> {
                    String content = post.getContent();
                    String preview = (content != null && content.length() > 30)
                            ? content.substring(0, 30)
                            : content;

                    return new PostViewDTO(
                            post.getId(),
                            post.getOwner().getId(),
                            post.getOwner().getUsername(),
                            preview,
                            post.getCreation()
                    );
                })
                .toList();
    }

    public List<PostResponseDTO> getAllPostsReplies(String postId) {
        return fetchAllReplies(postRepository.findDirectReplies(postId))
                .stream()
                .map(post -> new PostResponseDTO(
                        post.getId(),
                        post.getReply().getId(),
                        post.getOwner().getId(),
                        post.getOwner().getUsername(),
                        post.getContent(),
                        post.getRating(),
                        post.getCreation()
                ))
                .sorted(Comparator.comparing(PostResponseDTO::creation)) // Sort by creation date
                .toList().reversed();
    }

    private List<Post> fetchAllReplies(List<Post> replies) {
        List<Post> output = new java.util.ArrayList<>(replies);

        for (Post reply : replies) {
            List<Post> directReplies = postRepository.findDirectReplies(reply.getId());
            if (!directReplies.isEmpty()) {
                output.addAll(fetchAllReplies(directReplies));
            }
        }

        return output;
    }
}

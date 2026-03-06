package com.cne_project.cne_project.controller;

import com.cne_project.cne_project.model.dto.post.PostRequestDTO;
import com.cne_project.cne_project.model.dto.post.PostResponseDTO;
import com.cne_project.cne_project.model.dto.post.PostViewDTO;
import com.cne_project.cne_project.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostController {

    final private PostService postService;

    @PostMapping
    ResponseEntity<PostResponseDTO> createPost (@Valid @RequestBody PostRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.createPost(request));
    }

    @PostMapping("/{postId}/reply")
    ResponseEntity<PostResponseDTO> replyPost (
            @PathVariable UUID postId,
            @Valid @RequestBody PostRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.replyPost(request, postId.toString()));
    }

    @PutMapping("/{postId}")
    ResponseEntity<PostResponseDTO> updatePost (
            @PathVariable UUID postId,
            @Valid @RequestBody PostRequestDTO request
    ) {
        return ResponseEntity
                .ok(postService.updatePost(request, postId.toString()));
    }

    @DeleteMapping("/{postId}")
    ResponseEntity<Void> deletePost (
            @PathVariable UUID postId
    ) {
        postService.deletePost(postId.toString());
        return ResponseEntity
                .noContent().build();
    }

    @GetMapping("/{postId}")
    ResponseEntity<PostResponseDTO> getPostById (
            @PathVariable UUID postId
    ) {
        return ResponseEntity.ok(postService.getPostById(postId.toString()));
    }

    @PostMapping("/{postId}/rating/increase")
    public ResponseEntity<Void> increasePostRating(
            @PathVariable UUID postId
    ) {
        postService.increasePostRating(postId.toString());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/rating/decrease")
    public ResponseEntity<Void> decreasePostRating(
            @PathVariable UUID postId
    ) {
        postService.decreasePostRating(postId.toString());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PostViewDTO>> getAllPosts () {
        return ResponseEntity
                .ok(postService.getAllPosts());
    }

    // GetPostReplies
    @GetMapping("/{postId}/replies")
    public ResponseEntity<List<PostResponseDTO>> getAllPostsReplies (
        @PathVariable UUID postId
    ) {
        return ResponseEntity
                .ok(postService.getAllPostsReplies(postId.toString()));
    }


}

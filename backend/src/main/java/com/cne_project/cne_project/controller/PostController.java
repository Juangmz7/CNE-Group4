package com.cne_project.cne_project.controller;

import com.cne_project.cne_project.model.dto.post.PostRequestDTO;
import com.cne_project.cne_project.model.dto.post.PostResponseDTO;
import com.cne_project.cne_project.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // Reply post

    // Delete post

    // GetpostById

    // GetPostReplies


}

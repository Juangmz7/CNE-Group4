package com.cne_project.cne_project.repository;

import com.cne_project.cne_project.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
}

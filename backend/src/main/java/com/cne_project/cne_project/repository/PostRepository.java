package com.cne_project.cne_project.repository;

import com.cne_project.cne_project.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, String> {
}

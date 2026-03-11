package com.cne_project.cne_project.repository;

import com.cne_project.cne_project.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {

    @Query("""
            SELECT p FROM Post p JOIN FETCH p.owner
            WHERE p.owner.id != :currentUserId
            AND p.reply IS NULL
            """)
    List<Post> findPostsNotOwnedBy(@Param("currentUserId") String currentUserId);

    @Query("SELECT p FROM Post p JOIN FETCH p.owner WHERE p.reply.id = :postId ORDER BY p.creation ASC")
    List<Post> findDirectReplies(@Param("postId") String postId);
}

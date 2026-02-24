package com.cne_project.cne_project.model.entity;

import jakarta.persistence.*;
import lombok.Builder;

import java.time.Instant;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 32)
    private String id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to")
    private Post reply;

    @Column(nullable = false)
    private Integer rating = 0;

    @Column(nullable = false)
    private Long creation = Instant.now().getEpochSecond();

    protected Post() {}

    @Builder
    public Post(String content, User owner, Post reply) {
        setContent(content);
        setOwner(owner);
        setReply(reply);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public Post getReply() { return reply; }
    public void setReply(Post reply) { this.reply = reply; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public Long getCreation() { return creation; }
    public void setCreation(Long creation) { this.creation = creation; }
}
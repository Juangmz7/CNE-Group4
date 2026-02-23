package com.cne_project.cne_project.model.entity;

import jakarta.persistence.*;
import lombok.Builder;

import java.time.Instant;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(length = 32)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Long creation = Instant.now().getEpochSecond();

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserKarma karma;

    protected User() {}

    @Builder
    public User(String username, String email, String password) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Long getCreation() { return creation; }
    public void setCreation(Long creation) { this.creation = creation; }

    public Integer getKarma() { return karma.getKarma(); }
}
package com.cne_project.cne_project.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_karma")
@NoArgsConstructor
public class UserKarma {

    @Id
    @Column(length = 32)
    private String id;

    @Column(nullable = false)
    private Integer karma;

    public UserKarma(String id) {
        this.id = id;
        this.karma = 0;
    }

    public void increaseKarma() { this.karma++; }

    public void decreaseKarma() { this.karma--; }
}
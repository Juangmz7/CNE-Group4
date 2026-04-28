package com.cne_project.cne_project.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_karma")
public class UserKarma {
    @Id
    private String id;

    @Column(nullable = false)
    private Integer karma;

    public String getId() { return id; }
    public Integer getKarma() { return karma; }
}
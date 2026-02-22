package com.cne_project.cne_project.repository;

import com.cne_project.cne_project.model.UserKarma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserKarmaRepository extends JpaRepository<UserKarma, String> {
}

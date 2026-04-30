package com.cne_project.cne_project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ping")
@AllArgsConstructor
public class PingController {
    @GetMapping("/")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Ping!");
    }
}

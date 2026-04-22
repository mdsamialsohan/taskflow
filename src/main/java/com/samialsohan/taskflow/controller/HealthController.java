package com.samialsohan.taskflow.controller;

import com.samialsohan.taskflow.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {
    private final UserRepository userRepository;

    public HealthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/health")
    public Map<String, String> health(){
        return Map.of(
                "status", "UP",
                "service", "Taskflow API"
        );
    }
    @GetMapping("debug/user-count")
    public Map<String, Long>useCount(){
        return Map.of("count", userRepository.count());
    }
}
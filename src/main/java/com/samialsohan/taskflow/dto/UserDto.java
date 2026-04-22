package com.samialsohan.taskflow.dto;

import com.samialsohan.taskflow.repository.UserRepository;

import java.time.Instant;

public final class UserDto {
    private UserDto(){}
    public record CreateRequest(String name, String email){}
    public record Response(Long id, String name, String email, Instant createdAt){}
}
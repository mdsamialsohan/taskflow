package com.samialsohan.taskflow.dto;

import com.samialsohan.taskflow.repository.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public final class UserDto {
    private UserDto(){}
    public record CreateRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "name must be 100 character or less")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email){}

    public record Response(Long id, String name, String email, Instant createdAt){}

    public record UpdateRequest(
            @NotBlank(message = "Name is must required!")
            @Size(max = 100, message = "Name must be 100 characters or less")
            String name,

            @NotBlank(message = "Email is required")
            @Size(message = "Email must be valid")
            String email
    ){}
}
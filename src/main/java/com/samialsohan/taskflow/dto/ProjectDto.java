package com.samialsohan.taskflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public final class ProjectDto {
    private ProjectDto(){}
    public record CreateRequest(
            @NotBlank(message = "Project name is required")
            @Size(max = 200, message = "Project name must be 200 character or less")
            String name,

            @Size(max=1000, message = "Description must be 1000 character or less")
            String description
    ){}
    public record Response(
            Long id,
            String name,
            String description,
            Instant createdAt,
            Instant updatedAt
    ){}
    public record UpdateRequest(
            @NotBlank(message = "Project name is required")
            @Size(max = 200, message = "Project name must be 200 character or less")
            String name,

            @Size(max = 1000, message = "Description must be 1000 character or less")
            String description
    ){}
}

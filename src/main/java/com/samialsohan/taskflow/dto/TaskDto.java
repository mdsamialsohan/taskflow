package com.samialsohan.taskflow.dto;

import com.samialsohan.taskflow.entity.TaskPriority;
import com.samialsohan.taskflow.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.LocalDate;

public final class TaskDto {
    private TaskDto(){}
    public record CreateRequest(
            @NotBlank(message = "Task title is required")
            @Size(max=300, message = "Title must be 300 charcters or less")
            String title,

            @Size(max=2000, message = "Description must be 2000 characters or less")
            String description,

            @NotNull(message = "Project ID is required")
            Long projectId,
            Long assigneeId,
            TaskPriority priority,
            LocalDate dueDate
    ){}
    public record Response(
            Long id,
            String title,
            String description,
            TaskStatus status,
            TaskPriority priority,
            LocalDate dueDate,
            Instant createdAt,
            Instant updatedAt,
            Integer version,
            Long projectId,
            String projectName,
            Long assigneeId,
            String assigneeName
    ){}

}

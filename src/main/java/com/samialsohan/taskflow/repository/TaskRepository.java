package com.samialsohan.taskflow.repository;

import com.samialsohan.taskflow.entity.Task;
import com.samialsohan.taskflow.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByAssigneeId(Long assigneeId);
    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);
}

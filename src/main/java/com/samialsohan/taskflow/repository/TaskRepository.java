package com.samialsohan.taskflow.repository;

import com.samialsohan.taskflow.entity.Task;
import com.samialsohan.taskflow.entity.TaskPriority;
import com.samialsohan.taskflow.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByAssigneeId(Long assigneeId);
    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);
    @Query("""
                    SELECT t FROM Task t
                    WHERE(:projectId IS NULL OR t.project.id = :projectId)
                     AND (:status IS NULL OR t.status = :status)
                     AND (:priority IS NULL OR t.priority = :priority)
                     AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId)
                     AND (CAST(:keyword AS string) IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')))
            """)
    List<Task>searchTasks(
            @Param("projectId") Long projectId,
            @Param("status") TaskStatus status,
            @Param("priority")TaskPriority priority,
            @Param("assigneeId") Long assigneeId,
            @Param("keyword") String keyword
    );
}


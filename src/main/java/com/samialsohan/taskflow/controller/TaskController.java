package com.samialsohan.taskflow.controller;

import com.samialsohan.taskflow.dto.TaskDto;
import com.samialsohan.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDto.Response> getTaskByProject(@RequestParam Long projectId)
    {
        return taskService.getTaskByProject(projectId);
    }

    @GetMapping("/{id}")
    public TaskDto.Response getTaskById(@PathVariable Long id)
    {
        return taskService.getTaskById(id);
    }
    @PostMapping
    public ResponseEntity<TaskDto.Response> createTask(
            @Valid @RequestBody TaskDto.CreateRequest dto)
    {
        TaskDto.Response response = taskService.createTask(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id)
    {
        taskService.deleteTask(id);
    }
    @PatchMapping("/{id}/status")
    public TaskDto.Response updateTaskStatus(@PathVariable Long id,
                                             @Valid @RequestBody TaskDto.StatusUpdate dto){
        return taskService.updateTaskStatus(id,dto);
    }
    @PutMapping("/{id}")
    public TaskDto.Response updateTask(@PathVariable Long id,
                                       @Valid @RequestBody TaskDto.UpdateRequest dto)
    {
        return taskService.updateTask(id, dto);
    }
}

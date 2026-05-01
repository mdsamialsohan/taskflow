package com.samialsohan.taskflow.service;

import com.samialsohan.taskflow.dto.TaskDto;
import com.samialsohan.taskflow.entity.Project;
import com.samialsohan.taskflow.entity.Task;
import com.samialsohan.taskflow.entity.User;
import com.samialsohan.taskflow.exception.ResourceNotFoundException;
import com.samialsohan.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository,
                       ProjectService projectService,
                       UserService userService){
        this.projectService = projectService;
        this.taskRepository = taskRepository;
        this.userService = userService;
    }
    public List<TaskDto.Response> getTaskByProject(Long projectId)
    {
        projectService.findProjectOrThrow(projectId);
        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    public TaskDto.Response getTaskById(Long id)
    {
        Task task = findTaskOrThrow(id);
        return toResponse(task);
    }
    public TaskDto.Response createTask(TaskDto.CreateRequest dto)
    {
        Project project = projectService.findProjectOrThrow(dto.projectId());
        Task task = new Task(dto.title(), dto.description(), project);
        if(dto.priority()!=null){
            task.setPriority(dto.priority());
        }
        task.setDueDate(dto.dueDate());
        if(dto.assigneeId()!= null){
            User assignee = userService.findUserOrThrow(dto.assigneeId());
            task.setAssignee(assignee);
        }
        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }
    public void deleteTask(Long id){
        if(taskRepository.existsById(id)){
            throw new ResourceNotFoundException("Task",id);
        }
        taskRepository.deleteById(id);
    }
    private Task findTaskOrThrow(Long id){
        return taskRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Task", id ));

    }
    private TaskDto.Response toResponse(Task task)
    {
        return new TaskDto.Response(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.priority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getVersion(),
                task.getProject().getId(),
                task.getProject().getName(),
                task.getAssignee() != null ? task.getAssignee().getId() : null,
                task.getAssignee() != null ? task.getAssignee().getName() : null
        );


    }
}

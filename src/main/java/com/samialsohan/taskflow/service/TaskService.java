package com.samialsohan.taskflow.service;

import com.samialsohan.taskflow.dto.TaskDto;
import com.samialsohan.taskflow.entity.Project;
import com.samialsohan.taskflow.entity.Task;
import com.samialsohan.taskflow.entity.TaskStatus;
import com.samialsohan.taskflow.entity.User;
import com.samialsohan.taskflow.exception.BusinessRuleException;
import com.samialsohan.taskflow.exception.ResourceNotFoundException;
import com.samialsohan.taskflow.repository.TaskRepository;
import jakarta.validation.Valid;
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
    public TaskDto.Response updateTask(Long id, TaskDto.UpdateRequest dto){
        Task task = findTaskOrThrow(id);

        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setDueDate(dto.dueDate());

        if(dto.priority() != null)
            task.setPriority(dto.priority());

        if(dto.assigneeId() != null)
        {
            // Client sent an assignee ID → look up and assign
            User assignee = userService.findUserOrThrow(dto.assigneeId());
            task.setAssignee(assignee);
        }
        else{
            // Client sent null → unassign the task
            task.setAssignee(null);
        }
        Task saved = taskRepository.save(task);
        return toResponse(saved);
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
    public TaskDto.Response updateTaskStatus(Long id, TaskDto.StatusUpdate dto){
        Task task = findTaskOrThrow(id);
        validateStatusTransition(task, task.getStatus(), dto.status());
        task.setStatus(dto.status());
        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }
    private void validateStatusTransition(Task task, TaskStatus from, TaskStatus to){
        if(from == to) return;
        if(from == TaskStatus.DONE)
            throw new BusinessRuleException("Task already completed, Unable to change!");
        if(from == TaskStatus.CANCELLED)
            throw new BusinessRuleException("This task is cancelled, We are unable to change it!");
        switch(from){
            case TODO -> {
                if(to != TaskStatus.IN_PROGRESS){
                    throw new BusinessRuleException("Task in TODO can only move to IN_PROGRESS, not " +to);
                }
            }
            case IN_PROGRESS -> {
                if(to == TaskStatus.DONE && task.getAssignee()==null){
                    throw new BusinessRuleException("Cannot mark task as DONE without an assignee");
                }
                if(to!= TaskStatus.DONE && to!=TaskStatus.CANCELLED){
                    throw new BusinessRuleException("Task in IN_PROGRESS can only move to DONE or CANCELLED, not "+to);
                }
            }
            default -> throw new BusinessRuleException("UnExpected status: "+from);
        }
    }
}

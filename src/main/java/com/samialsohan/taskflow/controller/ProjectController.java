package com.samialsohan.taskflow.controller;

import com.samialsohan.taskflow.dto.ProjectDto;
import com.samialsohan.taskflow.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectDto.Response> getAllProjects(){
        return projectService.getAllProjects();
    }
    @GetMapping("/{id}")
    public ProjectDto.Response getProjectById(@PathVariable Long id){
        return projectService.getProjectById(id);
    }
    @PostMapping
    public ResponseEntity<ProjectDto.Response>createProject(
            @Valid @RequestBody ProjectDto.CreateRequest dto
    )
    {
        ProjectDto.Response response = projectService.createProject(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
    }

}

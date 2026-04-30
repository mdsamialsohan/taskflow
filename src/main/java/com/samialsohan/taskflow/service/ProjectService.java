package com.samialsohan.taskflow.service;

import com.samialsohan.taskflow.dto.ProjectDto;
import com.samialsohan.taskflow.entity.Project;
import com.samialsohan.taskflow.exception.ResourceNotFoundException;
import com.samialsohan.taskflow.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    public ProjectService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }
    public List<ProjectDto.Response>getAllProjects(){
        return projectRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

    }
    public ProjectDto.Response getProjectById(Long id)
    {
        Project project = findProjectOrThrow(id);
        return toResponse(project);
    }
    public ProjectDto.Response createProject(ProjectDto.CreateRequest dto)
    {
        Project project = new Project(dto.name(), dto.description());
        Project saved = projectRepository.save(project);
        return toResponse(saved);
    }
    public void deleteProject(Long id){
        if(!projectRepository.existsById(id)){
            throw new ResourceNotFoundException("Project", id);
        }
        projectRepository.deleteById(id);
    }
    public Project findProjectOrThrow(Long id)
    {
        return projectRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Project",id));
    }
    private ProjectDto.Response toResponse(Project project){ // this method convert entity into dto
        return new ProjectDto.Response(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}

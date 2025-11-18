package com.company.backend.project.controller.impl;

import com.company.backend.project.controller.ProjectController;
import com.company.backend.project.dto.ProjectDto;
import com.company.backend.project.service.ProjectService;
import com.company.backend.project.service.impl.ProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Primary
@RestController
@CrossOrigin("*")
@RequestMapping(path = "api")
public class ProjectControllerImpl implements ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectControllerImpl(ProjectService projectService)
    {
        this.projectService = projectService;
    }


    @Override
    public ResponseEntity<ProjectDto> create(ProjectDto projectDto) {
        return new ResponseEntity<>(this.projectService.create(projectDto) , HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        return ResponseEntity.ok(this.projectService.getAllProjects());
    }

    @Override
    public ResponseEntity<ProjectDto> update(ProjectDto projectDto, String projectName) {
        return ResponseEntity.ok(this.projectService.update(projectDto, projectName));
    }

    @Override
    public void delete(String projectName) {
        this.projectService.delete(projectName);
    }
}

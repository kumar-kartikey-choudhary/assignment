package com.company.backend.project.controller;


import com.company.backend.project.dto.ProjectDto;
import jakarta.persistence.GeneratedValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.DeleteExchange;

import java.util.List;

@ResponseBody
public interface ProjectController {

    @PostMapping(path = "admin/project")
    ResponseEntity<ProjectDto> create(@RequestBody ProjectDto projectDto);

    @GetMapping(path = "projects")
    ResponseEntity<List<ProjectDto>> getAllProjects();

    @PutMapping(path = "admin/project/{projectName}")
    ResponseEntity<ProjectDto> update(@RequestBody ProjectDto projectDto , @PathVariable(name = "projectName") String projectName);

    @DeleteMapping(path = "admin/project/{projectName}")
    void delete(@PathVariable(name = "projectName") String projectName);

}

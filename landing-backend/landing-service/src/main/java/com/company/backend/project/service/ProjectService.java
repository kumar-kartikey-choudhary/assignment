package com.company.backend.project.service;

import com.company.backend.project.dto.ProjectDto;

import java.util.List;

public interface ProjectService {
    ProjectDto create(ProjectDto projectDto);

    List<ProjectDto> getAllProjects();

    ProjectDto update(ProjectDto projectDto, String projectName);

    void delete(String projectName);
}

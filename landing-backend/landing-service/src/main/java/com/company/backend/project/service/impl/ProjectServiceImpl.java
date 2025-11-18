package com.company.backend.project.service.impl;

import com.company.backend.project.model.Project;
import com.company.backend.project.dto.ProjectDto;
import com.company.backend.project.repository.ProjectRepository;
import com.company.backend.project.service.ProjectService;
import com.company.backend.utilty.MapperUtility;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository)
    {
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public ProjectDto create(ProjectDto projectDto) {
        log.info("Inside @class projectServiceImpl @method create  @Param projectDto :{}", projectDto);
        try {

            Project project = MapperUtility.sourceToTarget(projectDto, Project.class);
            project = this.projectRepository.saveAndFlush(project);
            log.info("Our project data sent to the database and stored successfully");
            return MapperUtility.sourceToTarget(project,  ProjectDto.class, "uuid");
        } catch (Exception e) {
            throw new RuntimeException("Error to map project :" + projectDto.getUuid());
        }
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        log.info("Inside @class ProjectServiceImpl @method getAllProjects ");
        List<Project> all = this.projectRepository.findAll();
        return all.stream().map(project -> {
            try {
                return  MapperUtility.sourceToTarget(project, ProjectDto.class);
            } catch (Exception e) {
                throw new RuntimeException("Mapping error for project: " , e);
            }
        }).toList();
    }

    @Override
    @Transactional
    public ProjectDto update(ProjectDto projectDto, String projectName) {
        log.info("Inside @class projectServiceImpl @method update  @Param projectDto :{} projectName :{}", projectDto,projectName);
        try {
            Project project = this.projectRepository.findByName(projectName).orElseThrow(() -> new RuntimeException("project can not found"));
            project.setName(projectDto.getName());
            project.setDescription(projectDto.getDescription());
            project = this.projectRepository.saveAndFlush(project);
            log.info("project updated successfully  and stored to database...");
            return MapperUtility.sourceToTarget(project, ProjectDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void delete(String projectName) {
        log.info("Inside @class ProjectServiceImpl @method delete  @Param  projectName :{}", projectName);
        try {
            Project project = this.projectRepository.findByName(projectName).orElseThrow(() -> new RuntimeException("project can not found"));
            this.projectRepository.delete(project);
        } catch (Exception e) {
            throw new RuntimeException("Project deletion not  possible...");
        }

    }
}

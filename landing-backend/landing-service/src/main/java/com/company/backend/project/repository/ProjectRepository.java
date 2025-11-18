package com.company.backend.project.repository;

import com.company.backend.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project  ,String> {

    Optional<Project> findByName(String name);

}

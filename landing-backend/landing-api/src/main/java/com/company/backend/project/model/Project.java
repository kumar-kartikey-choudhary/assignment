package com.company.backend.project.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "PROJECTS")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PROJECT_UUID" , columnDefinition = "VARCHAR(50) NOT NULL", updatable = false)
    private String uuid;

    @Column(name = "IMAGE_URL" , nullable = false)
    private String imageUrl;

    @Column(name = "PROJECT_NAME" , nullable = false)
    private String name;

    @Column(name = "PROJECT_DESCRIPTION" , nullable = false , length = 1000)
    private String description;

}

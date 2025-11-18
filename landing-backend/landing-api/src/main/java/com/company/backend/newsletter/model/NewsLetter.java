package com.company.backend.newsletter.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "NEWSLETTERS")
public class NewsLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "UUID" , columnDefinition = "VARCHAR(50) NOT NULL", nullable = false)
    private String uuid;

    @Column(name = "EMAIL" , unique = true , nullable = false)
    private String email;

    @Column(name = "SUBSCRIBED_AT",columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime subscribedAt;
}

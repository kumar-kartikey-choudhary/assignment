package com.company.backend.client.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "CLIENTS")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "CLIENT_UUID" , columnDefinition = "VARCHAR(50) NOT NULL ", nullable = false)
    private String uuid;

    @Column(name = "IMAGE_URL" , nullable = false)
    private String imageUrl;


    @Column(name = "CLIENT_NAME" , nullable = false)
    private String name;


    @Column(name = "PROJECT_DESCRIPTION" , nullable = false , length = 1000)
    private String description;

    @Column(name = "DESIGNATION" , nullable = false)
    private String designation;

}

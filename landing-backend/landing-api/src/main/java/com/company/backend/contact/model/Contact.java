package com.company.backend.contact.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "CONTACTS")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "CONTACT_UUID" , columnDefinition = "VARCHAR(50) NOT NULL" , nullable = false)
    private String uuid;

    @Column(name = "FULL_NAME", nullable = false)
    private String fullName;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "ROLE" , nullable = false)
    private String role = "USER";

    @Column(name = "MOBILE_NUMBER" , unique = true , nullable = false)
    private String mobileNumber;

    @Column(name = "CITY" , nullable = false)
    private String city;
}

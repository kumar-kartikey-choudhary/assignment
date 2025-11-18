package com.company.backend.contact.dto;

import lombok.Data;

@Data
public class ContactDto {

    private String uuid;
    private String fullName;
    private String email;
    private String mobileNumber;
    private String city;
}

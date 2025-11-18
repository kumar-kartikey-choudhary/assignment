package com.company.backend.newsletter.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsLetterDto {

    private String uuid;
    private String email;
    private LocalDateTime subscribedAt;
}

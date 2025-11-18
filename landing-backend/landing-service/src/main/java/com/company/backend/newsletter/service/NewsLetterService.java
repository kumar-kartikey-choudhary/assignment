package com.company.backend.newsletter.service;

import com.company.backend.newsletter.dto.NewsLetterDto;

import java.util.List;

public interface NewsLetterService {
    NewsLetterDto save(NewsLetterDto newsLetterDto);

    List<NewsLetterDto> getAllNewsLetter();
}

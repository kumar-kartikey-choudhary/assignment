package com.company.backend.newsletter.controller.impl;

import com.company.backend.newsletter.controller.NewsLetterController;
import com.company.backend.newsletter.dto.NewsLetterDto;
import com.company.backend.newsletter.service.NewsLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Primary
@RestController
@CrossOrigin("*")
@RequestMapping(path = "api")
public class NewLetterControllerImpl implements NewsLetterController {

    private final NewsLetterService newsLetterService;

    @Autowired
    public NewLetterControllerImpl(NewsLetterService newsLetterService)
    {
        this.newsLetterService = newsLetterService;
    }


    @Override
    public ResponseEntity<NewsLetterDto> save(NewsLetterDto newsLetterDto) {
        return new ResponseEntity<>(this.newsLetterService.save(newsLetterDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<NewsLetterDto>> getAllNewsLetter() {
        return ResponseEntity.ok(this.newsLetterService.getAllNewsLetter());
    }
}

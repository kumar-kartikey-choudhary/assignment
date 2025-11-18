package com.company.backend.newsletter.controller;

import com.company.backend.newsletter.dto.NewsLetterDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ResponseBody
public interface NewsLetterController {

    @PostMapping(path = "newsLetter")
    ResponseEntity<NewsLetterDto> save(@RequestBody NewsLetterDto newsLetterDto);

    @GetMapping(path = "admin/newsLetters")
    ResponseEntity<List<NewsLetterDto>> getAllNewsLetter();

}

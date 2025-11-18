package com.company.backend.newsletter.service.impl;

import com.company.backend.newsletter.dto.NewsLetterDto;
import com.company.backend.newsletter.model.NewsLetter;
import com.company.backend.newsletter.repository.NewsLetterRepository;
import com.company.backend.newsletter.service.NewsLetterService;
import com.company.backend.utilty.MapperUtility;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class NewsLetterServiceImpl implements NewsLetterService {

    private final NewsLetterRepository newsLetterRepository;

    @Autowired
    public NewsLetterServiceImpl(NewsLetterRepository newsLetterRepository)
    {
        this.newsLetterRepository = newsLetterRepository;
    }


    @Override
    @Transactional
    public NewsLetterDto save(NewsLetterDto newsLetterDto) {
        log.info("Inside @class NewsLetterServiceImpl @method create  @Param newsLetterDto :{}", newsLetterDto);
        try {
            NewsLetter newsLetter = MapperUtility.sourceToTarget(newsLetterDto, NewsLetter.class);
            newsLetter = this.newsLetterRepository.saveAndFlush(newsLetter);
            log.info("Our newsLetter data sent to the database and stored successfully");
            return MapperUtility.sourceToTarget(newsLetter,  NewsLetterDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error to map newsLetter :" + newsLetterDto.getUuid());
        }

    }

    @Override
    public List<NewsLetterDto> getAllNewsLetter() {
        log.info("Inside @class NewsLetterServiceImpl @method getAllNewsLetters ");
        List<NewsLetter> all = this.newsLetterRepository.findAll();
        return all.stream().map(newsLetter -> {
            try {
                return  MapperUtility.sourceToTarget(newsLetter, NewsLetterDto.class);
            } catch (Exception e) {
                throw new RuntimeException("Mapping error for newsLetter: " , e);
            }
        }).toList();
    }
}

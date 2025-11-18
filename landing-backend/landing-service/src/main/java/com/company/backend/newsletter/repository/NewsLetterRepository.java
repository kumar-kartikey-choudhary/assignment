package com.company.backend.newsletter.repository;

import com.company.backend.newsletter.model.NewsLetter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsLetterRepository extends JpaRepository<NewsLetter , String> {
}

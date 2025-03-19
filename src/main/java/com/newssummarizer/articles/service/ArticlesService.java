package com.newssummarizer.articles.service;

import com.newssummarizer.articles.repository.ArticleEntity;
import com.newssummarizer.articles.repository.ArticlesRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
@Setter
public class ArticlesService {

    @Autowired
    private ArticlesRepository repository;

    public Page<ArticleEntity> findAllByPage(Pageable request) {
        return repository.findAll(request);
    }

    public Optional<ArticleEntity> findById(BigInteger id) {
        return repository.findById(id);
    }

    public String findSummary(BigInteger id) {
        return repository.findById(id).isPresent()? repository.findById(id).get().getSummary() : null;
    }
}

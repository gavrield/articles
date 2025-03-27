package com.newssummarizer.articles.service;

import com.newssummarizer.articles.controller.PaginatedResponse;
import com.newssummarizer.articles.dto.ArticleDto;
import com.newssummarizer.articles.mapper.ArticleMapper;
import com.newssummarizer.articles.repository.ArticleEntity;
import com.newssummarizer.articles.repository.ArticlesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticlesService {

    private final ArticlesRepository repository;
    private final ArticleMapper mapper;

    public PaginatedResponse<ArticleDto> findAllByPage(Pageable request) {
        Page<ArticleEntity> page = repository.findAll(request);
        return new PaginatedResponse<>(
                mapper.toArticleDtoList(page.getContent()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

    public ArticleDto findById(BigInteger id) {
        Optional<ArticleEntity> article = repository.findById(id);
        if (article.isPresent())
            return mapper.toArticleDto(article.get());
        else return null;
    }

    public String findSummary(BigInteger id) {
        return repository.findById(id).map(ArticleEntity::getSummary).orElse(null);
    }
}

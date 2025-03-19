package com.newssummarizer.articles.controller;

import com.newssummarizer.articles.repository.ArticleEntity;
import com.newssummarizer.articles.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.Optional;

@RestController
public class ArticlesController {

    @Autowired
    private ArticlesService service;

    @GetMapping(path = "/articles")
    public Page<ArticleEntity> findAllByPage(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "5") int sizePerPage,
                                             @RequestParam(defaultValue = "ID") SortField sortField,
                                             @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection) {
        Pageable pageable = PageRequest.of(page, sizePerPage, sortDirection, String.valueOf(sortField));
        return service.findAllByPage(pageable);
    }

    @GetMapping(path = "/articles/{id}")
    public Optional<ArticleEntity> findById(@PathVariable BigInteger id) {
        return service.findById(id);
    }

    @GetMapping(path = "/articles/{id}/summary")
    public String findSummary(@PathVariable BigInteger id) {
        return service.findSummary(id);
    }
}

package com.newssummarizer.articles.controller;


import com.newssummarizer.articles.dto.ArticleDto;
import com.newssummarizer.articles.service.ArticlesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequiredArgsConstructor
public class ArticlesController {

    private final ArticlesService service;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 5;
    private static final String DEFAULT_SORT_FIELD = "ID";
    private static final String DEFAULT_DIRECTION = "DESC";


    @GetMapping(path = "/api/v1/articles")
    public PaginatedResponse<ArticleDto> findAllByPage(@RequestParam(defaultValue = "" + DEFAULT_PAGE) int page,
                                                       @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int sizePerPage,
                                                       @RequestParam(defaultValue = DEFAULT_SORT_FIELD) SortField sortField,
                                                       @RequestParam(defaultValue = DEFAULT_DIRECTION) Sort.Direction sortDirection) {
        Pageable pageable = PageRequest.of(page, sizePerPage, sortDirection, sortField.getDatabaseFieldName());
        return service.findAllByPage(pageable);

    }

    @GetMapping(path = "/api/v1/articles/{id}")
    public ArticleDto findById(@PathVariable BigInteger id) {
        return service.findById(id);
    }

    @GetMapping(path = "/api/v1/articles/{id}/summary")
    public String findSummary(@PathVariable BigInteger id) {
        return service.findSummary(id);
    }
}

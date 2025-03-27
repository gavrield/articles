package com.newssummarizer.articles.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ArticleDto {
    private BigInteger id;
    private String query;
    private String title;
    private String author;
    private String publishedAt;
    private String url;
    private String summary;
}

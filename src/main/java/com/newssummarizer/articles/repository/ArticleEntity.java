package com.newssummarizer.articles.repository;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Document(collection = "articles")
@TypeAlias("article")
@Data
public class ArticleEntity {

    @Id
    private BigInteger id;
    private String query;
    private String title;
    private String author;
    private String publishedAt;
    private String url;
    private String summary;
}


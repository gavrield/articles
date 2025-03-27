package com.newssummarizer.articles.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortField {
    ID("id"),
    TITLE("title"),
    QUERY("query"),
    AUTHOR("author"),
    PUBLISHED_DATE("publishedDate");

    private final String databaseFieldName;
}

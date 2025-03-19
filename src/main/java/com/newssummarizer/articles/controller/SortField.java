package com.newssummarizer.articles.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortField {
    ID("_id");

    private final String databaseFieldName;
}

package com.newssummarizer.articles.controller;


import java.util.List;

public record PaginatedResponse<T>(List<T> content, int number, int size, long totalElements) {

}

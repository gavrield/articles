package com.newssummarizer.articles.service;

import com.newssummarizer.articles.controller.PaginatedResponse;
import com.newssummarizer.articles.dto.ArticleDto;
import com.newssummarizer.articles.mapper.ArticleMapper;
import com.newssummarizer.articles.repository.ArticleEntity;
import com.newssummarizer.articles.repository.ArticlesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticlesServiceTest {

    @Mock
    private ArticlesRepository repository;

    @Mock
    private ArticleMapper mapper;

    @InjectMocks
    private ArticlesService service;

    private static Stream<Arguments> findAllByPageArguments() {
        return Stream.of(
                Arguments.of(
                        0, 10,
                        Arrays.asList(
                                createArticleEntity(BigInteger.valueOf(1), "Title 1", "Summary 1"),
                                createArticleEntity(BigInteger.valueOf(2), "Title 2", "Summary 2")
                        ),
                        Arrays.asList(
                                createArticleDto(BigInteger.valueOf(1), "Title 1", "Summary 1"),
                                createArticleDto(BigInteger.valueOf(2), "Title 2", "Summary 2")
                        ),
                        0, // Changed to expected page number
                        10, // Changed to expected page size
                        2L,
                        "Success with data"
                ),
                Arguments.of(
                        1, 5,
                        Arrays.asList(
                                createArticleEntity(BigInteger.valueOf(3), "Title 3", "Summary 3")
                        ),
                        Arrays.asList(
                                createArticleDto(BigInteger.valueOf(3), "Title 3", "Summary 3")
                        ),
                        1, // Changed to expected page number
                        5, // Changed to expected page size
                        6L, // Changed expected totalElements to 6
                        "Success with different page and size"
                ),
                Arguments.of(
                        0, 10,
                        Collections.emptyList(),
                        Collections.emptyList(),
                        0, // Changed to expected page number
                        10, // Changed to expected page size
                        0L,
                        "Empty result"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("findAllByPageArguments")
    void testFindAllByPage(int pageNumber, int pageSize, List<ArticleEntity> entities, List<ArticleDto> dtos, int expectedNumber, int expectedSize, long totalElements, String testName) {
        // Arrange
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ArticleEntity> articlePage = new PageImpl<>(entities, pageable, totalElements);

        when(repository.findAll(pageable)).thenReturn(articlePage);
        when(mapper.toArticleDtoList(entities)).thenReturn(dtos);

        // Act
        PaginatedResponse<ArticleDto> response = service.findAllByPage(pageable);

        // Assert
        assertNotNull(response, "Response should not be null for: " + testName);
        assertEquals(expectedNumber, response.number(), "Page number mismatch for: " + testName);
        assertEquals(expectedSize, response.size(), "Page size mismatch for: " + testName);
        assertEquals(totalElements, response.totalElements(), "Total elements mismatch for: " + testName);
        assertEquals(dtos, response.content(), "Content mismatch for: " + testName);

        verify(repository, times(1)).findAll(pageable);
        verify(mapper, times(1)).toArticleDtoList(entities);
    }

    private static Stream<Arguments> findByIdArguments() {
        return Stream.of(
                Arguments.of(
                        BigInteger.valueOf(1),
                        Optional.of(createArticleEntity(BigInteger.valueOf(1), "Test Title", "Test Summary")),
                        createArticleDto(BigInteger.valueOf(1), "Test Title", "Test Summary"),
                        "Existing article"
                ),
                Arguments.of(
                        BigInteger.valueOf(999),
                        Optional.empty(),
                        null,
                        "Non-existing article"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("findByIdArguments")
    void testFindById(BigInteger articleId, Optional<ArticleEntity> entityOptional, ArticleDto expectedDto, String testName) {
        // Arrange
        when(repository.findById(articleId)).thenReturn(entityOptional);
        if (entityOptional.isPresent()) {
            when(mapper.toArticleDto(entityOptional.get())).thenReturn(expectedDto);
        }

        // Act
        ArticleDto result = service.findById(articleId);

        // Assert
        assertEquals(expectedDto, result, "Result mismatch for: " + testName);
        verify(repository, times(1)).findById(articleId);
        if (entityOptional.isPresent()) {
            verify(mapper, times(1)).toArticleDto(entityOptional.get());
        } else {
            verify(mapper, never()).toArticleDto(any());
        }
    }

    private static Stream<Arguments> findSummaryArguments() {
        return Stream.of(
                Arguments.of(
                        BigInteger.valueOf(1),
                        Optional.of(createArticleEntity(BigInteger.valueOf(1), "Test Title", "This is a test summary.")),
                        "This is a test summary.",
                        "Existing article"
                ),
                Arguments.of(
                        BigInteger.valueOf(999),
                        Optional.empty(),
                        null,
                        "Non-existing article"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("findSummaryArguments")
    void testFindSummary(BigInteger articleId, Optional<ArticleEntity> entityOptional, String expectedSummary, String testName) {
        // Arrange
        when(repository.findById(articleId)).thenReturn(entityOptional);

        // Act
        String result = service.findSummary(articleId);

        // Assert
        assertEquals(expectedSummary, result, "Summary mismatch for: " + testName);
        verify(repository, times(1)).findById(articleId);
    }

    private static ArticleEntity createArticleEntity(BigInteger id, String title, String summary) {
        ArticleEntity entity = new ArticleEntity();
        entity.setId(id);
        entity.setTitle(title);
        entity.setSummary(summary);
        return entity;
    }

    private static ArticleDto createArticleDto(BigInteger id, String title, String summary) {
        ArticleDto dto = new ArticleDto();
        dto.setId(id);
        dto.setTitle(title);
        dto.setSummary(summary);
        return dto;
    }
}
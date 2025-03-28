package com.newssummarizer.articles.controller;

import com.newssummarizer.articles.dto.ArticleDto;
import com.newssummarizer.articles.dto.PaginatedResponse;
import com.newssummarizer.articles.service.ArticlesService;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.domain.Pageable;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticlesController.class)
class ArticlesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArticlesService articlesService;


    @ParameterizedTest
    @CsvSource({
            "0, 5, TITLE, ASC, 3, 3", // Page 0, size 5, should get all 3, total 3
            "0, 2, ID, DESC, 2, 3",   // Page 0, size 2, should get first 2, total 3
            "1, 2, QUERY, ASC, 1, 3"  // Page 1, size 2, should get the last 1 (index 2), total 3
    })
    void findAllByPage_shouldReturnListOfArticleDtoWithPaginationDetails(
            int page, int sizePerPage, String sortFieldName, String sortDirection, int expectedContentLength, long totalElements
    ) throws Exception {
        // Arrange

        PaginatedResponse<ArticleDto> paginatedResponse = getArticleDtoPaginatedResponse(page, sizePerPage, totalElements);

        when(articlesService.findAllByPage(any(Pageable.class))).thenReturn(paginatedResponse); // Mock to return PaginatedResponse

        // Act & Assert
        mockMvc.perform(get("/api/v1/articles")
                        .param("page", String.valueOf(page))
                        .param("sizePerPage", String.valueOf(sizePerPage))
                        .param("sortField", SortField.valueOf(sortFieldName).name())
                        .param("sortDirection", sortDirection)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(expectedContentLength))
                .andExpect(jsonPath("$.number").value(page))
                .andExpect(jsonPath("$.size").value(sizePerPage))
                .andExpect(jsonPath("$.totalElements").value(totalElements));
    }

    private static PaginatedResponse<ArticleDto> getArticleDtoPaginatedResponse(int page, int sizePerPage, long totalElements) {
        List<ArticleDto> allArticleDtos = List.of(
                new ArticleDto(BigInteger.ONE, "query1", "Title 1", "Author 1", "2024-01-01", "url1", "Summary 1"),
                new ArticleDto(BigInteger.TWO, "query2", "Title 2", "Author 2", "2024-01-02", "url2", "Summary 2"),
                new ArticleDto(BigInteger.valueOf(3), "query3", "Title 3", "Author 3", "2024-01-03", "url3", "Summary 3")
        );

        int start = page * sizePerPage;
        int end = Math.min(start + sizePerPage, allArticleDtos.size());
        List<ArticleDto> content = new ArrayList<>();

        if (start < allArticleDtos.size()) {
            for (int i = start; i < end ; i++) {
                content.add(allArticleDtos.get(i));
            }
        }

        return new PaginatedResponse<>(
                content,
                page,
                sizePerPage,
                totalElements
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1, query1, Test Title 1, Author A",
            "2, query2, Another Title, Author B",
            "100, query100, Some Other Article, Author C"
    })
    void findById_existingId_shouldReturnArticleDto(BigInteger articleId, String query, String expectedTitle, String author) throws Exception {
        // Arrange
        ArticleDto articleDto = new ArticleDto(articleId, query, expectedTitle, author, "2024-03-08", "url", "Test Summary");
        when(articlesService.findById(articleId)).thenReturn(articleDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/articles/{id}", articleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(articleId.intValue()))
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.query").value(query))
                .andExpect(jsonPath("$.author").value(author));
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "500",
            "9999"
    })
    void findById_nonExistingId_shouldReturnNullOrAppropriateError(BigInteger articleId) throws Exception {
        // Arrange
        when(articlesService.findById(articleId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/v1/articles/{id}", articleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @ParameterizedTest
    @CsvSource({
            "1, This is a summary for article 1.",
            "5, Summary for article 5.",
            "10, A different summary."
    })
    void findSummary_existingId_shouldReturnSummary(BigInteger articleId, String expectedSummary) throws Exception {
        // Arrange
        when(articlesService.findSummary(articleId)).thenReturn(expectedSummary);

        // Act & Assert
        mockMvc.perform(get("/api/v1/articles/{id}/summary", articleId)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedSummary));
    }

    @ParameterizedTest
    @CsvSource({
            "100",
            "200",
            "3000"
    })
    void findSummary_nonExistingId_shouldReturnNullOrAppropriateError(BigInteger articleId) throws Exception {
        // Arrange
        when(articlesService.findSummary(articleId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/v1/articles/{id}/summary", articleId)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
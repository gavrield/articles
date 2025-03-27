package com.newssummarizer.articles.mapper;

import com.newssummarizer.articles.dto.ArticleDto;
import com.newssummarizer.articles.repository.ArticleEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    ArticleDto toArticleDto(ArticleEntity entity);
    List<ArticleDto> toArticleDtoList(List<ArticleEntity> articleEntityList);
}

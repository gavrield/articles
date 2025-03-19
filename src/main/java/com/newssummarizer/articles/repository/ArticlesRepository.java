package com.newssummarizer.articles.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ArticlesRepository extends MongoRepository<ArticleEntity, BigInteger> {
}

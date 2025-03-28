# Articles Storage Microservice

## Overview

This project implements a microservice responsible for storing and retrieving news articles. It is part of a larger news summarization system that provides an API for other services to access and manage article data.

**Key Components**:

- MongoDB Atlas for data storage
- Gemini integration for article summarization
- NewsAPI.org for article fetching

GitHub Repository: [https://github.com/gavrield/articles](https://github.com/gavrield/articles)

## Features

### Article Storage

- Stores articles with attributes:
  - id
  - title
  - content
  - url
  - publishedDate
  - source

### Article Retrieval

- Endpoints for retrieving articles by various criteria

### Data Management

- MongoDB Atlas for data persistence

### Gemini Integration

- Google Gemini API integration
- Article content summarization

### NewsAPI.org Integration

- Fetches articles from NewsApi.org
- Supports keyword/category based fetching (e.g., 'Technology')

## Architecture

| Component              | Technology           |
|------------------------|---------------------|
| Microservice Framework | Spring Boot         |
| Service Communication  | REST                |
| Database               | MongoDB             |
| API Design             | RESTful principles  |

## Technologies

- **Core**:
  - Spring Boot
  - Spring Data MongoDB
  - Gradle

- **Services**:
  - MongoDB Atlas
  - Gemini API
  - NewsAPI.org

- **Deployment**:
  - Docker

## Setup Instructions

### Prerequisites

- JDK 21
- MongoDB Atlas account
- NewsAPI.org API key
- Docker (optional)

### Installation

1. Clone repository:

   ```bash
   git clone https://github.com/gavrield/articles
   ```
  
2. Build with Gradle:

   ```bash
   ./gradlew build
   ```

### Configuration

Set these in `application.properties`:

```properties
# MongoDB
spring.data.mongodb.uri=mongodb+srv://gavriel3:${MONGODB_PASSWORD}@cluster0.svmq8.mongodb.net/NewsSummary?retryWrites=true&w=majority&appName=Cluster0
```

### Running

**Without Docker**:

```bash
./gradlew bootRun
```

**With Docker**:

```bash
docker build -t articles-service .
docker run -p 8080:8080 articles-service
```

**With Docker-Compose**
Set .env file with MONGODB_PASSWORD variable
Start services:

   ```bash
   docker-compose up -build
   ```

## API Documentation

### Base URL

`https://your-service-url/api/v1`

### Endpoints

#### 1. Get Paginated Articles

```
GET /articles
```

**Parameters**:

- `page` (default: 0)
- `size` (default: 5)
- `sortField` (default: ID)
- `sortDirection` (default: DESC)

**Response**:

```json
{
  "content": [
    {
      "id": 1,
      "title": "Sample Article",
      "url": "https://example.com",
      "publishedAt": "2023-01-01",
      "summary": "Article summary"
    }
  ],
  "number": 0,
  "size": 1,
  "totalElements": 10
}
```

#### 2. Get Article by ID

```bash
GET /articles/{id}
```

#### 3. Get Article Summary

```bash
GET /articles/{id}/summary
```

## Testing

Run all tests:

```bash
./gradlew test
```

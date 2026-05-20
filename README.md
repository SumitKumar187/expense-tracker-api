# Expense Tracker API

A production-ready Expense Tracker REST API built using Spring Boot 3, JWT Authentication, MySQL, Docker, and Swagger.

---

# Features

- User Registration & Login
- JWT Authentication & Authorization
- Expense CRUD APIs
- Category CRUD APIs
- Expense Filtering by Date
- Swagger API Documentation
- Global Exception Handling
- Validation Handling
- Unit Testing
- Integration Testing
- Dockerized Application
- Docker Compose Setup

---

# Tech Stack

| Technology | Usage |
|---|---|
| Java 17 | Backend Language |
| Spring Boot 3 | REST API Framework |
| Spring Security | Authentication |
| JWT | Token-based Security |
| MySQL | Database |
| Hibernate/JPA | ORM |
| Maven | Build Tool |
| Swagger/OpenAPI | API Documentation |
| Docker | Containerization |
| Docker Compose | Multi-container Setup |
| JUnit & Mockito | Testing |

---

# Project Structure

src/main/java

- controller → REST Controllers
- service → Business Logic Interfaces
- service/impl → Service Implementations
- repository → Database Access
- entity → Database Entities
- dto → Request/Response DTOs
- security → JWT Security Classes
- exception → Global Exception Handling
- config → Security & Swagger Configuration

---

# Authentication Flow

1. User registers
2. User logs in
3. JWT token is generated
4. Token is sent in Authorization header
5. Protected APIs validate token

Example:

Authorization: Bearer YOUR_TOKEN

---

# API Documentation

Swagger UI:

```bash
http://localhost:8080/swagger-ui/index.html
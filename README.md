# Library Management System - Spring Boot

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-Educational-yellow)](LICENSE)

A comprehensive library management system demonstrating both **Monolithic** and **Microservices** architectures using Spring Boot.

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Contributors](#contributors)

---

## 🎯 Project Overview

This project implements a complete library management system that handles:
- **Books** - Book inventory management
- **Members** - Library member registration and management
- **Loans** - Book borrowing and return operations

The system is implemented in **two architectural styles**:
1. **Monolithic Application** (Phase 1)
2. **Microservices Architecture** (Phase 2)

---

## ✨ Features

### Core Business Features
- ✅ CRUD operations for Books, Members, and Loans
- ✅ Book availability tracking
- ✅ Member status management (ACTIVE, INACTIVE, SUSPENDED)
- ✅ Loan status tracking (ACTIVE, RETURNED, OVERDUE)
- ✅ Automatic inventory updates on loan/return

### Technical Features
- ✅ RESTful API design
- ✅ DTO pattern with MapStruct
- ✅ Comprehensive validation
- ✅ Global exception handling
- ✅ JPA relationships
- ✅ Service discovery (Microservices)
- ✅ Centralized configuration (Microservices)
- ✅ API Gateway pattern (Microservices)
- ✅ Inter-service communication with Feign (Microservices)

---

## 🛠️ Technologies Used

### Backend
- **Java 17**
- **Spring Boot 3.4.1**
- **Spring Data JPA**
- **Spring Cloud 2024.0.0** (for microservices)
- **MySQL 8.0**
- **MapStruct 1.5.5**
- **Lombok**
- **Bean Validation API**

### Microservices Components
- **Netflix Eureka** - Service Discovery
- **Spring Cloud Config** - Centralized Configuration
- **Spring Cloud Gateway** - API Gateway
- **OpenFeign** - Declarative REST Client

### Build Tool
- **Maven**

---

## 🏗️ Architecture

### Phase 1: Monolithic Architecture

![Monolithic Architecture](docs/monolithic-architecture.png)

**Layers:**
- **Controller Layer** - REST API endpoints
- **Service Layer** - Business logic
- **Repository Layer** - Data access
- **Entity Layer** - Domain models

**Database:** Single MySQL database (`library_db`)

### Phase 2: Microservices Architecture

![Microservices Architecture](docs/microservices-architecture.png)

**Services:**
- **Config Service** (8888) - Centralized configuration
- **Eureka Service** (8761) - Service registry
- **API Gateway** (8080) - Single entry point
- **Book Service** (8081) - Book management
- **Member Service** (8082) - Member management
- **Loan Service** (8083) - Loan management with Feign clients

**Databases:** Separate database per service
- `library_books_db`
- `library_members_db`
- `library_loans_db`

---

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- IntelliJ IDEA or any Java IDE

### Database Setup
```sql
-- For Phase 1
CREATE DATABASE library_db;

-- For Phase 2
CREATE DATABASE library_books_db;
CREATE DATABASE library_members_db;
CREATE DATABASE library_loans_db;
```

### Phase 1: Running Monolithic Application

1. **Clone the repository**
```bash
git clone https://github.com/YOUR_USERNAME/library-management-system-springboot.git
cd library-management-system-springboot/phase1-monolithic
```

2. **Update database credentials**
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

3. **Build and run**
```bash
mvn clean install
mvn spring-boot:run
```

4. **Access the application**
- Base URL: `http://localhost:8080`
- API endpoints: `http://localhost:8080/api/books`

### Phase 2: Running Microservices

1. **Update configuration**
Edit password in `config-service/src/main/resources/config/*.yml`

2. **Start services in order:**
```bash
# 1. Config Service
cd phase2-microservices/config-service
mvn spring-boot:run

# 2. Eureka Service
cd ../eureka-service
mvn spring-boot:run

# 3. Book Service
cd ../book-service
mvn spring-boot:run

# 4. Member Service
cd ../member-service
mvn spring-boot:run

# 5. Loan Service
cd ../loan-service
mvn spring-boot:run

# 6. API Gateway
cd ../api-gateway
mvn spring-boot:run
```

3. **Access services**
- Eureka Dashboard: `http://localhost:8761`
- API Gateway: `http://localhost:8080`
- All requests go through gateway on port 8080

---

## 📡 API Documentation

### Books API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/books` | Get all books |
| GET | `/api/books/{id}` | Get book by ID |
| POST | `/api/books` | Create new book |
| PUT | `/api/books/{id}` | Update book |
| DELETE | `/api/books/{id}` | Delete book |

**Example Request:**
```json
POST /api/books
{
  "title": "Clean Code",
  "isbn": "978-0132350884",
  "author": "Robert C. Martin",
  "publisher": "Prentice Hall",
  "publishedYear": 2008,
  "availableCopies": 5
}
```

### Members API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/members` | Get all members |
| GET | `/api/members/{id}` | Get member by ID |
| POST | `/api/members` | Create new member |
| PUT | `/api/members/{id}` | Update member |
| DELETE | `/api/members/{id}` | Delete member |

**Example Request:**
```json
POST /api/members
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "membershipDate": "2024-01-01",
  "status": "ACTIVE"
}
```

### Loans API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/loans` | Get all loans |
| GET | `/api/loans/{id}` | Get loan by ID |
| POST | `/api/loans` | Create new loan |
| PUT | `/api/loans/{id}/return` | Return book |
| DELETE | `/api/loans/{id}` | Delete loan |
| GET | `/api/loans/member/{id}` | Get loans by member |
| GET | `/api/loans/book/{id}` | Get loans by book |

**Example Request:**
```json
POST /api/loans
{
  "bookId": 1,
  "memberId": 1,
  "loanDate": "2024-01-15",
  "dueDate": "2024-02-15",
  "status": "ACTIVE"
}
```

---

## 🗄️ Database Schema

### Entities and Relationships
```
┌─────────────┐         ┌──────────────┐         ┌─────────────┐
│    Book     │         │     Loan     │         │   Member    │
├─────────────┤         ├──────────────┤         ├─────────────┤
│ id (PK)     │◄───────┤│ id (PK)      │├───────►│ id (PK)     │
│ title       │   1:M   │ book_id (FK) │   M:1   │ firstName   │
│ isbn (UQ)   │         │ member_id(FK)│         │ lastName    │
│ author      │         │ loan_date    │         │ email (UQ)  │
│ publisher   │         │ due_date     │         │ phone       │
│ publishedYear│         │ return_date  │         │ membership  │
│ available   │         │ status       │         │ status      │
│  Copies     │         └──────────────┘         └─────────────┘
└─────────────┘
```

---

## 🧪 Testing

### Using Postman

1. Import the Postman collection (if provided)
2. Set base URL: `http://localhost:8080`
3. Test all CRUD operations

### Using cURL
```bash
# Create a book
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Book",
    "isbn": "978-1234567890",
    "author": "Test Author",
    "publisher": "Test Publisher",
    "publishedYear": 2024,
    "availableCopies": 10
  }'

# Get all books
curl http://localhost:8080/api/books
```

---

## 📂 Project Structure

### Phase 1 - Monolithic
```
library-management-monolith/
├── src/main/java/com/library/
│   ├── LibraryManagementApplication.java
│   ├── controller/
│   │   ├── BookController.java
│   │   ├── MemberController.java
│   │   └── LoanController.java
│   ├── service/
│   │   ├── BookService.java
│   │   ├── MemberService.java
│   │   └── LoanService.java
│   ├── repository/
│   │   ├── BookRepository.java
│   │   ├── MemberRepository.java
│   │   └── LoanRepository.java
│   ├── entity/
│   │   ├── Book.java
│   │   ├── Member.java
│   │   └── Loan.java
│   ├── dto/
│   │   ├── BookDTO.java
│   │   ├── MemberDTO.java
│   │   ├── LoanDTO.java
│   │   └── LoanResponseDTO.java
│   ├── mapper/
│   │   ├── BookMapper.java
│   │   ├── MemberMapper.java
│   │   └── LoanMapper.java
│   └── exception/
│       ├── GlobalExceptionHandler.java
│       ├── ResourceNotFoundException.java
│       ├── DuplicateResourceException.java
│       ├── BusinessException.java
│       └── ErrorResponse.java
└── src/main/resources/
    └── application.properties
```

### Phase 2 - Microservices
```
phase2-microservices/
├── config-service/
├── eureka-service/
├── api-gateway/
├── book-service/
├── member-service/
└── loan-service/
```

---



## 📝 License

This project is for educational purposes only.

---

## 🙏 Acknowledgments

- Spring Boot Documentation
- Baeldung Tutorials
- Spring Cloud Documentation
- MapStruct Documentation



**Last Updated:** January 2026

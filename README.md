# Library Management System

A full-featured **Library Management System** REST API built with **Spring Boot**, **Spring Security (JWT)**, **Spring Data JPA**, and **MySQL**. It supports book cataloguing, book loans (checkout/check-in/renewal), reservations, fines, subscriptions, and email notifications.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Configuration](#configuration)
  - [Running the Application](#running-the-application)
- [Authentication](#authentication)
- [API Reference](#api-reference)
  - [Auth](#auth)
  - [Books (User)](#books-user)
  - [Books (Admin)](#books-admin)
  - [Genres (User)](#genres-user)
  - [Genres (Admin)](#genres-admin)
  - [Book Loans (User)](#book-loans-user)
  - [Book Loans (Admin)](#book-loans-admin)
  - [Reservations (User)](#reservations-user)
  - [Reservations (Admin)](#reservations-admin)
  - [Fines (User)](#fines-user)
  - [Fines (Admin)](#fines-admin)
  - [Subscriptions (User)](#subscriptions-user)
  - [Subscriptions (Admin)](#subscriptions-admin)
  - [Subscription Plans (User)](#subscription-plans-user)
  - [Subscription Plans (Admin)](#subscription-plans-admin)
  - [User Profile](#user-profile)
- [Roles & Permissions](#roles--permissions)
- [Running Tests](#running-tests)

---

## Features

- **User Authentication** — JWT-based login, signup, forgot/reset password, and token verification.
- **Book Catalogue** — CRUD for books (admin), full-text and advanced search, bulk creation, soft & hard delete.
- **Genre Management** — Hierarchical genres (parent/child) with book counts.
- **Book Loans** — Checkout, check-in, and renewal workflows with overdue tracking.
- **Reservations** — Queue-based reservation system; users can reserve unavailable books; admins can fulfill or cancel reservations.
- **Fines** — Automatic fine generation for overdue returns; Razorpay payment integration; admin waive support.
- **Subscriptions** — Users subscribe to plans with different borrowing privileges; admin activation/cancellation.
- **Email Notifications** — Automated emails via Spring Mail for password reset, reservation availability, fine notices, etc.
- **Role-Based Access Control** — `ROLE_USER` and `ROLE_ADMIN` via Spring Security.
- **Pagination & Sorting** — All list endpoints support paginated, sorted responses.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.x |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL |
| Email | Spring Boot Starter Mail |
| Payments | Razorpay Java SDK 1.4.8 |
| Utilities | Lombok |
| Build Tool | Maven (mvnw wrapper included) |

---

## Project Structure

```
src/
└── main/
    └── java/com/app/Library_Management/
        ├── LibraryManagementApplication.java   # Entry point
        ├── configuration/                      # JWT config & Spring Security setup
        │   ├── JwtConstant.java
        │   ├── JwtProvider.java
        │   ├── JwtValidator.java
        │   └── SecurityConfig.java
        ├── controller/
        │   ├── admin/                          # Admin-only REST controllers
        │   └── user/                           # Public / authenticated REST controllers
        ├── domain/                             # Enums (roles, statuses, types)
        ├── exception/                          # Custom exceptions & GlobalException handler
        ├── mapper/                             # Entity <-> DTO mappers
        ├── model/                              # JPA entities
        ├── payload/
        │   ├── dto/                            # Response DTOs
        │   ├── request/                        # Request payload classes
        │   └── response/                       # Wrapper response classes
        ├── repository/                         # Spring Data JPA repositories
        └── service/                            # Service interfaces & implementations
└── test/
    └── java/com/app/Library_Management/
        └── LibraryManagementApplicationTests.java
```

---

## Getting Started

### Prerequisites

- Java 21 or later
- Maven 3.8+ (or use the included `mvnw` wrapper)
- MySQL 8.x running locally (or accessible remotely)
- (Optional) A Razorpay account for payment integration
- (Optional) An SMTP server / Gmail app-password for email notifications

### Configuration

Create `src/main/resources/application.properties` (file is gitignored) and populate the following:

```properties
# Server
server.port=8080

# MySQL DataSource
spring.datasource.url=jdbc:mysql://localhost:3306/library_db?createDatabaseIfNotExist=true
spring.datasource.username=<your_mysql_username>
spring.datasource.password=<your_mysql_password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT
jwt.secret=<your_jwt_secret_key>

# Spring Mail (e.g. Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=<your_email>
spring.mail.password=<your_app_password>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Razorpay
razorpay.key.id=<your_razorpay_key_id>
razorpay.key.secret=<your_razorpay_key_secret>
```

### Running the Application

```bash
# Using the Maven wrapper
./mvnw spring-boot:run

# Or build a JAR and run it
./mvnw clean package -DskipTests
java -jar target/Library_Management-0.0.1-SNAPSHOT.jar
```

The API will be available at `http://localhost:8080`.

---

## Authentication

All protected endpoints require a **Bearer JWT token** in the `Authorization` header:

```
Authorization: Bearer <JWT_TOKEN>
```

Tokens are obtained from the `/api/auth/login` endpoint (see below).

---

## API Reference

### Auth

Base URL: `/api/auth`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/login` | Login and receive a JWT token | No |
| `POST` | `/signup` | Register a new user account | No |
| `POST` | `/forgot-password` | Send a password-reset email | No |
| `POST` | `/reset-password` | Reset password using token | No |
| `GET` | `/verify-token` | Verify a JWT token | No |
| `GET` | `/health` | Health check endpoint | No |

---

### Books (User)

Base URL: `/api/book`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/` | Get all books (paginated) | Yes |
| `GET` | `/search` | Search books by keyword | Yes |
| `GET` | `/{id}` | Get book by ID | Yes |
| `POST` | `/advanced-search` | Advanced filtered book search | Yes |
| `GET` | `/states` | Get book availability statistics | Yes |

---

### Books (Admin)

Base URL: `/api/admin/book`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/create` | Create a single book | Admin |
| `POST` | `/create/bulk` | Bulk create books | Admin |
| `PUT` | `/{id}` | Update a book | Admin |
| `DELETE` | `/{id}` | Soft-delete a book | Admin |
| `DELETE` | `/{id}/hard` | Hard-delete a book permanently | Admin |

---

### Genres (User)

Base URL: `/api/genres`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/` | List all genres | Yes |
| `GET` | `/{genreId}` | Get genre by ID | Yes |
| `GET` | `/top-level` | Get top-level (root) genres | Yes |
| `GET` | `/count` | Get total genre count | Yes |
| `GET` | `/{genreId}/book-count` | Get number of books in a genre | Yes |

---

### Genres (Admin)

Base URL: `/api/admin/genres`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/create` | Create a new genre | Admin |
| `PUT` | `/{genreId}` | Update a genre | Admin |
| `DELETE` | `/{genreId}` | Soft-delete a genre | Admin |
| `DELETE` | `/{genreId}/hard` | Hard-delete a genre permanently | Admin |

---

### Book Loans (User)

Base URL: `/api/book-loan`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/checkout` | Checkout a book | Yes |
| `POST` | `/checkin` | Return (check in) a book | Yes |
| `POST` | `/renew` | Renew an active loan | Yes |
| `GET` | `/my-loans` | Get current user's loans | Yes |

---

### Book Loans (Admin)

Base URL: `/api/admin/book-loan`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/checkout/{userId}` | Checkout a book on behalf of a user | Admin |
| `POST` | `/search` | Search loans with filters | Admin |
| `POST` | `/update-overdue` | Batch-update overdue loan statuses | Admin |

---

### Reservations (User)

Base URL: `/api/reservations`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/` | Create a reservation for the current user | Yes |
| `GET` | `/my` | Get current user's reservations (paginated) | Yes |
| `GET` | `/{reservationId}` | Get a specific reservation by ID | Yes |
| `DELETE` | `/{reservationId}` | Cancel a reservation | Yes |

---

### Reservations (Admin)

Base URL: `/api/admin/reservations`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/` | Create a reservation for a specific user | Admin |
| `GET` | `/` | Search reservations with filters | Admin |
| `GET` | `/{reservationId}` | Get a reservation by ID | Admin |
| `PUT` | `/{reservationId}/fulfill` | Fulfill a reservation (auto-checkout) | Admin |
| `DELETE` | `/{reservationId}` | Cancel a reservation | Admin |

For detailed request/response examples for the Reservation API, see [POSTMAN_RESERVATION_API.md](./POSTMAN_RESERVATION_API.md).

---

### Fines (User)

Base URL: `/api/fines`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/{fineId}/pay` | Pay a fine (Razorpay) | Yes |
| `GET` | `/my` | Get current user's fines | Yes |

---

### Fines (Admin)

Base URL: `/api/admin/fines`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/` | Create a fine for a loan | Admin |
| `POST` | `/waive` | Waive a fine | Admin |
| `GET` | `/` | Get all fines | Admin |

---

### Subscriptions (User)

Base URL: `/api/subscriptions`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/subscribe` | Subscribe to a plan | Yes |
| `GET` | `/active` | Get the user's active subscription | Yes |
| `GET` | `/{id}` | Get a subscription by ID | Yes |
| `POST` | `/{id}/activate` | Activate a subscription | Yes |
| `POST` | `/{id}/cancel` | Cancel a subscription | Yes |

---

### Subscriptions (Admin)

Base URL: `/api/admin/subscriptions`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/` | List all subscriptions (paginated) | Admin |
| `GET` | `/{id}` | Get a subscription by ID | Admin |
| `POST` | `/{id}/activate` | Activate a subscription | Admin |
| `POST` | `/{id}/cancel` | Cancel a subscription | Admin |
| `POST` | `/deactivate-expired` | Batch-deactivate expired subscriptions | Admin |

---

### Subscription Plans (User)

Base URL: `/api/subscription-plans`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/` | List all subscription plans | Yes |
| `GET` | `/{id}` | Get a subscription plan by ID | Yes |

---

### Subscription Plans (Admin)

Base URL: `/api/admin/subscription-plans`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/` | List all subscription plans | Admin |
| `POST` | `/create` | Create a new subscription plan | Admin |
| `PUT` | `/update/{id}` | Update a subscription plan | Admin |
| `DELETE` | `/delete/{id}` | Delete a subscription plan | Admin |

---

### User Profile

Base URL: `/api/users`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/profile` | Get the current user's profile | Yes |

---

## Roles & Permissions

| Role | Description |
|------|-------------|
| `ROLE_USER` | Default role assigned at signup. Can browse books, borrow, reserve, pay fines, and manage their own subscriptions. |
| `ROLE_ADMIN` | Full access to all endpoints, including book/genre management, loan administration, fine management, and subscription plan management. |

All admin endpoints are protected under `/api/admin/**` and require `ROLE_ADMIN`.

---

## Running Tests

```bash
./mvnw test
```

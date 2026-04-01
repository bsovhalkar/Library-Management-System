# Library Management System

A RESTful backend API for managing a library built with **Spring Boot**, **Java 21**, and **MySQL**. It supports book lending, reservations, fines, subscriptions, and more — with role-based access for admins and users.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 4.0.3 |
| Language | Java 21 |
| Database | MySQL |
| ORM | Spring Data JPA (Hibernate) |
| Security | Spring Security + JWT |
| Payment | Razorpay |
| Email | Spring Boot Mail |
| Build | Maven |
| Utilities | Lombok |

---

## Features

- **Authentication** — JWT-based login/signup, password reset via email, Google OAuth support
- **Book Management** — CRUD, bulk creation, soft/hard delete, advanced search & filtering
- **Hierarchical Genres** — Parent/child genre structure
- **Book Lending** — Checkout, check-in, renewal (with limit), overdue tracking
- **Reservation System** — Queue-based reservations with automatic status transitions
- **Fines** — Automatic overdue fines, manual admin fines, Razorpay payment integration, waiving
- **Subscriptions** — Plan-based lending limits (max books, days), activate/cancel/auto-renew
- **Wishlist** — Users can save books to a personal wishlist
- **Admin Panel** — Full control over books, users, fines, reservations, subscriptions

---

## Project Structure

```
src/main/java/com/app/Library_Management/
├── configuration/       # Security, JWT setup
├── controller/
│   ├── admin/           # Admin endpoints (/api/admin/**)
│   └── user/            # User endpoints (/api/**)
├── domain/              # Enums (status, types)
├── exception/           # Custom exceptions
├── mapper/              # DTO mappers
├── model/               # JPA entities
├── payload/
│   ├── dto/             # Data Transfer Objects
│   ├── request/         # Request bodies
│   └── response/        # Response bodies
├── repository/          # Spring Data JPA repositories
└── service/             # Business logic (interfaces + implementations)
```

---

## API Overview

### Authentication (`/api/auth`)
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | `/login` | Public |
| POST | `/signup` | Public |
| POST | `/forgot-password` | Public |
| POST | `/reset-password` | Public |

### Books (`/api/book`)
| Method | Endpoint | Access |
|--------|----------|--------|
| GET | `/` | User |
| GET | `/{id}` | User |
| GET | `/search` | User |
| POST | `/advanced-search` | User |
| POST | `/create` | Admin |
| PUT | `/{id}` | Admin |
| DELETE | `/{id}` | Admin |

### Book Loans (`/api/book-loan`)
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | `/checkout` | User |
| POST | `/checkin` | User |
| POST | `/renew` | User |
| GET | `/my-loans` | User |

### Reservations (`/api/reservations`)
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | `/` | User |
| GET | `/my` | User |
| DELETE | `/{id}` | User |
| PUT | `/{id}/fulfill` | Admin |

### Fines (`/api/fines`)
| Method | Endpoint | Access |
|--------|----------|--------|
| GET | `/my` | User |
| POST | `/{fineId}/pay` | User |
| POST | `/waive` | Admin |

### Subscriptions (`/api/subscriptions`)
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | `/subscribe` | User |
| GET | `/active` | User |
| POST | `/{id}/cancel` | User |
| POST | `/deactivate-expired` | Admin |

---

## Getting Started

### Prerequisites
- Java 21+
- MySQL 8+
- Maven 3.8+

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/bsovhalkar/Library-Management-System.git
   cd Library-Management-System
   ```

2. **Configure the database**

   Create a MySQL database and update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/library_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Configure JWT & Email**
   ```properties
   jwt.secret=your_jwt_secret_key
   spring.mail.host=smtp.gmail.com
   spring.mail.username=your_email@gmail.com
   spring.mail.password=your_app_password
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

   The API will be available at `http://localhost:8080`.

---

## Database Entities

| Entity | Description |
|--------|-------------|
| `User` | Library members and admins |
| `Book` | Books with inventory tracking |
| `Genre` | Hierarchical genre categories |
| `BookLoan` | Checkout/return/renewal records |
| `Fine` | Overdue and damage fines |
| `Reservation` | Book reservation queue |
| `Subscription` | User subscription records |
| `SubscriptionPlan` | Available lending plans |
| `Wishlist` | User book wishlists |
| `PasswordResetToken` | Tokens for password resets |

---

## Roles

| Role | Access |
|------|--------|
| `ROLE_USER` | Browse books, checkout, reserve, manage own account |
| `ROLE_ADMIN` | Full access including book/user/fine/plan management |

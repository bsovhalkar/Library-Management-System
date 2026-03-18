# AGENTS.md - AI Developer Guide

## Project Overview
Library Management System - A Spring Boot 4.0.3 REST API for managing books, genres, and user authentication using JWT.

**Tech Stack**: Java 21, Spring Boot 4.0.3, Spring Security, JWT (jjwt), JPA/Hibernate, MySQL, MapStruct

---

## Architecture & Core Components

### Authentication Flow
- **JWT Tokens**: Generated via `JwtProvider` containing email + authorities (roles)
- **Token Validation**: `JwtValidator` filter processes tokens from `Authorization` header (format: `Bearer <token>`)
- **Security Config**: Stateless session, role-based access control via `/api/admin/**` (ADMIN role) and `/api/**` (authenticated)
- **Key Files**: 
  - `JwtProvider.java` - Token generation & validation (singleton service)
  - `JwtValidator.java` - OncePerRequestFilter for every HTTP request
  - `JwtConstant.java` - Stores JWT header name & secret key
  - `SecurityConfig.java` - Configures filter chain, CORS, and authorization rules

### Database & Models
- **JPA Entities**: `Book`, `Genre`, `User` (inferred from repository classes)
- **Repositories**: `BookRepository`, `GenreRepository`, `UserRepository`
- **Mappers**: `BookMapper`, `GenreMapper` (MapStruct) - for DTO/Entity conversions

### REST Endpoints
```
GET/POST    /api/books           - Book operations (authenticated)
GET/POST    /api/genres          - Genre operations (authenticated)
            /api/admin/**        - Admin-only operations (requires ADMIN role)
GET         / (public)           - Unauthenticated access allowed
```

### CORS Configuration
Explicitly configured for:
- Origins: `http://localhost:5173` (frontend dev), `http://bs.com`
- Methods/Headers: All allowed
- Credentials: Enabled
- Max Age: 3600 seconds

---

## Critical Development Patterns

### Error Handling
- **Global Exception Handler**: `GlobalException.java` catches all exceptions
- **Custom Exceptions**: Domain-specific exceptions with clear naming:
  - `BookAlreadyExistException`, `BookNotFoundException`
  - `GenreNotFoundException`, `ParentNotFoundException`
  - `ParentAndChildCantBeSame` - Specific domain rule
  - `UserAlreadyExistException`
- **Convention**: Always throw custom exceptions from services, let GlobalException handler convert to HTTP responses

### JWT Token Details
- **Token Expiration**: 86400000 ms (1 day) - hardcoded in `JwtProvider.generateToken()`
- **Claims Stored**: `email` (username), `authorities` (comma-separated roles)
- **Secret Key**: 64-byte key stored in `JwtConstant.SECRET_KEY`
- **Validation**: Both signature validation (JwtValidator) and explicit validation method available (JwtProvider.validateToken)

### Mappers (MapStruct)
- Automatically generated implementations in `target/generated-sources/annotations/`
- Used for DTO ↔ Entity conversions in service layer
- Files: `src/main/java/com/app/Library_Management/mapper/`

---

## Common Tasks & Workflows

### Adding a New Entity & REST Endpoint
1. Create JPA Entity in `model/` package
2. Create `Repository extends JpaRepository<Entity, ID>` in `repository/`
3. Create `Mapper` interface in `mapper/` (MapStruct will generate impl)
4. Create `Service` class in `service/impl/`
5. Create `Controller` class with `@RestController @RequestMapping("/api/entity")`
6. Implement endpoints with `@GetMapping`, `@PostMapping`, etc.
7. Throw custom exceptions for domain errors
8. GlobalException will automatically convert to HTTP responses

### Authentication in Controllers
```java
@PostMapping("/login")
public String login(@RequestBody LoginRequest request) {
    // Authenticate user, get Authentication object
    Authentication auth = authenticationManager.authenticate(...);
    return jwtProvider.generateToken(auth);
}
```

### Token Extraction in Services
- Extract from `SecurityContextHolder.getContext().getAuthentication()` (already done by JwtValidator)
- Or use `jwtProvider.getEmailFromJwtToken(token)` if token string available
- Never make direct database calls with unvalidated tokens

### Exception Handling Pattern
```java
if (book == null) {
    throw new BookNotFoundException("Book with ID " + id + " not found");
}
```
GlobalException catches and returns HTTP 400/404 depending on exception type.

---

## Build & Deployment

### Build Command
```bash
mvn clean install
```

### Run Locally
```bash
mvn spring-boot:run
```
or
```bash
java -jar target/Library_Management-0.0.1-SNAPSHOT.jar
```

### Key Configuration Files
- `pom.xml` - Dependencies & plugins (parent: spring-boot-starter-parent 4.0.3)
- `src/main/resources/application.properties` - Database, server config

---

## Known Improvements Made
- ✅ Fixed class name: `JwrProvider` → `JwtProvider` (JWT = JSON Web Token)
- ✅ Added null/empty token validation in all methods
- ✅ Added proper exception handling (ExpiredJwtException, JwtException)
- ✅ Made secretKey field `final` (immutable singleton)
- ✅ Added authentication null check in `generateToken()`
- ✅ Added 4 new utility methods: `validateToken()`, `getClaimsFromToken()`
- ✅ Added comprehensive JavaDoc for all public methods

---

## Important Notes for AI Agents
- **Database Schema**: Inferred from entity/repository names; verify actual schema before migrations
- **User Role Storage**: Stored in JWT claims; ensure UserRole enum aligns with authority strings
- **Token Header Format**: Mandatory `"Bearer <token>"` prefix in Authorization header
- **Parent/Child Genre Relationships**: Custom validation rule (ParentAndChildCantBeSame) - check Genre entity for details
- **Security First**: Always validate authentication context; never trust Authorization header directly


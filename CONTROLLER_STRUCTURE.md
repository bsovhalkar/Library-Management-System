# Controller Folder Structure - Reorganization Summary

## New Folder Structure

```
src/main/java/com/app/Library_Management/controller/
├── admin/
│   ├── AdminBookController.java
│   ├── AdminGenreController.java
│   └── AdminSubscriptionPlanController.java
└── user/
    ├── AuthController.java
    ├── UserBookController.java
    ├── UserController.java
    ├── UserGenreController.java
    └── UserSubscriptionPlanController.java
```

## Controller Organization

### Admin Controllers (`/controller/admin`)
- **AdminBookController** - `/api/admin/book`
  - POST `/create` - Create single book
  - POST `/create/bulk` - Create bulk books
  - PUT `/{id}` - Update book
  - DELETE `/{id}` - Soft delete book
  - DELETE `/{id}/hard` - Hard delete book

- **AdminGenreController** - `/api/admin/genres`
  - POST `/create` - Create genre
  - PUT `/{genreId}` - Update genre
  - DELETE `/{genreId}` - Soft delete genre
  - DELETE `/{genreId}/hard` - Hard delete genre

- **AdminSubscriptionPlanController** - `/api/admin/subscription-plans`
  - GET - Get all plans
  - POST `/create` - Create subscription plan
  - PUT `/update/{id}` - Update subscription plan
  - DELETE `/delete/{id}` - Delete subscription plan

### User Controllers (`/controller/user`)
- **AuthController** - `/api/auth`
  - POST `/login` - User login
  - POST `/signup` - User registration
  - POST `/forgot-password` - Forgot password
  - POST `/reset-password` - Reset password
  - GET `/verify-token` - Verify JWT token
  - GET `/health` - Health check

- **UserBookController** - `/api/book`
  - GET `/` - Get all books
  - GET `/search` - Search books with filters
  - GET `/{id}` - Get book by ID
  - POST `/advanced-search` - Advanced book search
  - GET `/states` - Get book statistics

- **UserController** - `/api/users`
  - GET `/profile` - Get current user profile
  - GET `/list` - Get all users

- **UserGenreController** - `/api/genres`
  - GET `/` - Get all genres
  - GET `/{genreId}` - Get genre by ID
  - GET `/top-level` - Get top-level genres
  - GET `/count` - Get genre count
  - GET `/{genreId}/book-count` - Get books in genre

- **UserSubscriptionPlanController** - `/api/subscription-plans`
  - GET - Get all subscription plans
  - GET `/{id}` - Get plan by ID

## Package Names Updated
All files have been moved to their respective packages:
- Admin controllers: `com.app.Library_Management.controller.admin`
- User controllers: `com.app.Library_Management.controller.user`

## Benefits
✅ Clear separation of concerns (Admin vs User operations)
✅ Better code organization and maintainability
✅ Easier to apply different security rules per folder
✅ Scalable structure for future controllers


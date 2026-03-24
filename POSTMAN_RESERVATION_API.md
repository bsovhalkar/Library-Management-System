# Reservation API - Postman Testing Guide

## USER RESERVATION CONTROLLER
Base URL: `http://localhost:8080/api/reservations`

---

### 1. CREATE RESERVATION (For Current User)
**Method:** `POST`
**Endpoint:** `/api/reservations`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>
```

**Request Body:**
```json
{
  "bookId": 1,
  "notes": "Please reserve this book for me"
}
```

**Success Response (201):**
```json
{
  "id": 1,
  "userId": 5,
  "userName": "John Doe",
  "userEmail": "john@example.com",
  "bookId": 1,
  "bookTitle": "The Great Gatsby",
  "bookAuthor": "F. Scott Fitzgerald",
  "bookISBN": "978-0743273565",
  "bookCoverPageURL": "https://example.com/cover.jpg",
  "reservationStatus": "PENDING",
  "reservedAt": "2026-03-24T10:30:00",
  "availableAt": null,
  "availableUntil": null,
  "createdAt": "2026-03-24T10:30:00",
  "updatedAt": "2026-03-24T10:30:00",
  "fulfilledAt": null,
  "queuePosition": 1,
  "notificationSent": false,
  "notes": "Please reserve this book for me",
  "cancelledAt": null,
  "isExpired": false,
  "canBeCancelled": true,
  "hoursUntilExpiry": null
}
```

**Error Response (400):**
```json
{
  "message": "Book is currently available, no need to reserve",
  "success": false
}
```

---

### 2. GET MY RESERVATIONS
**Method:** `GET`
**Endpoint:** `/api/reservations/my`

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Query Parameters:**
```
?page=0&pageSize=20&sortBy=reservedAt&sortDirection=DESC
```

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | integer | 0 | Page number (0-indexed) |
| pageSize | integer | 20 | Items per page |
| sortBy | string | reservedAt | Field to sort by |
| sortDirection | string | DESC | ASC or DESC |

**Success Response (200):**
```json
{
  "content": [
    {
      "id": 1,
      "userId": 5,
      "userName": "John Doe",
      "userEmail": "john@example.com",
      "bookId": 1,
      "bookTitle": "The Great Gatsby",
      "bookAuthor": "F. Scott Fitzgerald",
      "bookISBN": "978-0743273565",
      "bookCoverPageURL": "https://example.com/cover.jpg",
      "reservationStatus": "PENDING",
      "reservedAt": "2026-03-24T10:30:00",
      "createdAt": "2026-03-24T10:30:00",
      "updatedAt": "2026-03-24T10:30:00",
      "queuePosition": 1,
      "notificationSent": false,
      "notes": "Please reserve this book",
      "isExpired": false,
      "canBeCancelled": true,
      "hoursUntilExpiry": 168
    }
  ],
  "pageNumber": 0,
  "pageSize": 20,
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "first": true,
  "empty": false
}
```

---

### 3. GET RESERVATION BY ID
**Method:** `GET`
**Endpoint:** `/api/reservations/{reservationId}`

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Path Parameter:**
```
{reservationId} = 1
```

**Success Response (200):**
```json
{
  "id": 1,
  "userId": 5,
  "userName": "John Doe",
  "userEmail": "john@example.com",
  "bookId": 1,
  "bookTitle": "The Great Gatsby",
  "bookAuthor": "F. Scott Fitzgerald",
  "bookISBN": "978-0743273565",
  "bookCoverPageURL": "https://example.com/cover.jpg",
  "reservationStatus": "PENDING",
  "reservedAt": "2026-03-24T10:30:00",
  "createdAt": "2026-03-24T10:30:00",
  "updatedAt": "2026-03-24T10:30:00",
  "queuePosition": 1,
  "notificationSent": false,
  "isExpired": false,
  "canBeCancelled": true
}
```

---

### 4. CANCEL RESERVATION
**Method:** `DELETE`
**Endpoint:** `/api/reservations/{reservationId}`

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Path Parameter:**
```
{reservationId} = 1
```

**Success Response (200):**
```json
{
  "message": "Reservation cancelled successfully",
  "success": true
}
```

**Error Response (400):**
```json
{
  "message": "This reservation cannot be cancelled",
  "success": false
}
```

---

## ADMIN RESERVATION CONTROLLER
Base URL: `http://localhost:8080/api/admin/reservations`

---

### 1. CREATE RESERVATION FOR USER (Admin Only)
**Method:** `POST`
**Endpoint:** `/api/admin/reservations`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Query Parameters:**
```
?userId=5
```

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| userId | Long | Yes | Target user ID |

**Request Body:**
```json
{
  "bookId": 1,
  "notes": "Admin reserved this book for user"
}
```

**Success Response (201):**
```json
{
  "id": 2,
  "userId": 5,
  "userName": "John Doe",
  "userEmail": "john@example.com",
  "bookId": 1,
  "bookTitle": "The Great Gatsby",
  "bookAuthor": "F. Scott Fitzgerald",
  "bookISBN": "978-0743273565",
  "bookCoverPageURL": "https://example.com/cover.jpg",
  "reservationStatus": "PENDING",
  "reservedAt": "2026-03-24T10:45:00",
  "createdAt": "2026-03-24T10:45:00",
  "queuePosition": 2,
  "notificationSent": false,
  "notes": "Admin reserved this book for user",
  "isExpired": false,
  "canBeCancelled": true
}
```

---

### 2. SEARCH RESERVATIONS (With Filters)
**Method:** `GET`
**Endpoint:** `/api/admin/reservations`

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Query Parameters (All Optional):**
```
?userId=5&bookId=1&activeOnly=true&page=0&pageSize=20&sortBy=reservedAt&sortDirection=DESC
```

| Parameter | Type | Description |
|-----------|------|-------------|
| userId | Long | Filter by user ID |
| bookId | Long | Filter by book ID |
| activeOnly | Boolean | Show only PENDING/AVAILABLE (true/false) |
| page | integer | Page number |
| pageSize | integer | Items per page |
| sortBy | string | Field to sort by |
| sortDirection | string | ASC or DESC |

**Example Requests:**

**Filter by User:**
```
GET /api/admin/reservations?userId=5&page=0&pageSize=20
```

**Filter by Book:**
```
GET /api/admin/reservations?bookId=1&activeOnly=true
```

**Filter Active Only:**
```
GET /api/admin/reservations?activeOnly=true&sortBy=queuePosition&sortDirection=ASC
```

**Success Response (200):**
```json
{
  "content": [
    {
      "id": 1,
      "userId": 5,
      "userName": "John Doe",
      "userEmail": "john@example.com",
      "bookId": 1,
      "bookTitle": "The Great Gatsby",
      "bookAuthor": "F. Scott Fitzgerald",
      "bookISBN": "978-0743273565",
      "reservationStatus": "PENDING",
      "reservedAt": "2026-03-24T10:30:00",
      "queuePosition": 1,
      "notificationSent": false,
      "isExpired": false,
      "canBeCancelled": true,
      "hoursUntilExpiry": 168
    },
    {
      "id": 2,
      "userId": 6,
      "userName": "Jane Smith",
      "userEmail": "jane@example.com",
      "bookId": 1,
      "bookTitle": "The Great Gatsby",
      "bookAuthor": "F. Scott Fitzgerald",
      "bookISBN": "978-0743273565",
      "reservationStatus": "PENDING",
      "reservedAt": "2026-03-24T10:45:00",
      "queuePosition": 2,
      "notificationSent": false,
      "isExpired": false,
      "canBeCancelled": true,
      "hoursUntilExpiry": 170
    }
  ],
  "pageNumber": 0,
  "pageSize": 20,
  "totalElements": 2,
  "totalPages": 1,
  "last": true,
  "first": true,
  "empty": false
}
```

---

### 3. FULFILL RESERVATION (Auto-Checkout)
**Method:** `PUT`
**Endpoint:** `/api/admin/reservations/{reservationId}/fulfill`

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Path Parameter:**
```
{reservationId} = 1
```

**Success Response (200):**
```json
{
  "id": 1,
  "userId": 5,
  "userName": "John Doe",
  "userEmail": "john@example.com",
  "bookId": 1,
  "bookTitle": "The Great Gatsby",
  "bookAuthor": "F. Scott Fitzgerald",
  "bookISBN": "978-0743273565",
  "reservationStatus": "FULFILLED",
  "reservedAt": "2026-03-24T10:30:00",
  "fulfilledAt": "2026-03-24T11:00:00",
  "createdAt": "2026-03-24T10:30:00",
  "updatedAt": "2026-03-24T11:00:00",
  "queuePosition": 1,
  "notificationSent": true,
  "isExpired": false,
  "canBeCancelled": false
}
```

**Error Response (400):**
```json
{
  "message": "Reservation can't to be fulfilled (Book available count has been empty)",
  "success": false
}
```

---

### 4. CANCEL RESERVATION (Admin)
**Method:** `DELETE`
**Endpoint:** `/api/admin/reservations/{reservationId}`

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Path Parameter:**
```
{reservationId} = 1
```

**Success Response (200):**
```json
{
  "message": "Reservation cancelled successfully",
  "success": true
}
```

---

### 5. GET RESERVATION BY ID (Admin)
**Method:** `GET`
**Endpoint:** `/api/admin/reservations/{reservationId}`

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Path Parameter:**
```
{reservationId} = 1
```

**Success Response (200):**
```json
{
  "id": 1,
  "userId": 5,
  "userName": "John Doe",
  "userEmail": "john@example.com",
  "bookId": 1,
  "bookTitle": "The Great Gatsby",
  "bookAuthor": "F. Scott Fitzgerald",
  "bookISBN": "978-0743273565",
  "reservationStatus": "PENDING",
  "reservedAt": "2026-03-24T10:30:00",
  "createdAt": "2026-03-24T10:30:00",
  "queuePosition": 1,
  "isExpired": false,
  "canBeCancelled": true
}
```

---

## POSTMAN COLLECTION IMPORT (JSON)

You can import this into Postman:

```json
{
  "info": {
    "name": "Reservation API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "User Reservations",
      "item": [
        {
          "name": "Create Reservation",
          "request": {
            "method": "POST",
            "header": [
              {"key": "Content-Type", "value": "application/json"},
              {"key": "Authorization", "value": "Bearer {{JWT_TOKEN}}"}
            ],
            "url": {"raw": "{{BASE_URL}}/api/reservations", "host": ["{{BASE_URL}}"], "path": ["api", "reservations"]},
            "body": {
              "mode": "raw",
              "raw": "{\"bookId\": 1, \"notes\": \"Please reserve this book\"}"
            }
          }
        },
        {
          "name": "Get My Reservations",
          "request": {
            "method": "GET",
            "header": [{"key": "Authorization", "value": "Bearer {{JWT_TOKEN}}"}],
            "url": {"raw": "{{BASE_URL}}/api/reservations/my?page=0&pageSize=20&sortBy=reservedAt&sortDirection=DESC", "host": ["{{BASE_URL}}"], "path": ["api", "reservations", "my"], "query": [{"key": "page", "value": "0"}, {"key": "pageSize", "value": "20"}, {"key": "sortBy", "value": "reservedAt"}, {"key": "sortDirection", "value": "DESC"}]}
          }
        },
        {
          "name": "Get Reservation By ID",
          "request": {
            "method": "GET",
            "header": [{"key": "Authorization", "value": "Bearer {{JWT_TOKEN}}"}],
            "url": {"raw": "{{BASE_URL}}/api/reservations/1", "host": ["{{BASE_URL}}"], "path": ["api", "reservations", "1"]}
          }
        },
        {
          "name": "Cancel Reservation",
          "request": {
            "method": "DELETE",
            "header": [{"key": "Authorization", "value": "Bearer {{JWT_TOKEN}}"}],
            "url": {"raw": "{{BASE_URL}}/api/reservations/1", "host": ["{{BASE_URL}}"], "path": ["api", "reservations", "1"]}
          }
        }
      ]
    },
    {
      "name": "Admin Reservations",
      "item": [
        {
          "name": "Create Reservation for User",
          "request": {
            "method": "POST",
            "header": [
              {"key": "Content-Type", "value": "application/json"},
              {"key": "Authorization", "value": "Bearer {{ADMIN_JWT_TOKEN}}"}
            ],
            "url": {"raw": "{{BASE_URL}}/api/admin/reservations?userId=5", "host": ["{{BASE_URL}}"], "path": ["api", "admin", "reservations"], "query": [{"key": "userId", "value": "5"}]},
            "body": {
              "mode": "raw",
              "raw": "{\"bookId\": 1, \"notes\": \"Admin reserved for user\"}"
            }
          }
        },
        {
          "name": "Search Reservations",
          "request": {
            "method": "GET",
            "header": [{"key": "Authorization", "value": "Bearer {{ADMIN_JWT_TOKEN}}"}],
            "url": {"raw": "{{BASE_URL}}/api/admin/reservations?userId=5&activeOnly=true&page=0&pageSize=20", "host": ["{{BASE_URL}}"], "path": ["api", "admin", "reservations"], "query": [{"key": "userId", "value": "5"}, {"key": "activeOnly", "value": "true"}, {"key": "page", "value": "0"}, {"key": "pageSize", "value": "20"}]}
          }
        },
        {
          "name": "Fulfill Reservation",
          "request": {
            "method": "PUT",
            "header": [{"key": "Authorization", "value": "Bearer {{ADMIN_JWT_TOKEN}}"}],
            "url": {"raw": "{{BASE_URL}}/api/admin/reservations/1/fulfill", "host": ["{{BASE_URL}}"], "path": ["api", "admin", "reservations", "1", "fulfill"]}
          }
        },
        {
          "name": "Cancel Reservation",
          "request": {
            "method": "DELETE",
            "header": [{"key": "Authorization", "value": "Bearer {{ADMIN_JWT_TOKEN}}"}],
            "url": {"raw": "{{BASE_URL}}/api/admin/reservations/1", "host": ["{{BASE_URL}}"], "path": ["api", "admin", "reservations", "1"]}
          }
        }
      ]
    }
  ]
}
```

---

## POSTMAN ENVIRONMENT VARIABLES

Set these in Postman Environment:

```
BASE_URL = http://localhost:8080
JWT_TOKEN = <your_user_jwt_token>
ADMIN_JWT_TOKEN = <your_admin_jwt_token>
```

---

## ERROR RESPONSES

### 404 - Not Found
```json
{
  "message": "Reservation not found",
  "success": false
}
```

### 400 - Bad Request
```json
{
  "message": "Book has already been checked out",
  "success": false
}
```

### 403 - Forbidden
```json
{
  "message": "You can't cancel your reservation",
  "success": false
}
```

### 401 - Unauthorized
```json
{
  "message": "Unauthorized access",
  "success": false
}
```

---

## TESTING WORKFLOW

1. **User Flow:**
   - Login to get JWT_TOKEN
   - Create a reservation for a book
   - Get my reservations
   - View specific reservation
   - Cancel reservation

2. **Admin Flow:**
   - Login with admin account to get ADMIN_JWT_TOKEN
   - Create reservation for a user
   - Search reservations with filters
   - Fulfill a reservation (auto-checkout)
   - Cancel reservation
   - View reservation details


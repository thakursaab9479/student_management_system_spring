# Student Management System

A full-stack student management system with role-based access for **Admins**, **Teachers**, and **Students**. Backend built with Spring Boot and secured with JWT authentication; frontend built with React and Material UI.

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Reference](#api-reference)
- [Database Schema](#database-schema)
- [Roles & Permissions](#roles--permissions)
- [Project Status](#project-status)
- [Known Limitations](#known-limitations)

## Overview

This system lets an institution manage students, teachers, and courses, with support for enrollment, attendance tracking, and grading — all gated behind role-based authentication so each user type only sees and does what they should. Both backend and frontend are fully built and tested end to end.

The backend and frontend live in two separate repositories:
- Backend: `student_management_system_spring` — https://github.com/thakursaab9479/student_management_system_spring
- Frontend: `student_management_system_react` — https://github.com/thakursaab9479/student-management-system-react

They run and deploy independently, communicating over a REST API with CORS configured between them.

## Tech Stack

**Backend**
- Java 17+, Spring Boot 3
- Spring Web, Spring Data JPA, Spring Security
- MySQL 8
- JWT (io.jsonwebtoken) for stateless authentication
- Maven

**Frontend**
- React (Vite), JavaScript
- Material UI (@mui/material, @mui/icons-material)
- React Router (react-router-dom)
- Axios, with a centralized instance handling auth headers and session expiry

## Features

- JWT-based authentication with role-based authorization (ADMIN, TEACHER, STUDENT)
- Full CRUD for students, teachers, and courses (create, view, edit, delete)
- Student-course enrollment, attendance tracking, and grade recording (create, view, delete)
- Server-side pagination on every list view — the backend returns one page of results at a time, not the entire table
- Client-side form validation (name, email, phone, course code, date-order, and numeric-range checks) via a shared validators module
- Role-aware UI — write controls (forms, edit/delete buttons) and certain navigation links are hidden entirely for roles that can't use them, matching the backend's own @PreAuthorize rules
- Session-expiry handling — an expired or invalid token automatically signs the user out and redirects to login with a clear message
- Password hashing with BCrypt
- CORS configured for a separate frontend origin

## Project Structure

**Backend**

src/main/java/com/vrsingh/sms/student_management_system_spring/
├── entity/        # JPA entities: Student, Teacher, Course, User, Enrollment, Attendance, Grade
├── repository/    # Spring Data JPA repositories
├── service/       # Business logic, including pagination
├── controller/    # REST endpoints
├── config/        # SecurityConfig, JwtFilter
├── util/          # JwtUtil (token generation/validation)
└── dto/           # LoginRequest and other request/response shapes

**Frontend**

src/
├── api/           # axios.js — centralized instance: attaches JWT, handles 401/session expiry
├── component/     # Navbar, Layout (shared shell), ProtectedRoute (route guard)
├── pages/         # Login, Dashboard, Students, Teachers, Courses, Enrollments, Attendance, Grades
└── utils/         # validators.js — shared regex patterns (name, email, phone)

## Getting Started

### Prerequisites
- JDK 17 or higher
- MySQL 8+
- Node.js 18+ and npm
- Maven (bundled with IntelliJ)

### Backend Setup

1. Clone the backend repository
2. Create the database in MySQL:

CREATE DATABASE student_management;

3. Configure src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/student_management
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE
spring.jpa.hibernate.ddl-auto=update
jwt.secret=YOUR_SECRET_KEY_HERE
jwt.expiration=86400000

4. Run via IntelliJ (green run button on StudentManagementSystemSpringApplication) or:

mvn spring-boot:run

5. API available at http://localhost:8080

### Frontend Setup

1. Clone the frontend repository
2. Install dependencies:

npm install

3. Confirm the backend's CORS config (SecurityConfig.java) allows your frontend's origin — defaults to http://localhost:5173
4. Run the dev server:

npm run dev

5. Open http://localhost:5173, log in with a user created via /api/auth/register

## API Reference

Base URL: http://localhost:8080

### Auth (public)

| Method | Endpoint | Description |
|---|---|---|
| POST | /api/auth/register | Register a new user (username, password, email, role) |
| POST | /api/auth/login | Log in, returns a JWT + role + username |

All endpoints below require a Bearer <token> in the Authorization header.

### Pagination

Every list (GET-all) endpoint accepts:

| Param | Default | Description |
|---|---|---|
| page | 0 | Zero-indexed page number |
| size | 10 | Rows per page |

And returns a Spring Data Page object, not a plain array:

{
  "content": [ /* the actual rows */ ],
  "totalElements": 42,
  "totalPages": 5,
  "number": 0,
  "size": 10
}

### Students

| Method | Endpoint | Required Role |
|---|---|---|
| GET | /api/students?page=0&size=10 | ADMIN, TEACHER |
| GET | /api/students/{id} | ADMIN, TEACHER |
| POST | /api/students | ADMIN |
| PUT | /api/students/{id} | ADMIN |
| DELETE | /api/students/{id} | ADMIN |

### Teachers

Same pattern as Students above.

### Courses

| Method | Endpoint | Required Role |
|---|---|---|
| GET | /api/courses?page=0&size=10 | ADMIN, TEACHER, STUDENT |
| GET | /api/courses/{id} | ADMIN, TEACHER, STUDENT |
| POST | /api/courses | ADMIN |
| PUT | /api/courses/{id} | ADMIN |
| DELETE | /api/courses/{id} | ADMIN |

### Enrollments

| Method | Endpoint | Required Role |
|---|---|---|
| GET | /api/enrollments?page=0&size=10 | ADMIN, TEACHER |
| POST | /api/enrollments?studentId={id}&courseId={id}&enrollmentDate={date} | ADMIN |
| DELETE | /api/enrollments/{id} | ADMIN |

### Attendance

| Method | Endpoint | Required Role |
|---|---|---|
| GET | /api/attendance?page=0&size=10 | ADMIN, TEACHER, STUDENT |
| POST | /api/attendance?studentId={id}&courseId={id}&classDate={date}&status={status} | ADMIN, TEACHER |
| DELETE | /api/attendance/{id} | ADMIN, TEACHER |

### Grades

| Method | Endpoint | Required Role |
|---|---|---|
| GET | /api/grades?page=0&size=10 | ADMIN, TEACHER, STUDENT |
| POST | /api/grades?studentId={id}&courseId={id}&examType={type}&marks={marks} | ADMIN, TEACHER |
| DELETE | /api/grades/{id} | ADMIN, TEACHER |

## Database Schema

7 tables, all linked via @ManyToOne relationships (unidirectional):

| Table | Key Fields |
|---|---|
| student | firstName, middleName, lastName, email, phoneNumber, dateOfBirth, enrollmentDate |
| teacher | firstName, lastName, email, phoneNumber, subject, joiningDate |
| course | name, code, credits, teacher_id (FK) |
| user | username, password (hashed), email, role |
| enrollment | student_id (FK), course_id (FK), enrollmentDate |
| attendance | student_id (FK), course_id (FK), classDate, status |
| grade | student_id (FK), course_id (FK), examType, marks |

## Roles & Permissions

Enforced by the backend (@PreAuthorize) and mirrored in the frontend UI (forms/buttons/nav links hidden, not just disabled, for roles that can't use them):

| Role | Access |
|---|---|
| ADMIN | Full access — create, edit, delete students, teachers, courses, enrollments; view all data |
| TEACHER | View students and teachers; view courses; create/delete attendance and grades |
| STUDENT | View-only — courses, attendance, and grades (Students/Teachers/Enrollments are hidden from their nav entirely) |

## Project Status

- Backend — complete: all entities, repositories, services, and controllers built and tested end to end; JWT authentication, role-based access control, and server-side pagination all verified
- Frontend — complete: all 6 data modules (Students, Teachers, Courses, Enrollments, Attendance, Grades) with real CRUD against the live API; shared layout/navigation, logout, protected routes, role-based UI, and session-expiry handling all built and tested

## Known Limitations

A few known gaps, not currently blocking anything but worth fixing before production use:

- Form validation (name/email/phone/course-code patterns, date-order, numeric ranges) exists only in the frontend — the API itself doesn't enforce any of it, so a direct request (e.g. via Postman) can still insert invalid data
- Register/login responses currently include the hashed password field — should be excluded from API responses
- Error responses are raw stack traces rather than clean JSON error messages
- User isn't linked to Teacher or Student yet, so there's no way to restrict a teacher to only their own assigned course, or a student to only their own records
- List endpoints return all records (paginated, but unfiltered) to any permitted role, rather than scoping results to "my own data" for teachers/students
- jwt.secret is stored in plaintext in application.properties — should move to an environment variable for any real deployment
- Deleting the last row on the last page of a paginated table doesn't automatically step back a page, leaving an empty page briefly

# Student Management System

A full-stack student management system with role-based access for **Admins**, **Teachers**, and **Students**. Backend built with Spring Boot and secured with JWT authentication; frontend built with React.

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

This system lets an institution manage students, teachers, and courses, with support for enrollment, attendance tracking, and grading — all gated behind role-based authentication so each user type only sees and does what they should.

## Tech Stack

**Backend**
- Java 17+, Spring Boot 3
- Spring Web, Spring Data JPA, Spring Security
- MySQL 8
- JWT (`io.jsonwebtoken`) for stateless authentication
- Maven
  **Frontend**
- React (Vite)
- JavaScript
- Material UI
- Axios
- React Router
- *(in progress)*
## Features

- JWT-based authentication with role-based authorization (`ADMIN`, `TEACHER`, `STUDENT`)
- Full CRUD for students, teachers, and courses
- Student-course enrollment
- Attendance tracking per student, per course
- Grade recording per student, per course, per exam type
- Password hashing with BCrypt
- CORS configured for a separate frontend origin
## Project Structure

```
src/main/java/com/vrsingh/sms/student_management_system_spring/
├── entity/        # JPA entities: Student, Teacher, Course, User, Enrollment, Attendance, Grade
├── repository/    # Spring Data JPA repositories
├── service/       # Business logic
├── controller/    # REST endpoints
├── config/        # SecurityConfig, JwtFilter
├── util/          # JwtUtil (token generation/validation)
└── dto/           # LoginRequest and other request/response shapes
```

## Getting Started

### Prerequisites
- JDK 17 or higher
- MySQL 8+
- Maven (bundled with IntelliJ)
- Node.js 18+ and npm *(for the frontend)*
### Backend Setup

1. Clone the repository
2. Create the database in MySQL:
```sql
   CREATE DATABASE student_management;
```
3. Configure `src/main/resources/application.properties`:
```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/student_management
   spring.datasource.username=root
   spring.datasource.password=YOUR_PASSWORD_HERE
   spring.jpa.hibernate.ddl-auto=update
   jwt.secret=YOUR_SECRET_KEY_HERE
   jwt.expiration=86400000
```
4. Run the application via IntelliJ (green run button on `StudentManagementSystemSpringApplication`) or:
```
   mvn spring-boot:run
```
5. API available at `http://localhost:8080`
### Frontend Setup

*Coming soon — React project in progress.*

## API Reference

Base URL: `http://localhost:8080`

### Auth (public)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user (username, password, email, role) |
| POST | `/api/auth/login` | Log in, returns a JWT + role + username |

All endpoints below require a `Bearer <token>` in the `Authorization` header.

### Students

| Method | Endpoint | Required Role |
|---|---|---|
| GET | `/api/students` | ADMIN, TEACHER |
| GET | `/api/students/{id}` | ADMIN, TEACHER |
| POST | `/api/students` | ADMIN |
| PUT | `/api/students/{id}` | ADMIN |
| DELETE | `/api/students/{id}` | ADMIN |

### Teachers

Same pattern as Students above.

### Courses

| Method | Endpoint | Required Role |
|---|---|---|
| GET | `/api/courses` | ADMIN, TEACHER, STUDENT |
| GET | `/api/courses/{id}` | ADMIN, TEACHER, STUDENT |
| POST | `/api/courses` | ADMIN |
| PUT | `/api/courses/{id}` | ADMIN |
| DELETE | `/api/courses/{id}` | ADMIN |

### Enrollments

| Method | Endpoint | Required Role |
|---|---|---|
| GET | `/api/enrollments` | ADMIN, TEACHER |
| POST | `/api/enrollments?studentId={id}&courseId={id}&enrollmentDate={date}` | ADMIN |
| DELETE | `/api/enrollments/{id}` | ADMIN |

### Attendance

| Method | Endpoint | Required Role |
|---|---|---|
| GET | `/api/attendance` | ADMIN, TEACHER, STUDENT |
| POST | `/api/attendance?studentId={id}&courseId={id}&classDate={date}&status={status}` | ADMIN, TEACHER |
| DELETE | `/api/attendance/{id}` | ADMIN, TEACHER |

### Grades

| Method | Endpoint | Required Role |
|---|---|---|
| GET | `/api/grades` | ADMIN, TEACHER, STUDENT |
| POST | `/api/grades?studentId={id}&courseId={id}&examType={type}&marks={marks}` | ADMIN, TEACHER |
| DELETE | `/api/grades/{id}` | ADMIN, TEACHER |

## Database Schema

7 tables, all linked via `@ManyToOne` relationships (unidirectional):

| Table | Key Fields |
|---|---|
| `student` | firstName, lastName, email, phoneNumber, dateOfBirth, enrollmentDate |
| `teacher` | firstName, lastName, email, phoneNumber, subject, joiningDate |
| `course` | name, code, credits, teacher_id (FK) |
| `user` | username, password (hashed), email, role |
| `enrollment` | student_id (FK), course_id (FK), enrollmentDate |
| `attendance` | student_id (FK), course_id (FK), classDate, status |
| `grade` | student_id (FK), course_id (FK), examType, marks |

## Roles & Permissions

| Role | Access |
|---|---|
| **ADMIN** | Full access — create, edit, delete students, teachers, courses, enrollments; view all data |
| **TEACHER** | View students and teachers; view courses; create/delete attendance and grades |
| **STUDENT** | View-only — courses, attendance, and grades |

## Project Status

- ✅ **Backend** — complete: all entities, repositories, services, and controllers built and tested end to end; JWT authentication and role-based access control verified via Postman
- 🔨 **Frontend** — in progress: React + Vite + Material UI
## Known Limitations

A few known gaps, not currently blocking anything but worth fixing before production use:

- Register/login responses currently include the hashed password field — should be excluded from API responses
- Error responses are raw stack traces rather than clean JSON error messages
- `User` isn't linked to `Teacher` or `Student` yet, so there's no way to restrict a teacher to only their own assigned course, or a student to only their own records
- `GET` requests on attendance and grades currently return **all** records to any permitted role, rather than filtering to "my own data" for teachers/students
- `jwt.secret` is stored in plaintext in `application.properties` — should move to an environment variable for any real deployment
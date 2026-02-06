# Sweet – Full‑Stack Food Ordering & Delivery Platform

> **A production‑grade, scalable full‑stack application built with modern backend and frontend technologies, designed with clean architecture, security, and real‑world business workflows in mind.**

---

## 🚀 Overview

**Sweet** is a complete **end‑to‑end food ordering and delivery platform** consisting of:

* A **Spring Boot backend** providing secure REST APIs
* A **React frontend** for admin/user interfaces
* An **Android application** for end users

The project demonstrates **real‑world engineering practices** such as JWT authentication, role‑based access control, modular architecture, session handling, API versioning, and scalable data modeling.

This is not a demo project — it is structured like a **startup‑ready product**.

---

## 🧠 Key Highlights (Why Recruiters Care)

* ✅ Clean **layered architecture** (Controller → Service → Repository)
* ✅ Secure **JWT‑based authentication & authorization**
* ✅ Real‑world entities: Users, Categories, Products, Orders, Banners
* ✅ Modular & scalable backend design
* ✅ API consumed by **both React and Android clients**
* ✅ Industry‑standard coding conventions
* ✅ Separation of concerns across all layers

---

## 🏗️ System Architecture

```
Client Layer
 ├── React Web App
 └── Android App
        ↓
REST APIs (Spring Boot)
        ↓
Service Layer (Business Logic)
        ↓
Repository Layer (JPA/Hibernate)
        ↓
Database (MySQL / PostgreSQL)
```

---

## 🔐 Authentication & Security

### JWT Authentication Flow

1. User logs in with credentials
2. Backend validates credentials
3. JWT token is generated
4. Token is sent to client (HTTP‑only cookie / header)
5. Every secured request passes through **JWT Filter**
6. Role & token validation happens before controller execution

### Security Features

* JWT Token Validation Filter
* Stateless authentication
* Protected routes using Spring Security
* Role‑based access control (Admin / User)
* Secure password storage

---

## 🧩 Backend – Spring Boot

### Tech Stack

* Java 17+
* Spring Boot
* Spring Security
* Spring Data JPA (Hibernate)
* JWT Authentication
* Maven

---

### 📁 Backend Package Structure

```
com.example.sweet
 ├── config          → Security & filter configurations
 ├── controller      → REST API endpoints
 ├── service         → Business logic
 ├── repository      → Database access layer
 ├── model/entity    → Database entities
 ├── dto             → API response/request objects
 ├── util            → JWT & helper utilities
 └── exception       → Global exception handling
```

---

### 🔹 Controllers

Each controller is responsible only for **request handling**, not business logic.

Examples:

* `AuthController` – Login, registration, authentication
* `CategoryController` – Fetch food categories
* `BannerController` – Hero/banner images for home screen
* `ProfileController` – User profile data

Controllers:

* Validate input
* Call service layer
* Return standardized API responses

---

### 🔹 Service Layer

This is where **actual business logic lives**.

Responsibilities:

* Data validation
* Authorization checks
* Mapping entities to DTOs
* Transaction management

This separation ensures:

* High testability
* Clean maintainability
* Easy future scaling

---

### 🔹 Repository Layer

* Uses Spring Data JPA
* Abstracts all database operations
* No business logic

Benefits:

* Database‑agnostic
* Cleaner code
* Easier migrations

---

### 🔹 Entities & Data Model

Key Entities:

* **User** – Authentication, profile, roles
* **Category** – Food categories
* **Product** – Food items
* **Banner** – Hero banners
* **Order** – Order management

Entities are:

* Properly normalized
* Audited with timestamps
* Designed for scalability

---

## 🌐 Frontend – React Web App

### Tech Stack

* React
* JavaScript (ES6+)
* Axios
* REST API integration

### Responsibilities

* Admin dashboard
* Category & product management
* Banner management
* Authentication handling

The frontend consumes the **same backend APIs** used by the Android app, ensuring consistency and reuse.

---

## 📱 Android Application

### Tech Stack

* Java
* Android SDK
* RecyclerView
* ViewPager2
* REST API integration

### Features

* User authentication
* Home screen with banners
* Category listing
* Profile management
* Session handling

The Android app demonstrates:

* Clean UI separation
* Proper API consumption
* Real‑world mobile architecture

---

## 📡 API Design Principles

* RESTful conventions
* Proper HTTP status codes
* Consistent response structure
* DTO‑based responses

Example API response:

```json
{
  "success": true,
  "message": "Data fetched successfully",
  "data": { }
}
```

---

## 🧪 Error Handling

* Centralized global exception handling
* Custom error messages
* Clean API error responses

This ensures:

* Better debugging
* Cleaner client‑side handling

---

## 📈 Scalability & Production Readiness

* Stateless backend (JWT)
* Easy horizontal scaling
* Clean separation of concerns
* Can be deployed on:

  * AWS
  * Docker
  * Kubernetes

---

## 🧠 What This Project Demonstrates

✔ Strong Java & Spring Boot fundamentals
✔ Real‑world authentication flows
✔ Clean backend architecture
✔ Full‑stack integration
✔ Production‑grade thinking
✔ Ability to design, build, and scale systems

---

## 👨‍💻 Author

**[Your Name]**
Java Backend / Full‑Stack Developer

> This project reflects my ability to build **secure, scalable, and maintainable applications** that follow industry best practices.

---

## ⭐ Final Note for Recruiters

This project was built with the mindset of **real product development**, not tutorials. Every design decision prioritizes **clarity, security, and scalability** — exactly what production systems demand.

---

⭐ *If you’re reviewing this repository, you’re looking at a developer who understands more than just code — they understand systems.*

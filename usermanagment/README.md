# 👤 User Management Service

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-success)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)
![Status](https://img.shields.io/badge/Status-Completed-success)

The User Management Service is responsible for user registration, authentication, authorization, and profile management in the Flight Booking System.

---

# ✨ Features

## Authentication

- User Registration
- User Login
- JWT Authentication
- Password Encryption (BCrypt)
- Stateless Authentication

## Authorization

- Role-Based Access Control (RBAC)
- ADMIN Role
- USER Role
- Protected REST APIs

## User Management

- View Profile
- Update Profile
- Delete User (Admin)
- Get All Users (Admin)

---

# 🛠 Tech Stack

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JWT
- MySQL
- Maven
- Lombok

---

# 📁 Project Structure

```
src
└── main
    ├── java
    │   ├── config
    │   ├── controller
    │   ├── dto
    │   │   ├── request
    │   │   └── response
    │   ├── entity
    │   ├── enums
    │   ├── exception
    │   ├── mapper
    │   ├── repository
    │   ├── security
    │   ├── service
    │   └── util
    └── resources
```

---

# 🗄 Database

## Users Table

Stores user information including:

- First Name
- Last Name
- Email
- Password
- Phone Number
- Date of Birth
- Gender
- Nationality
- Passport Number
- Passport Expiry
- Address
- City
- State
- Country
- Role
- Account Status
- Email Verification Status
- Created At
- Updated At

---

# 🔐 Security

Implemented using Spring Security and JWT.

### Authentication Flow

```
User
   │
   ▼
Register/Login
   │
   ▼
Authentication Manager
   │
   ▼
JWT Generated
   │
   ▼
Client Stores Token
   │
   ▼
Protected API Request
   │
   ▼
JWT Filter
   │
   ▼
Authorized User
```

---

# 📌 REST APIs

## Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/public/register` | Register User |
| POST | `/api/public/login` | Login User |

---

## User APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/private/profile` | Get Profile |
| PUT | `/api/private/profile` | Update Profile |

---

## Admin APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/users` | Get All Users |
| GET | `/api/admin/users/{id}` | Get User By ID |
| DELETE | `/api/admin/users/{id}` | Delete User |

---

# ⚙ Configuration

Configure the following in `application.yml`:

- MySQL Database
- JWT Secret
- JWT Expiration Time

---

# ▶ Running the Service

Clone the repository:

```bash
git clone https://github.com/yourusername/flight-booking.git
```

Navigate to the service:

```bash
cd user-management-service
```

Run the application:

```bash
mvn spring-boot:run
```

---

# 📌 Future Enhancements

- Refresh Token Support
- Forgot Password
- Password Reset via Email
- Email Verification
- OAuth2 Login (Google/GitHub)
- Two-Factor Authentication (2FA)

---

# 👨‍💻 Author

**Vijay Kumar Soni**

Backend Java Developer

### Skills

- Java
- Spring Boot
- Spring Security
- JWT
- Hibernate
- MySQL
- REST APIs
- Microservices

---

⭐ If you found this project useful, please consider giving it a **Star**.
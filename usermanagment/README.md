# 👤 User Management Service

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen?logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-success?logo=springsecurity)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue?logo=mysql)
![Status](https://img.shields.io/badge/Status-Completed-success)

The **User Management Service** is a Spring Boot microservice responsible for user registration, authentication, authorization, profile management, and passenger information management.

---

## ✨ Features

### Authentication
- User registration and login
- JWT authentication
- Password encryption
- Stateless authentication
- Refresh token DTO support

### Authorization
- Role-Based Access Control (RBAC)
- `ADMIN` and `USER` roles
- Protected REST APIs

### User Management
- View and update user profiles
- Administrative user management
- User-related health endpoint

### Passenger Management
- Manage passenger information
- Add and update passenger details
- Retrieve passenger information

### Verification
- OTP request and verification components
- Email verification components
- Phone number verification components

### Password Management
- Forgot-password request handling
- Password change and reset request DTOs

### Scheduled Cleanup
- `UserCleanupScheduler` for scheduled user cleanup operations

---

## 🛠 Tech Stack

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JWT
- MySQL
- Maven
- Lombok
- Swagger / OpenAPI

---

## 📁 Project Structure

Based on your actual directory listing:

```text
usermanagment
└── src
    └── main
        ├── java
        │   └── com
        │       └── project
        │           └── usermanagment
        │               ├── UsermanagmentApplication.java
        │               │
        │               ├── config
        │               │   ├── JwtProperties.java
        │               │   ├── SwaggerConfig.java
        │               │   ├── bravoprop
        │               │   │   └── MailProperty.java
        │               │   └── twilioprop
        │               │       ├── OtpProperties.java
        │               │       └── TwilioProperties.java
        │               │
        │               ├── controller
        │               │   ├── AdminController
        │               │   │   └── AdminController.java
        │               │   ├── PassangerController
        │               │   │   └── PassengerController.java
        │               │   └── UserController
        │               │       ├── HealthController.java
        │               │       ├── privateUserController.java
        │               │       └── publicUserController.java
        │               │
        │               ├── dtos
        │               │   ├── PassengerDTO
        │               │   │   ├── PassengerRequestDTO.java
        │               │   │   ├── PassengerResponseDTO.java
        │               │   │   └── UpdatePassengerDTO.java
        │               │   └── UserDTO
        │               │       ├── UserResponse.java
        │               │       ├── OtherDTO
        │               │       ├── passwordDTO
        │               │       ├── registrationORlogin
        │               │       └── securitydto
        │               │
        │               ├── entity
        │               │   ├── OtpVerification.java
        │               │   ├── Passenger.java
        │               │   └── User.java
        │               │
        │               ├── enumFolder
        │               │   ├── Gender.java
        │               │   ├── OtpType.java
        │               │   ├── PassengerType.java
        │               │   ├── Role.java
        │               │   └── Title.java
        │               │
        │               ├── exception
        │               │   ├── GlobalExceptionHandler.java
        │               │   └── UnauthorizedAccessException.java
        │               │
        │               ├── mapper
        │               │   └── PassengerMapper.java
        │               │
        │               ├── repository
        │               │   ├── PassengerRepository.java
        │               │   └── UserRepository.java
        │               │
        │               ├── Scheduler
        │               │   └── UserCleanupScheduler.java
        │               │
        │               ├── security
        │               │   ├── CustomUserDetails.java
        │               │   ├── CustomUserDetailsService.java
        │               │   ├── JwtAuthenticationFilter.java
        │               │   ├── JwtUtil.java
        │               │   └── SecurityConfig.java
        │               │
        │               └── service
        │                   ├── AdminService
        │                   │   └── AdminService.java
        │                   ├── PassengerService
        │                   │   └── PassengerService.java
        │                   └── UserService
        │                       ├── OtpService.java
        │                       ├── PrivateUserService.java
        │                       ├── PublicUserService.java
        │                       └── Verification
        │                           ├── EmailVerificationService.java
        │                           └── NumberVerificationService.java
        │
        └── resources
            └── application.yaml
```

---

## 📦 Main Packages

| Package | Description |
|---|---|
| `config` | JWT, Swagger, email, and OTP configuration properties |
| `controller` | User, admin, passenger, and health endpoints |
| `dtos` | User and passenger request/response objects |
| `entity` | User, passenger, and OTP verification entities |
| `enumFolder` | Roles, gender, passenger types, titles, and OTP types |
| `exception` | Custom exception handling |
| `mapper` | Passenger mapping |
| `repository` | User and passenger database operations |
| `Scheduler` | Scheduled user cleanup |
| `security` | JWT authentication and user-details components |
| `service` | User, admin, passenger, OTP, and verification logic |

---

## 🗄 Database

### Main Entities

| Entity | Purpose |
|---|---|
| `User.java` | Stores user account and profile information |
| `Passenger.java` | Stores passenger information |
| `OtpVerification.java` | Stores OTP verification-related information |

The exact fields and database relationships are defined in the entity classes.

---

## 🔐 Security

The service uses Spring Security and JWT authentication components.

### Authentication Flow

```text
       User
         |
         v
   Register / Login
         |
         v
   Authentication
         |
         v
      JWT Token
         |
         v
  Protected API Request
         |
         v
 JWT Authentication Filter
         |
         v
  Access According to
  Security Configuration
```

---

## 📌 REST APIs

The service contains separate controllers for public user operations, private user operations, administrator operations, and passenger operations.

| Controller | Purpose |
|---|---|
| `publicUserController` | Public user operations |
| `privateUserController` | Authenticated user operations |
| `AdminController` | Administrative operations |
| `PassengerController` | Passenger operations |
| `HealthController` | Health-related endpoint |

Check the controller annotations for the exact endpoint paths and HTTP methods before documenting individual APIs.

---

## ⚙ Configuration

The main configuration file is:

```text
src/main/resources/application.yaml
```

The project also contains configuration classes for:

- JWT properties
- Swagger / OpenAPI
- Mail properties
- OTP and Twilio properties

Keep database credentials, JWT secrets, and service credentials out of version control.

---

## ▶️ Running the Service

### Prerequisites
- Java 21
- Maven
- MySQL
- Required application configuration

### Run Locally

Open a terminal in the `usermanagment` directory:

```bash
mvn clean install
```

Start the application:

```bash
mvn spring-boot:run
```

The application port is determined by your `application.yaml` configuration.

---

## 🚀 Future Enhancements

- Refresh token lifecycle improvements
- Stronger email and phone verification
- Improved password reset workflow
- OAuth2 login
- Two-factor authentication (2FA)
- Additional unit and integration tests

---

## 👨‍💻 Author

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

⭐ If you find this project useful, please consider giving it a **Star**.
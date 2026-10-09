# 🎫 Booking Management Service

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Backend-brightgreen?logo=springboot)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue?logo=mysql)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)

The **Booking Management Service** is a Spring Boot microservice responsible for managing flight bookings, passenger details, booking status, and payment information. It provides APIs for user, administrator, and internal booking operations.

---

## 🔌 Port Configuration

| Property | Value |
|---|---|
| Service Name | Booking Management Service |
| Port | `8083` |
| Local URL | `http://localhost:8083` |

Configure the application port in `application.yaml`:

```yaml
server:
  port: 8083
```

---

## ✨ Features

### Booking Management
- Create and manage flight bookings.
- Generate booking references and PNRs.
- Retrieve booking details.
- Update booking information.
- Manage booking status.
- Process booking cancellation requests.

### Passenger Management
- Manage passenger details associated with bookings.
- Validate passenger information.
- Handle passenger updates and seat-selection details.

### Payment Information
- Maintain booking payment status.
- Process payment-status update requests.
- Manage payment-related booking information.

### Flight and Seat Integration
- Communicate with Flight Management using `FlightServiceClient`.
- Handle flight and seat-related information.
- Send seat reservation requests through the configured client.

### Booking Expiry
- Process booking-expiry operations using `BookingExpiryScheduler`.
- Perform booking cleanup operations through `BookingCleanupService`.

### Security
- JWT authentication components.
- Spring Security configuration.
- User, administrator, and internal booking controllers.
- Centralized exception handling.

---

## 🛠 Technology Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate ORM
- MySQL
- Maven
- Lombok
- Spring Security
- JWT
- Spring Cloud OpenFeign
- REST APIs
- Swagger / OpenAPI
- Docker

---

## 📁 Project Structure

```text
bookingmanagement
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── project
│       │           └── bookingmanagement
│       │               ├── FlightBookingServiceApplication.java
│       │               │
│       │               ├── client
│       │               │   ├── FlightServiceClient.java
│       │               │   └── PaymentServiceClient.java
│       │               │
│       │               ├── config
│       │               │   ├── feign
│       │               │   │   └── FeignConfig.java
│       │               │   ├── jwt
│       │               │   │   ├── CustomUserPrincipal.java
│       │               │   │   ├── JwtAuthenticationFilter.java
│       │               │   │   ├── JwtProperties.java
│       │               │   │   └── JwtUtil.java
│       │               │   ├── security
│       │               │   │   ├── SecurityConfig.java
│       │               │   │   └── SecurityUtil.java
│       │               │   └── swagger
│       │               │       └── OpenApiConfig.java
│       │               │
│       │               ├── controller
│       │               │   ├── AdminBookingController.java
│       │               │   ├── InternalBookingController.java
│       │               │   └── UserBookingController.java
│       │               │
│       │               ├── dto
│       │               │   ├── booking
│       │               │   │   ├── internal
│       │               │   │   ├── request
│       │               │   │   └── response
│       │               │   ├── common
│       │               │   ├── external
│       │               │   │   ├── flight
│       │               │   │   └── user
│       │               │   └── passenger
│       │               │       ├── internal
│       │               │       ├── request
│       │               │       └── response
│       │               │
│       │               ├── entity
│       │               │   ├── Booking.java
│       │               │   ├── BookingPassenger.java
│       │               │   └── PaymentInfo.java
│       │               │
│       │               ├── enums
│       │               │   ├── bookingEnum
│       │               │   ├── bookingPassangerEnum
│       │               │   ├── paymentInfoEnum
│       │               │   └── validation
│       │               │
│       │               ├── exception
│       │               │   ├── BookingAlreadyCancelledException.java
│       │               │   ├── BookingCancellationException.java
│       │               │   ├── BookingNotFoundException.java
│       │               │   ├── BookingValidationException.java
│       │               │   ├── CouponNotValidException.java
│       │               │   ├── ExternalServiceException.java
│       │               │   ├── FlightNotAvailableException.java
│       │               │   ├── GlobalExceptionHandler.java
│       │               │   ├── PassengerNotFoundException.java
│       │               │   └── SeatAlreadyBookedException.java
│       │               │
│       │               ├── mapper
│       │               │   ├── BookingMapper.java
│       │               │   └── PassengerMapper.java
│       │               │
│       │               ├── repository
│       │               │   ├── BookingPassengerRepository.java
│       │               │   └── BookingRepository.java
│       │               │
│       │               ├── scheduler
│       │               │   └── BookingExpiryScheduler.java
│       │               │
│       │               ├── security
│       │               │   └── CurrentUserService.java
│       │               │
│       │               ├── service
│       │               │   ├── implementations
│       │               │   │   ├── BookingCleanupService.java
│       │               │   │   └── BookingServiceImpl.java
│       │               │   └── interfaces
│       │               │       ├── BookingService.java
│       │               │       └── PassengerService.java
│       │               │
│       │               └── util
│       │                   ├── BookingReferenceGenerator.java
│       │                   ├── PnrGenerator.java
│       │                   └── SecurityUtils.java
│       │
│       └── resources
│           └── application.yaml
├── pom.xml
├── Dockerfile
└── README.md
```

---

## 📦 Main Packages

| Package | Description |
|---|---|
| `client` | Clients for flight and payment service communication |
| `config` | Feign, JWT, security, and Swagger configuration |
| `controller` | User, administrator, and internal booking APIs |
| `dto` | Booking and passenger request and response objects |
| `entity` | Booking, passenger, and payment entities |
| `enums` | Booking, passenger, payment, and validation enumerations |
| `exception` | Custom exceptions and global exception handling |
| `mapper` | Booking and passenger mapping |
| `repository` | Database access for bookings and passengers |
| `scheduler` | Booking-expiry scheduling |
| `security` | Current-user utilities |
| `service` | Booking business logic and service interfaces |
| `util` | Booking reference, PNR, and security utilities |

---

## 🗄 Main Entities

### `Booking.java`
Represents a flight booking and its associated booking information.

### `BookingPassenger.java`
Stores passenger information associated with a booking.

### `PaymentInfo.java`
Represents payment-related information associated with a booking.

---

## 🎮 Controllers

| Controller | Purpose |
|---|---|
| `UserBookingController` | User booking operations |
| `AdminBookingController` | Administrator booking operations |
| `InternalBookingController` | Internal booking operations |

The exact API endpoints and HTTP methods are defined in the respective controller classes.

---

## ⚙ Configuration

The main configuration file is located at:

```text
src/main/resources/application.yaml
```

It contains the application's configuration, including the server port, database connection, security properties, and service communication settings.

Database credentials and other sensitive configuration values should be kept out of version control.

---

## ▶️ How to Run

### Prerequisites

- Java 21
- Maven
- MySQL
- Required application configuration

### Run Locally

Open a terminal in the `bookingmanagement` directory.

```bash
mvn clean install
```

Start the application:

```bash
mvn spring-boot:run
```

The application is configured to run at:

```text
http://localhost:8083
```

### Run with Docker

If the Docker configuration is set up:

```bash
docker build -t booking-management .
docker run --name booking-management -p 8083:8083 booking-management
```

Ensure the required database and service configuration is available to the container.

---

## 👨‍💻 Author

**Vijay Kumar Soni**

Java Backend Developer

**Skills:** Java, Spring Boot, Spring Security, Spring Data JPA, Hibernate, MySQL, REST APIs, Microservices, Maven, Docker.

---

⭐ If you find this project useful, please consider giving it a **Star**.

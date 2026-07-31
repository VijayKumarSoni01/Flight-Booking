# ✈️ Flight Booking System

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-success)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)
![Maven](https://img.shields.io/badge/Maven-Build-red)
![Status](https://img.shields.io/badge/Status-Active_Development-success)
![OpenFeign](https://img.shields.io/badge/OpenFeign-Enabled-blue)
![Docker](https://img.shields.io/badge/Docker-Supported-2496ED)

A production-inspired Flight Booking System built using Spring Boot Microservices. The project follows a scalable microservices architecture with secure authentication, RESTful APIs, and clean coding practices.

---

# 🚀 Project Status

🚧 **Actively Under Development**

## ✅ Completed

- User Management Service
- Flight Management Service
- Booking Management Service
- API Gateway
- JWT Authentication & Authorization
- Spring Security
- Role-Based Access Control (RBAC)
- REST APIs
- OpenFeign Communication
- Global Exception Handling
- DTO & Mapper Layer
- Seat Reservation System
- Automatic Seat Release Scheduler
- Booking Expiry Scheduler
- Docker Support
- Multi-Profile Configuration (Dev / Docker / Prod)

## 🚧 In Progress

- Payment Service
- Notification Service

## ⏳ Planned

- Service Discovery (Eureka)
- Config Server
- Kafka Event Streaming
- Distributed Tracing
- Redis Caching
- Kubernetes Deployment
- CI/CD Pipeline
- AWS Deployment

---

# 📦 Microservices

| Service | Status |
|----------|--------|
| ✅ User Management | Completed |
| ✅ Flight Management | Completed |
| ✅ Booking Management | Completed |
| 🚧 Payment Service | In Progress |
| ⏳ Notification Service | Planned |
| ✅ API Gateway | Completed |
| ⏳ Service Discovery | Planned |
---

# 🛠 Tech Stack

- Java 21
- Spring Boot 4.x
- Spring Security
- JWT Authentication
- Spring Cloud Gateway
- OpenFeign
- Spring Scheduler
- Bean Validation
- Spring Data JPA (Hibernate)
- MySQL
- Swagger / OpenAPI
- Docker
- Maven
- Git & GitHub

---

### Planned

- Apache Kafka
- Redis
- Kubernetes
- Jenkins / GitHub Actions
- AWS

---

# 📂 Project Structure

```
flight-booking-project/
│
├── usermanagment/
├── flightmanagement/
├── bookingmanagement/
├── apigateway/
├── docker-compose.yml
├── .gitignore
└── README.md
```

---

# ✨ Features

## 👤 User Management

- User Registration
- Login
- JWT Authentication
- Role-Based Authorization (RBAC)
- Email Verification
- Password Encryption
- Profile Management
- Passenger Management

---

## ✈️ Flight Management

- Airline Management
- Airport Management
- Aircraft Management
- Flight CRUD
- Flight Fare Management
- Flight Status Management
- Flight Amenities
- Seat Management
- Seat Reservation
- Seat Confirmation
- Seat Release
- Seat Availability
- Flight Validation
- Seat Hold
- Automatic Seat Release
---

## 📖 Booking Management

- Create Booking
- Booking Reference Generation
- PNR Generation
- Passenger Mapping
- Fare Calculation
- Seat Reservation
- Booking Expiry
- Automatic Seat Release
- Booking Status Tracking
- Payment Status Tracking
- Flight Validation
- Seat Availability Check

---

## 🌐 API Gateway

- JWT Authentication
- Route Management
- Request Forwarding
- Secure API Access

---

# 🏗 Architecture

```
                         Client
                            │
                            ▼
                     Spring Cloud Gateway
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
 User Management     Flight Management    Booking Management
        │                   ▲                   │
        │                   │                   │
        └────────────Feign Client───────────────┘
                            │
                            ▼
                    Payment Service (Planned)
                            │
                            ▼
                 Notification Service (Planned)
```

---

# ⚙ Prerequisites

- Java 21
- Maven 3.9+
- MySQL 8+
- IntelliJ IDEA / Eclipse
- Git

---

# 🚀 Getting Started

Clone the repository

```bash
git clone https://github.com/VijayKumarSoni01/flight-booking-project.git
```

Go to the project

```bash
cd flight-booking-project
```

Run a microservice

```bash
cd usermanagment
mvn spring-boot:run
```

or

```bash
cd flightmanagement
mvn spring-boot:run
```

or

```bash
cd bookingmanagement
mvn spring-boot:run
```

or

```bash
cd apigateway
mvn spring-boot:run
```

---

# 📌 Future Roadmap

- Payment Microservice
- Notification Microservice
- Service Discovery (Eureka)
- Config Server
- Kafka Integration
- Redis Cache
- Distributed Logging
- Docker Compose
- Kubernetes
- CI/CD (GitHub Actions / Jenkins)
- AWS Deployment
- Monitoring (Prometheus + Grafana)

---

# 🤝 Contributing

1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Push the branch.
5. Open a Pull Request.

---

# 📄 License

This project is developed for learning and portfolio purposes.

---

# 👨‍💻 Author

**Vijay Kumar Soni**

Backend Java Developer

### Skills

- Java
- Spring Boot
- Spring Security
- Spring Cloud Gateway
- Microservices
- JWT Authentication
- REST APIs
- OpenFeign
- Hibernate / JPA
- MySQL
- Docker
- Maven
- Git & GitHub

---

## 📖 About the Project

This Flight Booking System is a production-inspired microservices application designed to simulate a real-world airline reservation platform. It demonstrates secure authentication, service-to-service communication, seat management, booking workflows, and scalable backend architecture using Spring Boot and Spring Cloud technologies.

The project showcases enterprise backend development concepts including JWT-based authentication, Spring Security, RESTful APIs, OpenFeign communication, Docker support, and microservices best practices. It is being developed as a portfolio project to demonstrate scalable system design and modern Java backend development.

---

**GitHub:** https://github.com/VijayKumarSoni01

**LinkedIn:** https://www.linkedin.com/in/vijay-kumar-soni-3217b1309

---

⭐ If you like this project, don't forget to **Star** the repository.
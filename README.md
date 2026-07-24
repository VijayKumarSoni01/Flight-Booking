# ✈️ Flight Booking System

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-success)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)
![Maven](https://img.shields.io/badge/Maven-Build-red)
![Status](https://img.shields.io/badge/Status-In_Development-yellow)

A production-inspired Flight Booking System built using Spring Boot Microservices. The project follows a scalable microservices architecture with secure authentication, RESTful APIs, and clean coding practices.

---

# 🚀 Project Status

🚧 **Currently Under Development**

## ✅ Completed

- User Management Service
- Flight Management Service (Core Setup)
- JWT Authentication
- Spring Security
- Entity Design
- Repository Layer
- DTOs
- Mapper
- Global Exception Handling

## 🚧 In Progress

- Flight Service
- Flight Controller
- Validation
- Business Logic

## ⏳ Planned

- Booking Service
- Payment Service
- Notification Service
- API Gateway
- Service Discovery
- Kafka Integration
- Docker
- Kubernetes
- CI/CD

---

# 📦 Microservices

| Service | Status |
|----------|--------|
| User Management | ✅ Completed |
| Flight Management | 🚧 In Progress |
| Booking Service | ⏳ Planned |
| Payment Service | ⏳ Planned |
| Notification Service | ⏳ Planned |
| API Gateway | ⏳ Planned |
| Service Discovery | ⏳ Planned |

---

# 🛠 Tech Stack

- Java 21
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA (Hibernate)
- MySQL
- Maven
- Lombok
- Docker (Planned)
- Apache Kafka (Planned)
- Kubernetes (Planned)

---

# 📂 Project Structure

```
flight-booking/
│
├── user-management-service/
├── flight-management-service/
├── booking-service/
├── payment-service/
├── notification-service/
├── api-gateway/
├── service-discovery/
└── README.md
```

---

# ✨ Features

## 👤 User Management

- User Registration
- User Login
- JWT Authentication
- Role-Based Authorization (RBAC)
- Profile Management

## ✈️ Flight Management

- Airline Management
- Airport Management
- Aircraft Management
- Flight CRUD Operations
- Flight Search *(Planned)*
- Flight Status Management *(Planned)*

---

# 🏗 Architecture

```
                    Client
                       │
                       ▼
                 API Gateway
                       │
 ┌───────────────┬───────────────┬───────────────┐
 │               │               │               │
 ▼               ▼               ▼               ▼
User Service  Flight Service  Booking Service  Payment Service
                                       │
                                       ▼
                              Notification Service
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
git clone https://github.com/yourusername/flight-booking.git
```

Go to a microservice

```bash
cd user-management-service
```

or

```bash
cd flight-management-service
```

Run

```bash
mvn spring-boot:run
```

---

# 📌 Future Roadmap

- Complete Flight Management Service
- Develop Booking Service
- Payment Integration
- Notification Service
- API Gateway
- Service Discovery (Eureka)
- Kafka Messaging
- Docker
- Kubernetes
- CI/CD Pipeline
- AWS Deployment

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

**Skills**

- Java
- Spring Boot
- Spring Security
- Microservices
- REST APIs
- MySQL
- JWT
- Maven

---

⭐ If you like this project, don't forget to **Star** the repository.
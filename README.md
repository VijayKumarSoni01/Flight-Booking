# ✈️ Flight Booking Microservices

A Flight Booking System built using **Java, Spring Boot, Spring Security, JWT, MySQL, and a Microservices Architecture**. The project is designed to manage users, flights, bookings, payments, and flight-related operations through independent backend services.

The goal is to build a scalable, secure, and reliable flight booking application while progressively introducing production-level backend engineering practices.

## 📌 Project Overview

The application follows a microservices architecture in which each service is responsible for a specific business domain. Services communicate through REST APIs and OpenFeign where configured, while the API Gateway provides a centralized entry point to the application.

### Key Features

- User registration, login, and JWT-based authentication.
- Role-based access control (RBAC).
- Flight, airline, airport, and aircraft management.
- Flight schedules, fares, and seat availability.
- Seat reservation and booking management.
- Passenger details and booking references.
- Payment processing integration.
- Booking and payment status tracking.
- Docker-based development and deployment support.

**Development status:** Active development. Feature availability may vary by service.

## 🏗️ Microservices Architecture

The project is divided into the following services:

| Service | Responsibility | Status |
|---|---|---|
| User Management | User accounts, authentication, profiles, and passenger information | Implemented features; ongoing improvements |
| Flight Management | Flights, schedules, fares, airports, airlines, aircraft, and seats | Implemented features; ongoing improvements |
| Booking Management | Bookings, passengers, fare calculation, seat reservation, and booking status | Implemented features; ongoing improvements |
| Payment Management | Payment initiation, verification, gateway integration, and refunds | In progress |
| Notification Management | Booking confirmations, payment notifications, and cancellation alerts | Planned |
| API Gateway | Centralized routing and request authentication | Implemented features; ongoing improvements |

## 🧰 Technology Stack

### Backend
- Java 21
- Spring Boot 4.x
- Spring Web
- Spring Data JPA
- Spring Security
- JWT Authentication
- Hibernate
- Maven
- OpenFeign for service-to-service communication where configured

### Database
- MySQL
- Relational database design
- JPA/Hibernate ORM

### API Documentation
- Springdoc OpenAPI
- Swagger UI

### Payment Integration
- Razorpay integration
- Stripe integration

*The availability and completion of individual payment flows must be verified against the current implementation and configuration.*

### Development and Deployment
- Git and GitHub
- Docker
- Docker Compose
- Postman
- Eclipse/IDE development tools

## 📂 Project Structure

The main project structure is organized as follows:

```text
flight-booking-project/
│
├── usermanagment/
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
│
├── flightmanagement/
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
│
├── bookingmanagement/
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
│
├── payment/
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
│
├── apigateway/
│   ├── src/
│   ├── pom.xml
│   └── README.md
│
├── docker-compose.yml
├── .gitignore
└── README.md
```

*This is a representative structure. Include only files and service folders that exist in your repository.*

## 👤 1. User Management Service

The User Management Service manages user accounts, authentication, authorization, profiles, and passenger information.

### Main Responsibilities

- User registration and login.
- JWT access-token authentication.
- Password encryption using BCrypt.
- Role-based access control.
- User profile management.
- Passenger information management.
- Email verification and phone-number verification.
- OTP generation and verification.
- Password recovery and password changes.
- Administrative user-management operations.
- Scheduled cleanup of expired or temporary records, where configured.

### Main Components

- Controllers for public, private, administrative, and passenger operations.
- User and passenger entities.
- User and passenger repositories.
- JWT utilities and security filters.
- Authentication and verification services.
- DTOs for requests and responses.
- Exception handling.

## ✈️ 2. Flight Management Service

The Flight Management Service manages flight information, schedules, fares, airports, airlines, aircraft, and seats.

### Main Responsibilities

- Airline management.
- Airport management.
- Aircraft management.
- Flight creation and management.
- Flight schedule management.
- Flight instance management for individual travel dates.
- Flight fare management.
- Flight status information.
- Baggage policy management.
- Flight amenities.
- Seat creation and seat availability.
- Seat reservation and status management.
- Seat confirmation and release operations, where implemented.
- Scheduled flight-instance creation and flight-status updates.

### Flight Search

The public flight-search functionality uses flight schedules and flight instances to identify flights for a requested travel date. Search results should reflect the requested date, route, availability, and applicable fares.

### Seat Management

The service maintains seat-related information and reservation status. Further improvements will focus on concurrent reservations, temporary seat holds, and consistent seat-state transitions.

## 🎫 3. Booking Management Service

The Booking Management Service manages the booking lifecycle and coordinates with the Flight and Payment Management services.

### Main Responsibilities

- Booking creation and validation.
- Passenger details associated with bookings.
- Booking reference generation.
- PNR generation.
- Fare calculation and price breakdown.
- Flight availability validation.
- Seat reservation requests.
- Booking and payment status tracking.
- Booking expiry processing.
- Automatic cleanup and seat release for expired bookings, where configured.
- Booking details and booking history.
- Booking cancellation and flight-change workflows as implemented.
- Internal communication with Flight and Payment services.

### Booking Lifecycle

A typical booking workflow is:

1. The user searches for a flight.
2. The user selects a flight and provides passenger details.
3. The Booking Management Service validates the request.
4. Flight availability and seat reservation are checked.
5. The booking is created with an appropriate initial status.
6. The Payment Management Service processes the payment.
7. The payment result is verified.
8. The booking payment status is updated.
9. The booking is confirmed after the required checks succeed.
10. If the booking expires or is cancelled, the appropriate seat-release and refund operations are performed according to the implemented workflow.

The exact sequence and transaction guarantees depend on the current service implementation.

## 💳 4. Payment Management Service

The Payment Management Service handles payment-related operations and coordinates payment status with the Booking Management Service.

### Main Responsibilities

- Payment initiation.
- Payment records and status tracking.
- Payment verification.
- Payment gateway integration.
- Booking payment-status updates.
- Payment attempts and failure handling.
- Refund processing.
- Webhook handling for gateway events.
- Administrative payment operations and statistics.

### Payment Reliability Goals

- Prevent duplicate payments.
- Verify payment gateway callbacks and webhooks.
- Handle repeated requests safely.
- Recover from temporary gateway or network failures.
- Maintain consistent payment and booking statuses.
- Track refund status and failed payment attempts.

Payment-related operations must be tested with gateway credentials and appropriate sandbox environments before being considered production-ready.

## 🔔 5. Notification Management Service — Planned

The Notification Management Service will handle user communications for important booking events.

### Planned Responsibilities

- Booking confirmation notifications.
- Payment success and failure notifications.
- Booking cancellation notifications.
- Refund-status notifications.
- Email delivery.
- Optional SMS notifications.
- Notification retry handling.
- Delivery-status tracking.

Apache Kafka may be introduced to process notification events asynchronously and reduce direct dependencies between business services.

## 🌐 6. API Gateway

The API Gateway acts as the application's central entry point and routes incoming requests to the appropriate microservice.

### Main Responsibilities

- Centralized request routing.
- JWT authentication where configured.
- Forwarding requests to downstream services.
- Public and protected route handling.
- Consistent API entry points.
- Integration with backend microservices.

Future improvements may include rate limiting, centralized observability, stronger service-to-service security, and standardized error responses.

## 🏛️ Architecture Overview

```text
                    Client Application
                           |
                           v
                     API Gateway
                           |
          +----------------+----------------+
          |                |                |
          v                v                v
   User Management   Flight Management   Booking Management
          |                |                |
          v                v                v
       MySQL             MySQL             MySQL
                                             |
                                             v
                                    Payment Management
                                             |
                                             v
                                      Payment Gateway

                    Future Integration
                           |
                           v
                    Apache Kafka
                           |
              +------------+------------+
              |            |            |
              v            v            v
          Booking      Payment     Notification
           Events       Events       Service
```

The diagram represents logical responsibilities. Each service's actual routing, database configuration, and event-driven integrations depend on the deployment configuration and future implementation.

## 🔐 Security

Security is an important part of the project.

### Current Security Approach

- JWT-based authentication.
- Spring Security integration.
- BCrypt password hashing.
- Role-based access control.
- Public and protected endpoint separation.
- Authentication filters for validating incoming tokens.

### Planned Security Improvements

- Centralized authorization policies.
- OAuth 2.0 and OpenID Connect exploration.
- API rate limiting and abuse prevention.
- Secure service-to-service authentication.
- Externalized secrets and environment-based configuration.
- Validation of all incoming requests.
- More comprehensive security testing.

## 🗄️ Database Design

MySQL is used for persistent relational data, with Spring Data JPA and Hibernate handling entity persistence.

The main data domains include:

- **Users:** Account details, credentials, roles, and profile information.
- **Passengers:** Passenger details associated with user accounts or bookings.
- **Flights:** Flight information, schedules, and flight instances.
- **Airports and airlines:** Route endpoints and airline information.
- **Aircraft and seats:** Aircraft configuration, seats, and seat status.
- **Fares:** Fare and pricing information.
- **Bookings:** Booking references, passenger associations, and booking status.
- **Payments:** Payment attempts, gateway references, statuses, and refund details.

The database schema and relationships are maintained within the corresponding service. Future improvements will focus on indexing, query optimization, migration management, and transaction reliability.

## ⚙️ Prerequisites

Install the following tools before running the project:

- Java Development Kit (JDK) 21.
- Maven.
- MySQL Server.
- Git.
- Postman or another API testing tool.
- Docker Desktop, if using containers.

Use the Java and Spring Boot versions configured in each service's `pom.xml`. Confirm compatibility before upgrading dependencies.

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/VijayKumarSoni01/flight-booking-project.git
cd flight-booking-project
```

### 2. Configure the Database

Create the required MySQL databases and configure the database URL, username, and password in each service's application configuration.

Example:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/flightbooking
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

Use the appropriate database name for each service. The example above is illustrative; adjust it to match your actual configuration.

### 3. Configure Environment Variables

Configure the required values for:

- MySQL credentials.
- JWT signing secrets.
- Payment gateway credentials.
- Email and SMS provider credentials, if enabled.
- Downstream service URLs.
- Active Spring profiles.

Do not commit passwords, private keys, JWT secrets, or payment credentials to GitHub.

### 4. Build a Service

Open a terminal in the service directory and run:

```bash
mvn clean package
```

### 5. Run a Service

From the service directory, run:

```bash
mvn spring-boot:run
```

Repeat for the services required by your chosen workflow. Check each service's configuration for its actual port and dependencies.

### 6. Run with Docker Compose

If a valid root `docker-compose.yml` is configured, start the required containers with:

```bash
docker compose up -d --build
```

Check container status:

```bash
docker compose ps
```

View service logs:

```bash
docker compose logs -f
```

The Compose configuration must define the correct service names, ports, environment variables, networks, and database dependencies.

## 🧪 Testing

The project should be tested at both the individual-service and integration levels.

### Important Test Scenarios

- User registration and login.
- JWT validation and protected endpoints.
- Flight search for different dates and routes.
- Flight availability and fare retrieval.
- Concurrent seat reservation requests.
- Booking creation and expiry.
- Successful and failed payments.
- Duplicate payment requests.
- Payment webhook verification.
- Booking cancellation and seat release.
- Refund processing.
- Inter-service communication failures.

JUnit and Mockito can be used for unit testing. Testcontainers can be introduced for integration testing with real database and infrastructure containers.

## 🚀 Future Enhancements

The following enhancements are planned to improve performance, scalability, reliability, security, and production readiness.

### 1. Redis Integration

Redis will be considered for low-latency data access and distributed coordination.

Planned improvements:

- Cache frequently accessed airport, airline, and flight information.
- Cache suitable flight-search results with appropriate expiry.
- Use cache invalidation when relevant flight information changes.
- Explore Redis-based distributed locks for coordinating seat reservations across service instances.
- Store temporary seat holds with time-to-live (TTL) values.
- Improve performance by reducing unnecessary database reads.

**Important:** Redis locking alone does not guarantee seat-reservation correctness. Database constraints, atomic state transitions, and concurrency tests must also protect inventory.

### 2. Apache Kafka Integration

Apache Kafka will support asynchronous communication and an event-driven architecture.

Planned improvements:

- Publish booking-created, booking-confirmed, booking-cancelled, and booking-expired events.
- Publish payment-success, payment-failure, and refund-status events.
- Allow the Notification Management Service to consume relevant events.
- Reduce unnecessary synchronous communication between services.
- Implement retry handling and dead-letter topics.
- Make event consumers idempotent to tolerate duplicate delivery.
- Monitor consumer lag and event-processing failures.

For reliable event publishing, the project may adopt the transactional Outbox pattern to prevent database changes and event publication from becoming inconsistent.

### 3. Inventory and Seat Reservation Reliability

Seat availability and reservation correctness are critical to a flight booking system.

Planned improvements:

- Add optimistic locking using JPA's `@Version`.
- Implement atomic seat reservation operations.
- Introduce temporary seat holds and automatic hold expiry.
- Prevent two users from booking the same seat.
- Release seats safely after booking expiry or cancellation.
- Add concurrent booking tests.
- Reconcile stale reservations and inconsistent seat states.

### 4. Payment Reliability and Refund Workflow

Planned improvements:

- Add idempotency keys to payment-creation requests.
- Protect against duplicate gateway webhook processing.
- Verify webhook signatures and payment amounts.
- Add retry policies for transient failures.
- Implement refund initiation and refund-status tracking.
- Handle payment success when the booking update temporarily fails.
- Reconcile payment and booking states.
- Evaluate the Saga pattern for workflows spanning multiple services.

### 5. Notification Management

Planned improvements:

- Build a dedicated Notification Management microservice.
- Send booking and payment confirmation emails.
- Send cancellation and refund updates.
- Integrate Kafka for asynchronous notification processing.
- Add delivery-status tracking and retry policies.
- Support configurable notification templates.
- Explore SMS integration where required.

### 6. Resilience and Service Communication

Planned improvements:

- Add connection and response timeouts.
- Introduce circuit breakers and controlled retries using Resilience4j.
- Prevent retry storms and duplicate business operations.
- Improve handling of unavailable downstream services.
- Evaluate Eureka or another service discovery mechanism if needed.
- Explore Spring Cloud Config for centralized configuration.
- Document service contracts and internal API versions.

### 7. Monitoring, Logging, and Distributed Tracing

Planned improvements:

- Use Spring Boot Actuator for health checks.
- Collect application metrics using Prometheus.
- Build monitoring dashboards with Grafana.
- Introduce centralized logging using the ELK or OpenSearch stack.
- Explore OpenTelemetry with Jaeger or Zipkin for distributed tracing.
- Track request latency, error rates, and service availability.
- Monitor payment failures, booking failures, and Kafka consumer lag.
- Configure alerts for critical failures.

### 8. Docker and Kubernetes

Planned improvements:

- Standardize Dockerfiles across services.
- Improve Docker Compose configuration for local development.
- Add health checks and dependency-aware startup behavior.
- Use Kubernetes for container orchestration when deployment requirements justify it.
- Configure readiness and liveness probes.
- Support scaling and self-healing.
- Manage secrets and configuration securely.
- Define CPU and memory resource requests and limits.

### 9. CI/CD and Cloud Deployment

Planned improvements:

- Create automated build and test workflows using GitHub Actions.
- Run code quality and security checks in CI.
- Build and publish container images.
- Automate deployment to test and production environments.
- Explore AWS or Azure for cloud hosting.
- Evaluate managed databases and container registries.
- Configure environment-specific settings and secrets.
- Add deployment health checks and rollback procedures.

### 10. Testing and Code Quality

Planned improvements:

- Expand JUnit and Mockito unit tests.
- Add integration tests for service communication.
- Introduce Testcontainers for database and infrastructure testing.
- Test concurrent seat reservations and booking expiry.
- Test payment retries, duplicate requests, and webhook failures.
- Add API contract testing where useful.
- Introduce static analysis and automated code quality checks.

### 11. Booking Experience

Planned improvements:

- Downloadable booking tickets and invoices in PDF format.
- Booking history and a My Bookings interface.
- Flight-change workflows.
- Improved cancellation and refund tracking.
- Search filters, sorting, and pagination.
- Fare alerts and price tracking.
- Better validation messages and error responses.

### 12. Additional Architecture Improvements

Planned improvements:

- Evaluate service discovery based on deployment needs.
- Centralize configuration where appropriate.
- Introduce API versioning and consistent error formats.
- Improve database migration and schema-change management.
- Document architecture decisions and service dependencies.
- Add load testing to identify performance bottlenecks.
- Review backup, recovery, and disaster-recovery procedures.

### Planned Technology Additions

| Technology | Intended Purpose |
|---|---|
| Redis | Caching, temporary seat holds, and distributed coordination |
| Apache Kafka | Asynchronous events and event-driven communication |
| Resilience4j | Circuit breakers, retries, and fault tolerance |
| Spring Boot Actuator | Health checks and application metrics |
| Prometheus | Metrics collection |
| Grafana | Monitoring dashboards |
| OpenTelemetry | Distributed tracing instrumentation |
| ELK / OpenSearch | Centralized logging |
| Testcontainers | Integration testing with real infrastructure |
| GitHub Actions | CI/CD automation |
| Kubernetes | Container orchestration |
| AWS / Azure | Cloud deployment |

**Implementation priority:** Focus first on inventory correctness, atomic seat reservation, seat-hold expiry, payment idempotency, webhook protection, and refund handling. Next, introduce Redis and Kafka, then monitoring and automated testing. Kubernetes and cloud deployment can follow once the core booking and payment workflows are reliable.

These enhancements are planned. Their inclusion in this roadmap does not imply that they are already implemented.

## 📈 Project Goals

The long-term goals of the project are to:

- Build reliable Spring Boot microservices.
- Improve secure API design and service communication.
- Handle concurrent booking and payment operations safely.
- Develop practical experience with caching and event-driven architecture.
- Improve testing, observability, and fault tolerance.
- Gain experience with Docker, CI/CD, and cloud deployment.

## 🤝 Contributing

Contributions and suggestions are welcome.

1. Fork the repository.
2. Create a feature branch.
3. Make changes and add appropriate tests.
4. Verify that the application builds successfully.
5. Submit a pull request describing the changes.

## 👨‍💻 Author

**Vijay Kumar Soni**

- GitHub: [VijayKumarSoni01](https://github.com/VijayKumarSoni01)
- Project Repository: [Flight Booking Microservices](https://github.com/VijayKumarSoni01/flight-booking-project)
- LinkedIn: [Vijay Kumar Soni](https://www.linkedin.com/in/vijay-kumar-soni-3217b1309)

## 📄 License

Add a license file to the repository if you intend to distribute the project under a specific open-source license.

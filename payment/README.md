# 💳 Payment Management Service

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Backend-brightgreen?logo=springboot)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue?logo=mysql)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)

The **Payment Management Service** is a Spring Boot microservice responsible for processing payment operations, verifying payments, managing payment status, handling refunds, and processing payment gateway webhooks.

It includes payment gateway integrations for Razorpay and Stripe, along with payment administration and booking-service communication.

---

## 🔌 Port Configuration

| Property | Value |
|---|---|
| Service Name | Payment Management Service |
| Local URL | `http://localhost:8084` |

Configure the port in `application.yaml`:

```yaml
server:
  port: 8084
```

*Confirm that `8084` matches your actual application configuration.*

---

## ✨ Features

### Payment Management
- Create payment requests.
- Verify payment details.
- Track payment status.
- Handle payment attempts.
- Support payment retries where implemented.

### Payment Gateway Integration
- Razorpay gateway integration.
- Stripe gateway integration.
- Gateway selection through payment gateway components.
- Gateway-specific configuration and properties.

### Refund Management
- Process refund requests.
- Handle refund-related responses.
- Manage payment refund exceptions.

### Webhook Management
- Receive payment gateway webhook events.
- Process webhook operations through `WebhookServiceImpl`.
- Return webhook acknowledgment responses.

### Payment Administration
- Provide administrative payment operations.
- Support payment summaries and statistics where implemented.
- Handle payment-related administrative requests.

### Booking Integration
- Communicate with Booking Management through `BookingServiceClient`.
- Support booking payment-status updates.
- Handle payment confirmation and booking-related responses.

### Security
- JWT authentication components.
- Spring Security configuration.
- JWT authentication for Feign client communication.
- Centralized exception handling.

---

## 🛠 Technology Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate ORM
- MySQL
- Maven
- Spring Security
- JWT
- Spring Cloud OpenFeign
- Razorpay
- Stripe
- REST APIs
- Swagger / OpenAPI
- Docker

---

## 📁 Project Structure

The following structure is based on the actual directory listing you provided.

```text
payment
└── src
    └── main
        ├── java
        │   └── com
        │       └── project
        │           └── payment
        │               ├── PaymentApplication.java
        │               │
        │               ├── client
        │               │   └── BookingServiceClient.java
        │               │
        │               ├── config
        │               │   ├── OpenApiConfig.java
        │               │   ├── feign
        │               │   │   ├── FeignConfig.java
        │               │   │   └── JwtFeignInterceptor.java
        │               │   ├── payment
        │               │   │   ├── RazorpayConfig.java
        │               │   │   ├── RazorpayProperties.java
        │               │   │   ├── StripeConfig.java
        │               │   │   └── StripeProperties.java
        │               │   └── security
        │               │       ├── CustomUserPrincipal.java
        │               │       ├── JwtAuthenticationEntryPoint.java
        │               │       ├── JwtAuthenticationFilter.java
        │               │       ├── JwtProperties.java
        │               │       ├── JwtUtil.java
        │               │       ├── SecurityConfig.java
        │               │       └── SecurityUtils.java
        │               │
        │               ├── controller
        │               │   ├── PaymentAdminController.java
        │               │   ├── PaymentController.java
        │               │   └── WebhookController.java
        │               │
        │               ├── dto
        │               │   ├── common
        │               │   │   ├── ApiResponse.java
        │               │   │   ├── ErrorResponse.java
        │               │   │   ├── MessageResponse.java
        │               │   │   ├── PageResponse.java
        │               │   │   └── WebhookAckResponse.java
        │               │   ├── request
        │               │   │   ├── CreatePaymentReqDTO.java
        │               │   │   ├── RefundPaymentReqDTO.java
        │               │   │   ├── RetryPaymentReqDTO.java
        │               │   │   ├── UpdateBookingPaymentStatusReqDTO.java
        │               │   │   └── VerifyPaymentReqDTO.java
        │               │   └── response
        │               │       ├── BookingConfirmationResDTO.java
        │               │       ├── BookingValidationResDTO.java
        │               │       ├── PaymentAttemptResDTO.java
        │               │       ├── PaymentConfirmationResDTO.java
        │               │       ├── PaymentResDTO.java
        │               │       ├── PaymentStatisticsResDTO.java
        │               │       ├── PaymentStatusResDTO.java
        │               │       ├── PaymentSummaryResDTO.java
        │               │       └── RefundResponseDTO.java
        │               │
        │               ├── entity
        │               │   └── Payment.java
        │               │
        │               ├── enums
        │               │   ├── BookingStatus.java
        │               │   ├── CurrencyCode.java
        │               │   ├── PaymentGateway.java
        │               │   ├── PaymentMethod.java
        │               │   └── PaymentStatus.java
        │               │
        │               ├── exception
        │               │   ├── DuplicatePaymentException.java
        │               │   ├── GlobalExceptionHandler.java
        │               │   ├── InvalidPaymentStateException.java
        │               │   ├── PaymentAlreadyCompletedException.java
        │               │   ├── PaymentNotFoundException.java
        │               │   ├── PaymentProcessingException.java
        │               │   ├── PaymentRefundException.java
        │               │   └── PaymentVerificationException.java
        │               │
        │               ├── mapper
        │               │   └── PaymentMapper.java
        │               │
        │               ├── repository
        │               │   └── PaymentRepository.java
        │               │
        │               └── service
        │                   ├── PaymentAdminServiceImpl.java
        │                   ├── PaymentServiceImpl.java
        │                   ├── WebhookServiceImpl.java
        │                   ├── gateway
        │                   │   ├── GatewayFactory.java
        │                   │   ├── PaymentGatewayService.java
        │                   │   ├── RazorpayGateway.java
        │                   │   └── StripeGateway.java
        │                   └── interfaces
        │                       ├── PaymentAdminService.java
        │                       ├── PaymentService.java
        │                       └── WebhookService.java
        │
        └── resources
            └── application.yaml
├── pom.xml
├── Dockerfile
└── README.md
```

---

## 📦 Main Packages

| Package | Description |
|---|---|
| `client` | Communication with Booking Management |
| `config` | OpenAPI, payment gateway, Feign, and security configuration |
| `controller` | Payment, admin payment, and webhook endpoints |
| `dto` | Payment request, response, and common DTOs |
| `entity` | Payment entity |
| `enums` | Payment status, gateway, method, currency, and booking status |
| `exception` | Payment exceptions and global exception handling |
| `mapper` | Payment entity and DTO mapping |
| `repository` | Payment database operations |
| `service` | Payment, admin, and webhook business logic |
| `service/gateway` | Payment gateway interface, factory, and gateway implementations |
| `service/interfaces` | Payment service interfaces |

---

## 🗄 Main Entity

### `Payment.java`

Represents payment information managed by the service.

The entity's exact fields and database mappings are defined in `Payment.java`.

### Repository

`PaymentRepository.java` provides the persistence layer for payment operations.

---

## 🎮 Controllers

| Controller | Purpose |
|---|---|
| `PaymentController.java` | Payment-related API operations |
| `PaymentAdminController.java` | Administrative payment operations |
| `WebhookController.java` | Payment gateway webhook operations |

The exact API paths and HTTP methods are defined in the respective controller classes.

---

## 💰 Payment Gateway Integration

The project contains separate configuration and implementation classes for Razorpay and Stripe.

| Component | Class |
|---|---|
| Razorpay configuration | `RazorpayConfig.java` |
| Razorpay properties | `RazorpayProperties.java` |
| Razorpay implementation | `RazorpayGateway.java` |
| Stripe configuration | `StripeConfig.java` |
| Stripe properties | `StripeProperties.java` |
| Stripe implementation | `StripeGateway.java` |
| Gateway selection | `GatewayFactory.java` |
| Common gateway interface | `PaymentGatewayService.java` |

Gateway credentials and secrets should be supplied through environment variables or other secure configuration rather than committed to the repository.

---

## ⚙ Configuration

The main application configuration is located at:

```text
src/main/resources/application.yaml
```

It contains the application settings, database connection, security properties, and service URLs.

The payment gateway configuration is managed through:

- `RazorpayConfig.java`
- `RazorpayProperties.java`
- `StripeConfig.java`
- `StripeProperties.java`

Configure the required gateway credentials before testing payment operations.

---

## ▶️ How to Run

### Prerequisites

- Java 21
- Maven
- MySQL
- Required application configuration
- Payment gateway credentials for the gateway being tested

### Run Locally

Open a terminal in the `payment` directory:

```bash
mvn clean install
```

Start the service:

```bash
mvn spring-boot:run
```

Access the service using the port configured in `application.yaml`.

### Run with Docker

If the Dockerfile is configured:

```bash
docker build -t payment-management .
docker run --name payment-management -p 8084:8084 payment-management
```

Ensure the container port matches the application configuration and the database and Booking Management service URLs are accessible.

---

## 🐛 Troubleshooting

### Payment Creation Fails

- Verify the payment request data.
- Check the configured gateway credentials.
- Inspect the application logs.

### Payment Verification Fails

- Verify the gateway response and payment details.
- Check the verification request.
- Review the payment status and exception logs.

### Webhook Processing Fails

- Check the webhook endpoint configuration.
- Verify the gateway's webhook settings.
- Inspect webhook processing logs.

### Booking Status Is Not Updated

- Check `BookingServiceClient.java`.
- Verify the Booking Management service URL and availability.
- Review the Feign client response and error logs.

---

## 🚀 Future Enhancements

- Improve duplicate payment handling.
- Strengthen payment verification and error handling.
- Improve refund processing.
- Enhance webhook reliability.
- Add more payment integration tests.
- Improve payment logging and monitoring.

---

## 👨‍💻 Author

**Vijay Kumar Soni**

Java Backend Developer

**Skills:** Java, Spring Boot, Spring Security, Spring Data JPA, Hibernate, MySQL, REST APIs, Microservices, Maven, Docker.

---

⭐ If you find this project useful, please consider giving it a **Star**.
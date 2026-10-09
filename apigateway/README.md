# 🌐 API Gateway – Flight Booking System

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-brightgreen)
![Spring Cloud Gateway](https://img.shields.io/badge/Spring_Cloud_Gateway-Microservices-blue)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-success)
![Status](https://img.shields.io/badge/Status-Completed-success)

The API Gateway is the **single entry point** for client requests in the Flight Booking System. It routes incoming HTTP requests to the appropriate microservices and provides centralized request filtering, CORS configuration, and Swagger API documentation aggregation.

---

# ✨ Features

## Routing

- Centralized routing for all backend microservices
- User Management routing
- Flight Management routing
- Booking Management routing
- Payment Management routing
- Notification Management routing
- Separate routing for internal service endpoints

## Security

- JWT authentication filter
- Public endpoint handling
- Protected API request filtering
- Integration with Spring Security WebFlux
- Authorization header support

## Cross-Origin Resource Sharing (CORS)

- Configured frontend origin: `http://localhost:5173`
- Support for GET, POST, PUT, DELETE, and OPTIONS
- Authorization header exposure
- CORS preflight request handling

## API Documentation

- Aggregated Swagger UI
- OpenAPI documentation routing for all microservices
- Centralized API documentation access

## Environment Configuration

- Development profile using `localhost`
- Docker profile using container service names
- Configurable JWT properties
- Gateway routing debug logs

---

# 🛠 Tech Stack

- Java 21
- Spring Boot
- Spring Cloud Gateway
- Spring Security WebFlux
- JWT
- Springdoc OpenAPI / Swagger UI
- Maven
- Docker
- Docker Compose
- YAML Configuration

---

# 📁 Project Structure

```text
api-gateway
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── project
│       │           └── apigateway
│       │               ├── config
│       │               ├── filter
│       │               ├── security
│       │               └── util
│       └── resources
│           ├── application.yaml
│           ├── application-dev.yaml
│           └── application-docker.yaml
├── pom.xml
├── Dockerfile
└── README.md
```

*Note: Adjust the Java package folders and filenames to match your actual project structure.*

---

# 🏗 Microservices Architecture

The API Gateway forwards requests to the following services:

| Microservice | Port | Responsibility |
|---|---:|---|
| User Management | 8081 | Registration, login, authentication, and user management |
| Flight Management | 8082 | Flight search, schedules, instances, seats, airports, and airlines |
| Booking Management | 8083 | Booking operations and internal booking APIs |
| Payment Management | 8084 | Payment processing and payment webhooks |
| Notification Management | 8085 | Notification operations |
| API Gateway | 8080 | Centralized request routing |

### Request Flow

```text
React + Vite Frontend
         |
         ▼
    API Gateway
    localhost:8080
         |
    ┌────┼──────────┬──────────┬───────────┐
    ▼    ▼          ▼          ▼           ▼
  User  Flight    Booking    Payment   Notification
 Service Service  Service    Service     Service
  :8081  :8082     :8083      :8084       :8085
```

---

# 🔀 Routing Configuration

The gateway routes requests based on URL path predicates.

| Endpoint | Destination |
|---|---|
| `/api/public/**` | User Management |
| `/api/public/flights/**` | Flight Management |
| `/api/public/seats/**` | Flight Management |
| `/api/private/flights/**` | Flight Management |
| `/api/admin/flight-instances/**` | Flight Management |
| `/api/private/user/bookings/**` | Booking Management |
| `/api/private/admin/bookings/**` | Booking Management |
| `/api/internal/bookings/**` | Booking Management |
| `/api/private/payments/**` | Payment Management |
| `/api/admin/payments/**` | Payment Management |
| `/api/public/webhooks/**` | Payment Management |
| `/api/private/notifications/**` | Notification Management |
| `/api/private/admin/notifications/**` | Notification Management |
| `/api/internal/notifications/**` | Notification Management |

Additional Flight Management routes cover aircrafts, airlines, airports, baggage policies, flight amenities, flight fares, and flight status information.

**Important:** Keep specific routes such as `/api/public/flights/**` ahead of a broad `/api/public/**` User Management route so flight requests reach the correct service.

---

# 🔐 Security

The API Gateway integrates with Spring Security WebFlux and a custom JWT authentication filter.

### Authentication Flow

```text
Client Request
      |
      ▼
   API Gateway
      |
      ▼
 JWT Filter
      |
      ├── Public Request
      |       |
      |       ▼
      |   Route Request
      |
      └── Protected Request
              |
              ▼
        Validate JWT
              |
              ▼
        Apply Security Rules
              |
              ▼
        Forward Request
```

Public routes such as login and registration do not require an existing access token. Protected requests are handled according to the gateway filter and security configuration.

Backend microservices should also enforce their own authentication and authorization rules where appropriate.

---

# 🌍 CORS Configuration

The gateway is configured to allow requests from the React + Vite frontend.

**Frontend origin:**

```text
http://localhost:5173
```

Supported HTTP methods:

- GET
- POST
- PUT
- DELETE
- OPTIONS

The configuration also supports request headers, exposes the `Authorization` header, allows credentials, and configures preflight caching.

For production deployment, configure the actual frontend domain instead of the localhost development origin.

---

# 📚 Swagger API Documentation

The API Gateway aggregates OpenAPI documentation from the backend microservices.

### Swagger UI

```text
http://localhost:8080/swagger-ui.html
```

### Configured API Documentation

| Microservice | OpenAPI URL |
|---|---|
| User Management | `/user/v3/api-docs` |
| Flight Management | `/flight/v3/api-docs` |
| Booking Management | `/booking/v3/api-docs` |
| Payment Management | `/payment/v3/api-docs` |
| Notification Management | `/notification/v3/api-docs` |

Swagger access requires the gateway documentation routes and the corresponding backend OpenAPI endpoints to be configured and reachable.

---

# ⚙ Configuration

The gateway uses separate configurations for local development and Docker deployment.

## Development Profile

Backend services are accessed through `localhost`.

```text
User Management       → localhost:8081
Flight Management     → localhost:8082
Booking Management    → localhost:8083
Payment Management    → localhost:8084
Notification Management → localhost:8085
```

Activate the development profile:

```bash
SPRING_PROFILES_ACTIVE=dev
```

## Docker Profile

Backend services are accessed through Docker Compose service names.

```text
User Management       → user-management:8081
Flight Management     → flight-management:8082
Booking Management    → booking-management:8083
Payment Management   → payment-management:8084
Notification Management → notification-management:8085
```

Activate the Docker profile:

```bash
SPRING_PROFILES_ACTIVE=docker
```

Docker hostnames must match the actual Compose service names or network aliases, and the containers must share a Docker network.

## Environment Variables

| Variable | Description |
|---|---|
| `SPRING_PROFILES_ACTIVE` | Active configuration profile |
| `JWT_SECRET` | JWT signing secret |
| `JWT_EXPIRATION_TIME` | Access-token expiration in milliseconds |
| `JWT_REFRESH_EXPIRATION_TIME` | Refresh-token expiration in milliseconds |

Default expiration values in the example configuration:

- Access token: 30 minutes
- Refresh token: 7 days

Keep production secrets in environment variables or a secret manager. Do not commit real credentials to GitHub.

---

# ▶ Running the Service

## Prerequisites

- Java 21
- Maven
- Backend microservices configured on their expected ports
- Docker and Docker Compose for containerized execution

## Run Locally

1. Start the required backend microservices.
2. Activate the `dev` profile.
3. Configure the required environment variables.
4. Navigate to the API Gateway project directory.
5. Run the application.

Using Maven:

```bash
mvn spring-boot:run
```

Using Maven Wrapper on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The API Gateway runs on port `8080`.

## Run with Docker Compose

From the directory containing your Docker Compose file:

```bash
docker compose up -d --build
```

Check running services:

```bash
docker compose ps
```

View gateway logs:

```bash
docker compose logs -f api-gateway
```

Use the actual Compose service name if it differs from `api-gateway`.

---

# 🧪 Testing the Gateway

### Login API

```http
POST http://localhost:8080/api/public/login
Content-Type: application/json
```

Example request:

```json
{
  "identifier": "user@example.com",
  "password": "your-password"
}
```

The request should be forwarded to the User Management service.

Other gateway routes can be tested through the corresponding flight, booking, payment, and notification endpoints.

---

# 🐛 Troubleshooting

### UnknownHostException

If the gateway reports an error such as:

```text
Failed to resolve 'user-management'
```

Check the active profile.

- Use `localhost` when the services run directly from the IDE.
- Use the actual Docker service name when the services run in Docker.
- Verify that the gateway and backend containers share a Docker network.

### HTTP 500 Error

Inspect the gateway logs for DNS resolution errors, connection failures, and downstream-service errors. An HTTP 500 does not necessarily mean the backend controller is responsible.

### HTTP 401 / 403

Check JWT validation, public endpoint exclusions, Spring Security rules, and role-based authorization.

### CORS Error

Verify the frontend origin, permitted methods, request headers, and preflight request handling.

### Swagger UI Error

Confirm that the documentation routes are configured and the corresponding backend OpenAPI endpoints are accessible.

---

# 🚀 Future Enhancements

- Rate limiting
- Distributed tracing and correlation IDs
- Enhanced monitoring and health checks
- Production TLS configuration
- Improved downstream failure handling
- Automated gateway integration tests
- Centralized metrics and observability

---

# 👨‍💻 Author

**Vijay Kumar Soni**

Java Backend Developer

### Skills

- Java
- Spring Boot
- Spring Cloud Gateway
- Spring Security
- JWT Authentication
- REST APIs
- MySQL
- Microservices
- Docker

---

⭐ If you find this project useful, please consider giving it a **Star**.

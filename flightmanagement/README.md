# ✈️ Flight Management Service

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Backend-brightgreen?logo=springboot)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue?logo=mysql)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)

The **Flight Management Service** is a Spring Boot microservice responsible for managing flight information, airlines, airports, aircraft, flight schedules, flight instances, fares, seat information, and flight availability.

It provides REST APIs for managing flight data and searching for publicly available flights.

---

## 🔌 Port Configuration

| Property | Value |
|---|---|
| Service Name | Flight Management Service |
| Local URL | `http://localhost:8082` |
| API Gateway Port | `8080` |

Configure the port in `application.yaml` according to your actual application settings.

```yaml
server:
  port: 8082
```

*Note: Confirm that `8082` matches your current configuration.*

---

## ✨ Features

### Flight Management
- Create and manage flight information.
- Manage airline and airport details.
- Maintain aircraft information.
- Manage flight schedules and flight instances.
- Manage flight fares and flight status information.

### Seat Management
- Manage seat information.
- Track seat availability.
- Handle seat reservation requests.
- Maintain seat reservation status.

### Public Flight Search
- Search for flights using search request DTOs.
- Retrieve flight details.
- Provide flight information and seat availability through response DTOs.

### Additional Management
- Manage baggage policies.
- Manage flight amenities.
- Process scheduled flight-instance operations.
- Process scheduled flight-status updates.

### Security
- JWT authentication components.
- Spring Security configuration.
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
- REST APIs
- Swagger / OpenAPI
- Docker

---

## 📁 Project Structure

```text
flightmanagement
└── src
    └── main
        ├── java
        │   └── com
        │       └── flightmanagement
        │           └── flightmanagement
        │               ├── FlightmanagementApplication.java
        │               │
        │               ├── config
        │               │   ├── SwaggerConfig.java
        │               │   └── security
        │               │       ├── JwtProperties.java
        │               │       └── SecurityConfig.java
        │               │
        │               ├── controller
        │               │   ├── AircraftController.java
        │               │   ├── AirlineController.java
        │               │   ├── AirportController.java
        │               │   ├── BaggagePolicyController.java
        │               │   ├── FlightAmenityController.java
        │               │   ├── FlightController.java
        │               │   ├── FlightFareController.java
        │               │   ├── FlightInstanceController.java
        │               │   ├── FlightScheduleController.java
        │               │   ├── FlightStatusInfoController.java
        │               │   ├── booking
        │               │   │   └── FlightBookingController.java
        │               │   └── publicFlight
        │               │       ├── PublicFlightController.java
        │               │       └── SeatController.java
        │               │
        │               ├── dtos
        │               │   ├── requestDTOs
        │               │   │   ├── AircraftReqDTO.java
        │               │   │   ├── AirlineReqDTO.java
        │               │   │   ├── AirportReqDTO.java
        │               │   │   ├── AutoSeatReservationReqDTO.java
        │               │   │   ├── BaggagePolicyReqDTO.java
        │               │   │   ├── FlightAmenityReqDTO.java
        │               │   │   ├── FlightFareReqDTO.java
        │               │   │   ├── FlightFareUpdateReqDTO.java
        │               │   │   ├── FlightReqDTO.java
        │               │   │   ├── FlightScheduleRequest.java
        │               │   │   ├── FlightStatusInfoReqDTO.java
        │               │   │   ├── FlightStatusInfoUpdateReqDTO.java
        │               │   │   ├── SeatReqDTO.java
        │               │   │   ├── SeatReservationReqDTO.java
        │               │   │   └── publicFlight
        │               │   │       └── FlightSearchRequestDTO.java
        │               │   ├── responseDTOs
        │               │   │   ├── AircraftResDTO.java
        │               │   │   ├── AirlineResDTO.java
        │               │   │   ├── AirportResDTO.java
        │               │   │   ├── BaggagePolicyResDTO.java
        │               │   │   ├── FlightAmenityResDTO.java
        │               │   │   ├── FlightDetailsResDTO.java
        │               │   │   ├── FlightFareResDTO.java
        │               │   │   ├── FlightResDTO.java
        │               │   │   ├── FlightScheduleResponseDTO.java
        │               │   │   ├── FlightStatusInfoResDTO.java
        │               │   │   ├── PublicFlightResDTO.java
        │               │   │   ├── SeatAvailabilityResDTO.java
        │               │   │   ├── SeatResDTO.java
        │               │   │   └── SeatReservationResponse.java
        │               │   └── securityDTOs
        │               │       └── ApiResponse.java
        │               │
        │               ├── entity
        │               │   ├── Aircraft.java
        │               │   ├── Airline.java
        │               │   ├── Airport.java
        │               │   ├── BaggagePolicy.java
        │               │   ├── Flight.java
        │               │   ├── FlightAmenity.java
        │               │   ├── FlightFare.java
        │               │   ├── FlightInstance.java
        │               │   ├── FlightInstanceSeat.java
        │               │   ├── FlightSchedule.java
        │               │   ├── FlightStatusInfo.java
        │               │   └── Seat.java
        │               │
        │               ├── enums
        │               │   ├── CabinClass.java
        │               │   ├── CurrencyCode.java
        │               │   ├── FlightStatus.java
        │               │   ├── FlightType.java
        │               │   ├── FrequencyType.java
        │               │   └── SeatStatus.java
        │               │
        │               ├── exception
        │               │   ├── AircraftNotFoundException.java
        │               │   ├── FlightNotFoundException.java
        │               │   ├── GlobalExceptionHandler.java
        │               │   ├── ResourceAlreadyExistsException.java
        │               │   ├── ResourceNotFoundException.java
        │               │   └── SeatAlreadyBookedException.java
        │               │
        │               ├── mapper
        │               │   ├── AircraftMapper.java
        │               │   ├── AirlineMapper.java
        │               │   ├── AirportMapper.java
        │               │   ├── BaggagePolicyMapper.java
        │               │   ├── FlightAmenityMapper.java
        │               │   ├── FlightFareMapper.java
        │               │   ├── FlightMapper.java
        │               │   ├── FlightScheduleMapper.java
        │               │   ├── FlightStatusInfoMapper.java
        │               │   ├── PublicFlightMapper.java
        │               │   └── SeatMapper.java
        │               │
        │               ├── repository
        │               │   ├── AircraftRepository.java
        │               │   ├── AirlineRepository.java
        │               │   ├── AirportRepository.java
        │               │   ├── BaggageRepository.java
        │               │   ├── FlightAmenityRepository.java
        │               │   ├── FlightFareRepository.java
        │               │   ├── FlightInstanceRepository.java
        │               │   ├── FlightInstanceSeatRepository.java
        │               │   ├── FlightRepository.java
        │               │   ├── FlightScheduleRepository.java
        │               │   ├── FlightStatusInfoRepository.java
        │               │   └── SeatRepository.java
        │               │
        │               ├── scheduler
        │               │   ├── FlightInstanceScheduler.java
        │               │   └── FlightStatusScheduler.java
        │               │
        │               ├── security
        │               │   ├── JwtAuthenticationFilter.java
        │               │   └── JwtUtil.java
        │               │
        │               └── service
        │                   ├── implementation
        │                   ├── interFace
        │                   │   └── publicService
        │                   └── publicImpl
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
| `config` | Swagger and security configuration |
| `controller` | Flight, airline, airport, schedule, fare, seat, and other REST controllers |
| `dtos` | Request and response data transfer objects |
| `entity` | Flight-related JPA entities |
| `enums` | Flight status, seat status, cabin class, and related enumerations |
| `exception` | Custom exceptions and global exception handling |
| `mapper` | Mapping between entities and DTOs |
| `repository` | Database access interfaces |
| `scheduler` | Scheduled flight-instance and flight-status operations |
| `security` | JWT authentication components |
| `service` | Service interfaces and implementations |

---

## 🗄 Main Entities

| Entity | Purpose |
|---|---|
| `Aircraft` | Aircraft information |
| `Airline` | Airline information |
| `Airport` | Airport information |
| `Flight` | Flight information |
| `FlightSchedule` | Flight scheduling information |
| `FlightInstance` | Flight instance for a particular date and time |
| `FlightFare` | Flight fare information |
| `FlightStatusInfo` | Flight status information |
| `Seat` | Seat information |
| `FlightInstanceSeat` | Seat information associated with a flight instance |
| `BaggagePolicy` | Baggage policy information |
| `FlightAmenity` | Flight amenity information |

---

## 🎮 Controllers

The service includes controllers for:

- Aircraft management
- Airline management
- Airport management
- Flight management
- Flight fares
- Flight schedules
- Flight instances
- Flight status information
- Baggage policies
- Flight amenities
- Flight booking operations
- Public flight search and seat operations

The exact endpoints and HTTP methods are defined in the controller classes.

---

## ⚙ Configuration

The main configuration file is:

```text
src/main/resources/application.yaml
```

Configure the application port, database connection, JPA settings, security properties, and any other properties required by your implementation.

Do not commit database passwords or JWT secrets to version control.

---

## ▶️ How to Run

### Prerequisites

- Java 21
- Maven
- MySQL
- Required application configuration

### Run Locally

Open a terminal in the `flightmanagement` directory:

```bash
mvn clean install
```

Start the application:

```bash
mvn spring-boot:run
```

Use the port configured in `application.yaml` to access the service.

### Run with Docker

If the Dockerfile is configured:

```bash
docker build -t flight-management .
docker run --name flight-management -p 8082:8082 flight-management
```

Ensure the container port matches the application's configured port and that the database settings are correct.

---

## 👨‍💻 Author

**Vijay Kumar Soni**

Java Backend Developer

**Skills:** Java, Spring Boot, Spring Data JPA, Hibernate, MySQL, REST APIs, Microservices, Maven, Docker.

---

⭐ If you find this project useful, please consider giving it a **Star**.
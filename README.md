![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.0-brightgreen?style=for-the-badge&logo=spring-boot)
![Java](https://img.shields.io/badge/Java-17-blue?style=for-the-badge&logo=java)
![Maven](https://img.shields.io/badge/Maven-Build-orange?style=for-the-badge&logo=apache-maven)

# OTP Microservice

## Overview
The OTP Microservice is a scalable, robust backend application designed to handle the generation, delivery, and validation of One-Time Passwords (OTPs). Built with Spring Boot, this service abstracts the complexity of multi-channel notifications (Email, SMS, WhatsApp) and provides a secure mechanism for user verification. It implements a resilient caching layer for OTP lifecycle management, enforcing expiration (TTL) and retry limits to prevent brute-force attacks.

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Redis Usage & TTL Handling](#redis-usage--ttl-handling)
- [Design Decisions](#design-decisions)
- [Error Handling](#error-handling)
- [Environment Variables](#environment-variables)
- [Setup Instructions](#setup-instructions)
- [Future Improvements](#future-improvements)
- [Learning Outcomes](#learning-outcomes)
- [Author](#author)

## Features
- **Multi-Channel Delivery System**: Architected to support Email, SMS, and WhatsApp (Email currently implemented).
- **Secure OTP Generation**: Uses `ThreadLocalRandom` for thread-safe, unpredictable 6-digit OTP generation.
- **TTL & Lifecycle Management**: Configurable expiration times for OTPs with automatic invalidation.
- **Brute-Force Protection**: Restricts validation attempts (max 3 tries) before invalidating the OTP.
- **Design Pattern Driven**: Utilizes Strategy and Factory design patterns for clean abstraction.
- **Global Error Handling**: Centralized exception management for consistent API error responses.
- **Input Validation**: Leverages Jakarta Bean Validation for robust request payload checking.

## Tech Stack
- **Framework**: Spring Boot 3.5.0
- **Language**: Java 17
- **Build Tool**: Maven
- **Database/ORM**: H2 Database, Spring Data JPA (configured for future persistent state)
- **Email Service**: Spring Boot Starter Mail (JavaMailSender)
- **Validation**: Hibernate Validator, Jakarta Validation API

## Architecture
The project strictly follows a **Layered Architecture** with elements of **Clean Architecture** principles, ensuring a clear separation of concerns:

1. **Controller Layer** (`HomeController`): Acts as the entry point, handling HTTP requests, input validation, and HTTP responses.
2. **Factory Layer** (`ServiceFactory`): Implements the Factory pattern to dynamically route requests to the appropriate notification channel strategy.
3. **Service Layer** (`OTPService`, `MailService`): Contains business logic for generating OTPs, interacting with the storage, and sending emails.
4. **Data/Cache Layer** (`Store`): Manages the state, persistence, and TTL of the generated OTPs.

**Request Flow**:
`Client Request` -> `HomeController` -> `ServiceFactory` -> `OTPService` -> `MailService` (Dispatch) & `Store` (Cache)

## Project Structure
```text
src/main/java/com/junnu/app/
├── AppApplication.java          # Spring Boot Application Entry Point
├── controller/                  # API routing and exception handling
│   ├── ErrorHandler.java        # @RestControllerAdvice for global error handling
│   └── HomeController.java      # Main REST controller
├── dto/                         # Data Transfer Objects
│   └── NotificationRequest.java # Payload for sending OTP
├── notification/                # Notification handling and strategy patterns
│   ├── MailService.java         # Email delivery implementation
│   ├── NotificationChannel.java # Enum for EMAIL, SMS, WHATSAPP
│   ├── NotificationInterface.java # Base interface for notification strategies
│   └── ServiceFactory.java      # Factory to retrieve the correct strategy bean
└── otp/                         # OTP domain logic and storage
    ├── OTPService.java          # Business logic for OTP generation/saving
    ├── OtpData.java             # POJO representing OTP state, TTL, and retries
    ├── OtpGenerator.java        # Thread-safe OTP generator
    └── Store.java               # In-memory Redis-like cache storage
```

## API Endpoints

| Method | Endpoint       | Purpose                                      |
|--------|----------------|----------------------------------------------|
| `POST` | `/sendotp`     | Generates and dispatches an OTP to a target. |
| `POST` | `/validateotp` | Validates a submitted OTP for a user.        |
| `GET`  | `/allotps`     | Retrieves all active OTPs (Admin/Debug).     |

### 1. Send OTP
**Endpoint**: `/sendotp`  
**Method**: `POST`  
**Purpose**: Generates a 6-digit OTP and sends it via the specified channel.

**Request Payload**:
```json
{
  "target": "user@example.com",
  "channel": "EMAIL"
}
```

**Response (202 Accepted)**:
```text
MAIL WILL BE SENT SHORTLY..
```

### 2. Validate OTP
**Endpoint**: `/validateotp`  
**Method**: `POST`  
**Purpose**: Validates the provided OTP against the stored active OTP. Handles max retries and expiration.

**Request Payload**:
```json
{
  "user": "user@example.com",
  "otp": "123456"
}
```

**Success Response (200 OK)**:
```text
OTP is valid
```

**Error Response (400 Bad Request)**:
```text
OTP is invalid or expired
```

### 3. Get All OTPs (Debug)
**Endpoint**: `/allotps`  
**Method**: `GET`  
**Purpose**: Returns all currently cached OTP entries.

## Redis Usage & TTL Handling
While the service utilizes an in-memory `ConcurrentHashMap` within the `Store` class as the underlying data structure, it is architected to **simulate Redis behavior** (and is referred to conceptually as `redis` within the codebase). This setup allows for a seamless future migration to a distributed Redis cache.

**Implementation Details**:
- **TTL (Time to Live)**: Each OTP is assigned an `expiry` timestamp upon creation based on `otp.DEFAULT_EXPIRY` (default 5 minutes). During validation, the timestamp is checked. Expired OTPs are rejected and automatically evicted from the cache.
- **Rate Limiting / Retry Limits**: The store tracks the number of `tries`. If a user enters an incorrect OTP 3 times, the OTP is forcefully invalidated and evicted to prevent brute-force attacks.
- **Thread Safety**: `ConcurrentHashMap` ensures safe concurrent reads and writes across multiple threads handling incoming HTTP requests.

## Design Decisions
- **Strategy & Factory Patterns**: By creating a `NotificationInterface` and a `ServiceFactory`, the system avoids tightly coupling the controller to specific delivery methods. Adding an `SMS` provider simply requires creating a new class implementing `NotificationInterface` and annotating it with `@Service("SMS")`.
- **In-Memory Cache vs DB**: For OTPs, low latency and automatic expiration are crucial. An in-memory key-value store (simulating Redis) is favored over relational DB persistence to prevent I/O bottlenecks.
- **`ThreadLocalRandom` over `Math.random()`**: Used for OTP generation to ensure high performance and thread safety in a highly concurrent environment.
- **Fail-Fast Validation**: Jakarta Bean Validation is used at the controller level to instantly reject bad requests before they reach the service layer.

## Error Handling
The application uses a centralized `@RestControllerAdvice` (`ErrorHandler.java`) to intercept exceptions and return structured, consistent JSON error responses.

- **`IllegalArgumentException`**: Mapped to `400 Bad Request`.
- **`RuntimeException`**: Mapped to `500 Internal Server Error`.

**Example Error Response**:
```json
{
  "error": "Failed to send email"
}
```

## Environment Variables
The application requires specific environment variables or application properties to function correctly. 

Configure these in `src/main/resources/application.properties` or inject them as environment variables:

| Variable | Description | Default |
|----------|-------------|---------|
| `spring.mail.username` | The SMTP email address used to send OTPs. | `junnubest@gmail.com` |
| `smtp` | The App Password for the SMTP email address. | *Requires setup* |
| `otp.DEFAULT_EXPIRY` | The time-to-live for an OTP in minutes. | `5` |

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository_url>
cd OTP_MICROSERVICE/app
```

### 2. Environment Setup
Update the `application.properties` file with your SMTP credentials. If using Gmail, you must generate an **App Password**.
```properties
spring.mail.username=your-email@gmail.com
# Pass the SMTP password as an environment variable or hardcode for local testing
spring.mail.password=${smtp} 
```
Set the `smtp` environment variable in your terminal:
```bash
export smtp="your_app_password_here"
```

### 3. Application Startup
Use Maven wrapper to build and run the application:
```bash
./mvnw spring-boot:run
```
The application will start on `http://localhost:8080`.

## Future Improvements
1. **True Redis Integration**: Replace the `ConcurrentHashMap` store with a true distributed `RedisTemplate` to support horizontal scaling across multiple instances.
2. **Asynchronous Processing**: Annotate the `handleRequest` and mail sending methods with `@Async` or push messages to a message broker (RabbitMQ/Kafka) to decouple the HTTP request thread from the latency of SMTP servers.
3. **Implement Additional Channels**: Implement `NotificationInterface` for `SMS` (e.g., Twilio) and `WHATSAPP`.
4. **Enhanced Security**: Add rate limiting by IP to prevent abuse of the `/sendotp` endpoint.

## Learning Outcomes
- Implementing the **Factory and Strategy Design Patterns** in Spring Boot using `@Service` bean naming.
- Managing fast-expiring data structures and simulating **cache evictions and TTLs**.
- Creating robust, centralized API error handling using `@RestControllerAdvice`.
- Integrating external SMTP servers for automated email dispatch.
- Structuring a microservice for extensibility and clean separation of concerns.

## Author
**Junaidarmaan**
- GitHub: [Junaidarmaan](https://github.com/Junaidarmaan)

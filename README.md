# Employee Nexus API

A Spring Boot REST API for employee management with optimistic locking for concurrent modifications.

## Technologies

- Java 17
- Spring Boot 3.3.9
- Spring Data JPA
- H2 Database
- Spring Security for Basic Authentication
- JUnit 5 & Mockito for testing
- OpenAPI (Swagger) for API documentation
- Lombok for reducing boilerplate code

## Features

- CRUD operations for employee management
- Basic Authentication security
- Optimistic locking to handle concurrent modifications
- In-memory H2 database
- RESTful API endpoints
- Global exception handling
- Validation for employee data
- OpenAPI documentation with custom configuration
- Comprehensive test coverage
- Paginated responses with metadata

## Prerequisites

- Java 17 or higher
- Maven 3.9.x (or use included Maven wrapper)

## Getting Started

1. Clone the repository:
```bash
git clone [repository-url]
```

2. Navigate to project directory:
```bash
cd employee-nexus-api
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## Authentication

The API uses Basic Authentication:
- Username: `admin`
- Password: `password`

Include these credentials in the Authorization header for all API requests:
```
Authorization: Basic YWRtaW46cGFzc3dvcmQ=
```

## Database

The application uses H2 in-memory database:
- JDBC URL: `jdbc:h2:mem:employeedb`
- Username: `sa`
- Password: `password`
- H2 Console: `http://localhost:8080/h2-console`

## API Documentation

The API documentation is available through Swagger UI:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

Note: Authentication is required to access the API documentation.

## API Endpoints

| Method | URL | Description | Request Parameters | Response Format |
|--------|-----|-------------|-------------------|-----------------|
| GET | `/api/v1/employees` | Get all employees | `page` (default: 0), `size` (default: 10), `sort` (default: id,asc) | Paginated response with metadata |
| GET | `/api/v1/employees/{id}` | Get employee by ID | N/A | Single employee |
| POST | `/api/v1/employees` | Create new employee | N/A | Created employee |
| PUT | `/api/v1/employees/{id}` | Update employee | N/A | Updated employee |
| DELETE | `/api/v1/employees/{id}` | Delete employee | N/A | No content |

### Authentication Headers
All requests must include Basic Authentication headers:
```
Authorization: Basic YWRtaW46cGFzc3dvcmQ=
```

### Sort Parameters
- Format: `property,direction`
- Valid properties: `id`, `name`, `department`
- Valid directions: `asc`, `desc`
- Example: `sort=name,desc`

### Error Responses

The API includes comprehensive error handling:
- 400 Bad Request: Validation errors or invalid sort parameters
- 401 Unauthorized: Missing or invalid authentication
- 403 Forbidden: Insufficient permissions
- 404 Not Found: Resource not found
- 409 Conflict: Concurrent modification detected

## Testing

Run tests using:
```bash
./mvnw test
```

### Postman Collection

A Postman collection is available in the `postman` directory for testing all API endpoints. The collection includes:
- Pre-configured authentication
- CRUD operations
- Validation test cases
- Error scenarios
- Environment variables

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/bmo/
│   │       ├── config/
│   │       │   └── SecurityConfig.java
│   │       ├── controller/
│   │       ├── exception/
│   │       ├── model/
│   │       └── service/
│   └── resources/
│       └── application.yml
└── test/
    └── java/
        └── com/bmo/
            ├── controller/
            └── service/
```

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details

## Contact

- Developer: Aarif Diwan
- Email: aarif.diwan@gmail.com
- GitHub: https://github.com/aarifdiwan-hub

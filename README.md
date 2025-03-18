# Employee Nexus API

A Spring Boot REST API for employee management with optimistic locking for concurrent modifications.

## Technologies

- Java 17
- Spring Boot 3.3.9
- Spring Data JPA
- H2 Database
- JUnit 5 & Mockito for testing
- OpenAPI (Swagger) for API documentation
- Lombok for reducing boilerplate code

## Features

- CRUD operations for employee management
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

## Database

The application uses H2 in-memory database:
- JDBC URL: `jdbc:h2:mem:employeedb`
- Username: `sa`
- Password: `password`

## API Documentation

The API documentation is available through Swagger UI:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## API Endpoints

| Method | URL | Description | Request Parameters | Response Format |
|--------|-----|-------------|-------------------|-----------------|
| GET | `/api/employees` | Get all employees | `page` (default: 0), `size` (default: 10), `sort` (default: id,asc) | Paginated response with metadata |
| GET | `/api/employees/{id}` | Get employee by ID | N/A | Single employee |
| POST | `/api/employees` | Create new employee | N/A | Created employee |
| PUT | `/api/employees/{id}` | Update employee | N/A | Updated employee |
| DELETE | `/api/employees/{id}` | Delete employee | N/A | No content |

### Pagination Response Format

```json
{
  "content": [
    {
      "id": 1,
      "name": "Example Name",
      "department": "Example Dept",
      "version": 1
    }
  ],
  "metadata": {
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 1,
    "totalPages": 1,
    "first": true,
    "last": true
  }
}
```

### Sort Parameters
- Format: `property,direction`
- Valid properties: `id`, `name`, `department`
- Valid directions: `asc`, `desc`
- Example: `sort=name,desc`

### Error Responses

The API includes comprehensive error handling:
- 400 Bad Request: Validation errors or invalid sort parameters
- 404 Not Found: Resource not found
- 409 Conflict: Concurrent modification detected

## Testing

Run tests using:
```bash
./mvnw test
```

### Postman Collection

A Postman collection is available in the `postman` directory for testing all API endpoints:
- CRUD operations
- Validation test cases
- Error scenarios

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/bmo/
│   │       ├── config/
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

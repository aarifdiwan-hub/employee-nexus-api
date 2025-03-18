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
- Automated semantic versioning and release management

## Semantic Versioning

This project follows [Semantic Versioning 2.0.0](https://semver.org/) for version management and [Conventional Commits](https://www.conventionalcommits.org/) for commit messages. Version numbers follow the MAJOR.MINOR.PATCH format.

The version is managed through the `revision` property in pom.xml and is automatically updated by semantic-release based on commit messages.

Versioning is automated using semantic-release, which:
1. Analyzes commit messages
2. Determines version number
3. Generates CHANGELOG.md
4. Creates GitHub release
5. Publishes artifacts

For detailed information about commit types (`feat`, `fix`, `BREAKING CHANGE`, etc.) and versioning rules, see our [Versioning Guide](https://www.conventionalcommits.org/en/v1.0.0/#specification).

## Prerequisites

- Java 17 or higher
- Maven 3.9.x (or use included Maven wrapper)

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/aarifdiwan-hub/employee-nexus-api.git
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

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/bmo/employeenexus/
│   │       ├── config/
│   │       │   ├── OpenApiConfig.java
│   │       │   └── SecurityConfig.java
│   │       ├── controller/
│   │       │   └── EmployeeController.java
│   │       ├── dto/
│   │       │   ├── EmployeeDto.java
│   │       │   └── PageResponseDto.java
│   │       ├── exception/
│   │       │   ├── GlobalExceptionHandler.java
│   │       │   └── ResourceNotFoundException.java
│   │       ├── mapper/
│   │       │   └── EmployeeMapper.java
│   │       ├── model/
│   │       │   └── Employee.java
│   │       ├── repository/
│   │       │   └── EmployeeRepository.java
│   │       ├── service/
│   │       │   ├── EmployeeService.java
│   │       │   └── EmployeeServiceImpl.java
│   │       └── EmployeeNexusApplication.java
│   └── resources/
│       ├── application.yml
│       └── logback-spring.xml
└── test/
    └── java/
        └── com/bmo/employeenexus/
            ├── controller/
            │   └── EmployeeControllerTest.java
            ├── service/
            │   └── EmployeeServiceTest.java
            └── EmployeeNexusApplicationTests.java
```

## Authentication

The API uses Basic Authentication:
- Username: `admin`
- Password: `password`


## Database

The application uses H2 in-memory database:
- JDBC URL: `jdbc:h2:mem:employeedb`
- Username: `sa`
- Password: `password`

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

## Error Responses

The API includes comprehensive error handling:
- 400 Bad Request: Validation errors or invalid sort parameters
- 401 Unauthorized: Missing or invalid authentication
- 403 Forbidden: Insufficient permissions
- 404 Not Found: Resource not found
- 409 Conflict: Concurrent modification detected during update operations

## Testing

Run tests using:
```bash
./mvnw test
```
## CI/CD

The project uses GitHub Actions for:
- Building and testing on pull requests
- Automated releases on main branch
- Semantic versioning
- Changelog generation
- Artifact publishing


## Postman Collection

A Postman collection is available in the `postman` directory for testing all API endpoints. The collection includes:
- Pre-configured authentication
- CRUD operations
- Validation test cases
- Error scenarios
- Environment variables

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details

## Contact

- Developer: Aarif Diwan
- Email: aarif.diwan@gmail.com
- GitHub: https://github.com/aarifdiwan-hub

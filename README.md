# Employee Nexus API

A Spring Boot REST API for employee management with optimistic locking for concurrent modifications.

## Technologies

- Java 17
- Spring Boot 3.3.9
- Spring Data JPA
- H2 Database
- JUnit 5 & Mockito for testing

## Features

- CRUD operations for employee management
- Optimistic locking to handle concurrent modifications
- In-memory H2 database
- RESTful API endpoints
- Global exception handling

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
- Console URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:employeedb`
- Username: `sa`
- Password: `password`

## API Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/employees` | Get all employees |
| GET | `/api/employees/{id}` | Get employee by ID |
| POST | `/api/employees` | Create new employee |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |

### Request/Response Examples

#### Create Employee
```json
POST /api/employees
{
    "name": "Aarif Diwan",
    "department": "Engineering"
}
```

#### Update Employee
```json
PUT /api/employees/1
{
    "name": "Aarif Diwan",
    "department": "IT"
}
```

## Testing

Run tests using:
```bash
./mvnw test
```

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details

## Additional Documentation

For detailed information about project structure, error handling, and contribution guidelines, please see [HELP.md](HELP.md).

# library-management-api

REST API for managing library users, books, copies and loans.

## Technologies

- Java 17
- Spring Boot 3.5.4
- Maven
- PostgreSQL 16
- Docker & Docker Compose
- Spring Data JPA
- Bean Validation
- Spring Doc OpenAPI (Swagger)
- Spring Boot Actuator

## Requirements

- JDK 17
- Docker & Docker Compose (for containerized environment)
- Maven (optional, wrapper included)

## Environment Variables

| Variable               | Description                    | Default                                           |
|------------------------|--------------------------------|---------------------------------------------------|
| `DB_URI`               | PostgreSQL JDBC URL            | `jdbc:postgresql://database:5432/library_db`      |
| `DB_USER`              | Database user                  | `library_user`                                    |
| `DB_PASSWORD`          | Database password              | `library_password`                                |
| `DB_DRIVER`            | JDBC driver class              | `org.postgresql.Driver`                           |
| `API_PORT`             | Application HTTP port          | `8080`                                            |
| `DB_PORT`              | PostgreSQL port                | `5432`                                            |
| `CORS_ALLOWED_ORIGINS` | Allowed CORS origins           | `http://localhost:3000`                           |

Copy `.env.example` to `.env` and adjust values as needed.

## Running with Docker

```bash
docker compose --env-file .env up --build -d
```

## Access

- **API:** http://localhost:${API_PORT}
- **Swagger UI:** http://localhost:${API_PORT}/swagger-ui/index.html
- **Health Check:** http://localhost:${API_PORT}/actuator/health
- **Status:** http://localhost:${API_PORT}/api/v1/status

## Local Development

```bash
./mvnw clean test
./mvnw clean package -DskipTests
./mvnw spring-boot:run
```

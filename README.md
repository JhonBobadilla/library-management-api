# API de Gestión de Biblioteca

API REST para administrar usuarios, libros, ejemplares físicos y préstamos de una biblioteca.

## Tecnologías

- Java 17
- Spring Boot 3.5.4
- Maven
- PostgreSQL 16
- Docker y Docker Compose
- Spring Data JPA
- Hibernate
- Bean Validation
- Springdoc OpenAPI / Swagger
- Spring Boot Actuator

## Requisitos

- Docker
- Docker Compose
- JDK 17 únicamente para ejecución local
- Maven no es obligatorio porque el proyecto incluye Maven Wrapper

## Variables de entorno

| Variable               | Descripción                              | Valor incluido en .env.example                       |
|------------------------|------------------------------------------|------------------------------------------------------|
| `DB_URI`               | URL de conexión JDBC a PostgreSQL        | `jdbc:postgresql://database:5432/library_db`         |
| `DB_USER`              | Usuario de la base de datos              | `library_user`                                       |
| `DB_PASSWORD`          | Contraseña de la base de datos           | `library_password`                                   |
| `DB_DRIVER`            | Clase del driver JDBC                    | `org.postgresql.Driver`                              |
| `API_PORT`             | Puerto de la aplicación                  | `8080`                                               |
| `DB_PORT`              | Puerto de PostgreSQL                     | `5432`                                               |
| `CORS_ALLOWED_ORIGINS` | Orígenes permitidos para CORS            | `http://localhost:3000`                              |

> **Conexión local vs. Docker:** Dentro de Docker el hostname de la base de datos es `database`. Al ejecutar Spring Boot directamente desde la máquina local, `DB_URI` debe usar `localhost`: `jdbc:postgresql://localhost:5432/library_db`.

Copiar el archivo de ejemplo y ajustar los valores según sea necesario:

```bash
cp .env.example .env
```

## Ejecución con Docker

```bash
docker compose --env-file .env up --build -d
```

Verificar que los servicios estén activos:

```bash
docker compose ps
```

## Acceso a la aplicación

| Recurso               | URL                                                      |
|-----------------------|----------------------------------------------------------|
| API                   | http://localhost:8080                                    |
| Swagger UI            | http://localhost:8080/swagger-ui/index.html              |
| OpenAPI JSON          | http://localhost:8080/v3/api-docs                        |
| Health Check          | http://localhost:8080/actuator/health                    |
| Estado                | http://localhost:8080/api/v1/status                      |

## Funcionalidades

- CRUD de usuarios.
- CRUD de libros con creación automática de ejemplares físicos.
- Consulta de ejemplares disponibles por ISBN.
- Registro de préstamos con restricción de máximo un préstamo abierto por usuario.
- Consulta y filtros de préstamos por usuario o libro.
- Devolución de préstamos con liberación del ejemplar.
- Estados de préstamo: SCHEDULED, ACTIVE, OVERDUE y RETURNED.

## Regla de préstamos abiertos

- Un préstamo abierto es aquel cuyo estado sea SCHEDULED, ACTIVE u OVERDUE.
- Un usuario no puede tener más de un préstamo abierto simultáneamente.
- Para registrar otro préstamo, el préstamo anterior debe estar en estado RETURNED.
- Al devolver un préstamo, el ejemplar cambia a AVAILABLE y puede prestarse nuevamente.

## Endpoints principales

| Método | Ruta                                    | Descripción                                        |
|--------|-----------------------------------------|----------------------------------------------------|
| POST   | `/api/v1/users`                         | Crear un nuevo usuario                             |
| GET    | `/api/v1/users`                         | Listar todos los usuarios                          |
| GET    | `/api/v1/users/{id}`                    | Obtener un usuario por ID                          |
| PUT    | `/api/v1/users/{id}`                    | Actualizar un usuario existente                    |
| DELETE | `/api/v1/users/{id}`                    | Eliminar un usuario                                |
| POST   | `/api/v1/books`                         | Crear un nuevo libro con ejemplares                |
| GET    | `/api/v1/books`                         | Listar todos los libros                            |
| GET    | `/api/v1/books/{id}`                    | Obtener un libro por ID                            |
| GET    | `/api/v1/books/isbn/{isbn}`             | Obtener un libro por ISBN                          |
| GET    | `/api/v1/books/isbn/{isbn}/available-copies` | Consultar ejemplares disponibles por ISBN    |
| PUT    | `/api/v1/books/{id}`                    | Actualizar un libro existente                      |
| DELETE | `/api/v1/books/{id}`                    | Eliminar un libro                                  |
| POST   | `/api/v1/loans`                         | Registrar un nuevo préstamo                        |
| GET    | `/api/v1/loans`                         | Listar préstamos (con filtros opcionales)          |
| PATCH  | `/api/v1/loans/{id}/return`             | Devolver un ejemplar prestado                      |

## Datos de prueba y restauración

El respaldo de la base de datos se encuentra en:

```
database/backup/library_test_data.dump
```

Para restaurar los datos de prueba, la base de datos debe estar levantada. Ejecutar los siguientes comandos:

```bash
docker cp database/backup/library_test_data.dump library-db:/tmp/library_test_data.dump
docker exec library-db pg_restore \
  -U library_user \
  -d library_db \
  --clean \
  --if-exists \
  --no-owner \
  /tmp/library_test_data.dump
```

> **Nota:** En Git Bash para Windows puede ser necesario anteponer `MSYS_NO_PATHCONV=1` a los comandos `docker cp` y `docker exec`.

## Desarrollo local

Antes de ejecutar la aplicación localmente, configurar las variables de entorno. `DB_URI` debe apuntar a `localhost` (el hostname `database` solo es válido dentro de la red de Docker).

Ejemplo en Git Bash:

```bash
DB_URI=jdbc:postgresql://localhost:5432/library_db \
DB_USER=library_user \
DB_PASSWORD=library_password \
DB_DRIVER=org.postgresql.Driver \
./mvnw spring-boot:run
```

Ejecutar las pruebas:

```bash
./mvnw clean test
```

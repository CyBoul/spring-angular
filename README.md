# Pet Adoption — Full-Stack Demo

![Java](https://img.shields.io/badge/Java-21_LTS-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.5-6DB33F?logo=springboot&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-20-DD0031?logo=angular&logoColor=white)
![PrimeNG](https://img.shields.io/badge/PrimeNG-20-1976D2?logo=primeng&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3-C71A36?logo=apachemaven&logoColor=white)

A **pet adoption platform** — browse pets, adopt one, manage the catalog as an admin.

---

## Features

- **JWT auth via httpOnly cookie** — SameSite=Lax, no token in localStorage; session restored on refresh via `/api/auth/me`
- **Role-based access** — `@PreAuthorize` on all mutating endpoints; async guards on the frontend wait for server-validated state
- **DTO layer** — JPA entities never exposed through the API (`PetDTO`, `AdoptionDTO`, `AuthDTO`)
- **RFC 7807 errors** — consistent `ProblemDetail` responses from `GlobalExceptionHandler`
- **Security headers** — CSP, HSTS, X-Frame-Options, nosniff; CORS externalized to config property
- **Input validation** — `@NotBlank` / `@Size` on backend, reactive forms with `Validators` on frontend
- **OpenAPI / Swagger UI** — JWT Bearer scheme, restricted to dev profile

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21 · Spring Boot 3.5.5 · Spring Security (JWT httpOnly cookie) · Spring Data JPA |
| Database | PostgreSQL · Docker Compose |
| API | REST · RFC 7807 ProblemDetail · OpenAPI / Swagger UI |
| Frontend | Angular 20 · PrimeNG 20 · Standalone components · Reactive forms |
| Build | Maven multi-module |

---

## Running Locally

**Requirements:** Docker, Java 21+, Node 22+

```bash
# Start PostgreSQL
docker compose up -d

# Backend (port 8080)
./mvnw spring-boot:run -pl backend -DskipTests

# Frontend dev server (port 4200, proxies /api to backend)
cd frontend && npx ng serve --proxy-config proxy.conf.json
```

Open [http://localhost:4200](http://localhost:4200)

**Default users:**

| Email | Password | Role |
|-------|----------|------|
| `admin@fake.com` | `admin` | ADMIN |
| `bob@fake.com` | `password` | USER |

API docs: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Tests

```bash
cd backend && mvn test
```

32 tests across unit, Web MVC slice, and integration layers.

| Suite | Type | Tests | What it covers |
|-------|------|------:|----------------|
| `JwtServiceUnitTest` | Unit | 4 | Token generation, parsing, expiry |
| `PetControllerWebMvcTest` | MVC slice | 16 | Authorization rules (200/201/204/401/403/404) |
| `AuthControllerWebMvcTest` | MVC slice | 3 | Login happy path, bad credentials, cookie auth |
| `AuthSecurityIntTest` | Integration | 6 | Full security chain — filter, cookie validation, role enforcement |
| `JwtServiceIntTest` | Integration | 2 | JWT round-trip with real Spring context |
| `JwtServiceExpireIntTest` | Integration | 1 | Token expiry validation |

---

## Project Notes

- [Testing strategy](notes/tests.md)
- [Progress log](notes/progress.md)

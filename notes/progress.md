[<< Readme](../README.md)

This file tracks the technical progress and decisions made during the development of this project.
It is intentionally informal and focuses on implementation steps and learning milestones.

### Planned Improvements

Non-exhaustive list of upcoming technical and functional improvements:

- CORS configuration and security hardening
- Token lifetime and refresh strategy
- Testcontainers for integration tests
- CI pipeline (GitHub Actions)

---

### Recent Changes

- DTO layer (`PetDTO`, `AdoptionDTO`, `AuthDTO`) — JPA entities no longer exposed through REST API
- Swagger UI public access with JWT Bearer auth scheme (`@SecurityScheme` / `@SecurityRequirement`)
- Logback config — quiets Hikari, Hibernate, Tomcat at startup; app package at DEBUG
- SPA deep-link support — Spring Security permits Angular routes, MvcConfig catch-all forwards to index.html
- PrimeNG downgraded from 20.5.1-lts to 20.4.0 (non-LTS) — removes license banner
- Login page, pet list, admin CRUD with PrimeNG 20
- Routing with `authGuard` / `adminGuard`, lazy-loaded routes
- web-layer integration tests for PetController
- web-layer integration tests for AuthController
- Unit and integration tests for JwtService
- Password hashing using BCrypt
- HTTP interceptor to inject JWT into frontend API calls

---

### Authentication / JWT

Implemented authentication flow using JWT:

- Dedicated service for token creation and validation
- HTTP filter / interceptor for request validation
- Authentication controller
- Manual validation using `curl` ([see curl.md](curl.md))


### Project Initialization

- Spring Boot backend initialization
- PostgreSQL setup with Docker ([see compose.yml](../compose.yml))
- Angular project setup and module preparation ([see structure.md](structure.md))
- Maven build automation for full-stack packaging
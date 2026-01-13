[<< Readme](../README.md)

This file tracks the technical progress and decisions made during the development of this project.
It is intentionally informal and focuses on implementation steps and learning milestones.

### Planned Improvements

Non-exhaustive list of upcoming technical and functional improvements:

- CORS configuration and security hardening
- Token lifetime and refresh strategy
- UI integration (Bootstrap / PrimeNG)
- Login page implementation
- Role-based access control

---

### Recent Changes

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
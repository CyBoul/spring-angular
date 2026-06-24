[<< Readme](../README.md)

This file tracks the technical progress and decisions made during the development of this project.
It is intentionally informal and focuses on implementation steps and learning milestones.

### Planned Improvements

- Testcontainers for integration tests
- CI pipeline (GitHub Actions)

---

### Recent Changes

**Code review round 2 — security**
- JJWT upgraded 0.11.5 → 0.12.6, migrated to current API (`subject()`, `parseSignedClaims()`, `signWith(key)`)
- `JwtService` simplified: expired tokens short-circuit, no unnecessary `UserDetails` load
- `@PreAuthorize("hasRole('ADMIN')")` for HATEOAS controller too
- `Pet` entity no longer extends `RepresentationModel` — HATEOAS uses `EntityModel<Pet>` wrapper
- `@NotNull` on `PetDTO.type` for proper RFC 7807 validation errors
- `JsonDataLoader` uses `count()` instead of `findAll().isEmpty()`
- `User` `@JsonCreator` constructor now accepts `role` parameter
- Dead code removal

**Security & architecture hardening**
- CSP header: `Content-Security-Policy` added to `SecurityConfig` (script, style, font, img, connect directives)
- CORS externalized: origin moved from hardcoded `localhost:4200` to `app.cors.allowed-origins` property, env-var-backed in prod
- CORS filter wired in: replaced `.cors(disable)` with actual `CorsConfigurationSource` bean, `allowCredentials=true` for httpOnly cookies

**Frontend redesign & security hardening**
- Login: split-screen layout (hero panel + form), reactive forms with validation
- Pet list: hero section, search bar, animal-type filter chips, card grid with accent colors, skeleton loading
- Navbar: white background, active link indicator
- Global styles: Inter font, CSS custom properties

**Auth rework — httpOnly cookies**
- JWT moved from localStorage to httpOnly cookie (`Set-Cookie` by backend, `SameSite=Lax`)
- Token no longer in JSON response body — frontend never touches it
- Role stored in memory (`BehaviorSubject`), restored on refresh via `GET /api/auth/me`
- Guards async-aware: wait for server-validated session state
- Class-based `HttpInterceptor` replaced with functional `HttpInterceptorFn` (401 → redirect)
- `POST /api/auth/logout` clears cookie server-side

**Code quality pass**
- Dead code deleted (`logic/disabled/` package)
- Swagger restricted to dev profile
- `UNIQUE(email)` constraint added to schema
- Reactive forms with `Validators` on login and admin forms
- `takeUntilDestroyed` on all subscriptions
- Global `ErrorHandler` registered
- Environment files for API base URL (`environment.ts` / `environment.prod.ts`)
- `application-prod.properties` filled with env-var-backed config
- Pet model aligned with backend (`type` field, correct `Animal` enum values)
- Inline styles extracted to separate SCSS files

---

### Previous Changes

- DTO layer (`PetDTO`, `AdoptionDTO`, `AuthDTO`) — JPA entities no longer exposed through REST API
- Swagger UI with JWT Bearer auth scheme (`@SecurityScheme` / `@SecurityRequirement`)
- Logback config — quiets Hikari, Hibernate, Tomcat at startup
- SPA deep-link support — Spring Security permits Angular routes, MvcConfig catch-all
- Login page, pet list, admin CRUD with PrimeNG 20
- Routing with `authGuard` / `adminGuard`, lazy-loaded routes
- Web-layer and integration tests for controllers and JwtService
- Password hashing using BCrypt

---

### Authentication / JWT

- JWT in httpOnly cookie (backend sets/clears), `Authorization` header as fallback for Swagger
- `JwtFilter` reads cookie first, then header
- `/api/auth/me` endpoint for session restore on page refresh
- `/api/auth/logout` clears cookie with `Max-Age=0`
- Manual validation using `curl` ([see curl.md](curl.md))


### Project Initialization

- Spring Boot backend initialization
- PostgreSQL setup with Docker ([see compose.yml](../compose.yml))
- Angular project setup and module preparation ([see structure.md](structure.md))
- Maven build automation for full-stack packaging
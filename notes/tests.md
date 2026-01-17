<< [Readme](../README.md)

---

### Testing strategy

This project follows a layered testing strategy inspired by Spring best practices and Clean Architecture principles.
Each test type has a clearly defined scope and responsibility.

#### Guiding principles

- One test type per responsibility
- No duplicated coverage across layers
- MVC slice tests *do not* replace integration tests
- Integration tests *do not* replace E2E tests

---

#### Unit tests

``src/test/java/.../unit``

**Goal**: Verify business logic in isolation.

- No Spring context
- No HTTP layer
- Dependencies mocked manually
- Fast and deterministic

**Examples**:
- Domain services
- Utility classes 
- Pure business rules

---

#### Web (MVC slice) tests

``src/test/java/.../web``

**Goal**: Validate the web layer contract.

- Spring MVC context only (@WebMvcTest)
- Real controllers
- Dependencies mocked (@MockitoBean)
- HTTP requests tested via MockMvc
- Focus on request/response, status codes, validation, and security rules

**Examples**:
- REST controllers
- Authentication endpoints
- Authorization rules at controller level

---

#### Integration tests

``src/test/java/.../integration``

**Goal**: Verify that multiple components work together inside a Spring context.

- Partial Spring context
- Real beans and configuration
- No HTTP layer by default (MockMvc allowed when required)
- No full infrastructure bootstrapping unless required

**Examples**:
- Service + configuration integration
- Security or JWT configuration
- Cross-bean interactions

> See [Security-focused integration tests (JWT, roles, filters)](securitytests.md)

---

#### End-to-End (E2E) tests

``src/test/java/.../e2e``

**Goal**: Validate complete application flows.

- Full application context
- Real infrastructure (DB, security, etc.)
- HTTP calls against a running server
- Closest to production behavior

**Examples**:
- Full authentication flow
- API consumed as a black box





# Engineering Sandbox

## Purpose

This repository is a small, evolving project used to practice **testing, refactoring, and code structure** in a realistic full-stack environment.

The focus is not on delivering a finished product, but on:
- improving testability,
- applying SOLID principles pragmatically,
- learning through iteration and refactoring,
- documenting technical decisions and trade-offs.

---

## Tech Stack

- Java 24 / Spring Boot 3
- PostgreSQL / Docker
- Angular 20 / Node.js 24
- Maven multi-module build

---

## Engineering Focus

### Testing
- Unit and integration tests
- Tests used as a safety net for refactoring
- Ongoing work toward more consistent, behavior-focused tests
- Gradual introduction of TDD for upcoming features

### Code Structure
- Effort to keep business logic independent from technical concerns
- Preference for simple, readable design over premature abstraction
- SOLID principles used as guidelines, not strict rules

---

## Build
```bash
mvn install
```
This command builds both frontend and backend and packages them together.

---

## What I would refactor next

If the project grows, the next steps would likely include:

- improving test consistency and readability,
- introducing stricter boundaries where complexity increases,
- refactoring incrementally while avoiding unnecessary abstraction.

These changes are intentionally deferred to keep the project simple and focused.



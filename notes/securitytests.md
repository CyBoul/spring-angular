<< [Readme](../README.md) |  [Testing strategy](tests.md)

---

### Security-focused Integration Tests

Tests focusing on **authentication, authorization, and HTTP response codes**.  
Repository / database interactions are **mocked**, so we isolate security behavior.

- Security-focused tests **do not verify business logic**.
- JWT filter is included to simulate production security context.
- HTTP status checks are the main assertions.

| Endpoint               | JWT / Role            | Expected HTTP Status | Notes                              |
|---|---|---|---|
| `POST /api/auth/login` | ✅ Valid credentials   | <span style="color:green">200 OK</span> | Login success, returns JWT token |
| `POST /api/auth/login` | ❌ Invalid password    | <span style="color:red">401 Unauthorized</span> | Bad credentials                  |
| `POST /api/auth/login` | ❌ Unknown user        | <span style="color:red">401 Unauthorized</span> | User does not exist              |
| `GET /api/pets`        | ✅ Valid JWT (USER)    | <span style="color:green">200 OK</span> | Access granted                   |
| `GET /api/pets`        | ❌ Invalid JWT         | <span style="color:red">401 Unauthorized</span> | Invalid token                     |
| `GET /api/pets`        | ⚠️ No JWT              | <span style="color:red">401 Unauthorized</span> | Authentication required          |
| `DELETE /api/pets/1`   | ✅ Valid JWT (ADMIN)   | <span style="color:green">204 No Content</span> | Admin can delete                  |
| `DELETE /api/pets/1`   | ❌ Valid JWT (USER)    | <span style="color:red">403 Forbidden</span> | Non-admin cannot delete           |




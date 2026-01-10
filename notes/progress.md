[<< Readme](../README.md)

### ToDo List

- Redo Tests !!!
- CORS config
- Time of Tokens
- Import Bootstrap/PrimeNg
- Create the login page
- Role based access
- ...

---

### Latest changes

- JwtService unit & integration tests (PASS)
- Bcrypt encryption for passwords ✔
- Create an http-interceptor to inject token in API calls from front ✔

---

### Authentication / JWT

- Create a service for `JWT` tokens & validation ✔
- Create a filter (interceptor) to validate requests ✔
- Create a controller for authentication calls ✔
- Tests with `curl` ([see curl.md](curl.md)) ✔

---

### Project init

- Init a basic spring-boot project ✔
- Dockerized postgres ([see compose.yml](../compose.yml)) ✔
- Prepare for Angular: Create modules ([see structure.md](structure.md)) ✔
- Prepare for Angular: Fix POMs  ✔
- Create a minimal Angular app ✔
  ```
  cd project_folder
  ng new frontend --minimal
  ```
- Configure maven build automation ✔

_``mvn install`` from the project folder will :_
- _download and install node (once)_
- _build the frontend_
- _copy build output from front module to the ``static`` resource folder of back module_
- _build the backend_

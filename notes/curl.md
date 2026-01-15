<< [Readme](../README.md) | [Change logs](progress.md)

---

#### Auth Error

```shell
curl -i -H "Content-Type: application/json" \
-d "{\"email\":\"admin@fake.com\",\"password\":\"WrongPass\"}" \
http://localhost:8080/api/auth/login  
```

``HTTP/1.1 400``

---

#### Successful authentication, which grants the jwt token

```shell  
curl -i -H "Content-Type: application/json" \
-d "{\"email\":\"admin@fake.com\",\"password\":\"admin\"}" \
http://localhost:8080/api/auth/login
```

``HTTP/1.1 200`` 
``Content-Type: application/json``
``...``
``{"token":"eyJhbGciOiJIUzI1NiJ9...axGZE"}``

---

#### Fetching data with it

```shell  
curl -i -H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...axGZE" \
http://localhost:8080/api/pets
```

``HTTP/1.1 200``
``Content-Type: application/json``
``...``
``[{"id":1,"name":"Bobby","description":"Cute and sweet","type":"DOG"...}]``

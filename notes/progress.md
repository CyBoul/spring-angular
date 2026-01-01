[< Back](../README.md)

### Changes

- Init a basic spring-boot project ✔
- Connect to H2 database ✔
- Change inMem DB by dockerized postgres ✔

```compose.yaml
services:
    postgres:
        image: 'postgres:latest'
        environment:
            - 'POSTGRES_DB=DatabaseName'
            - 'POSTGRES_PASSWORD=password'
            - 'POSTGRES_USER=user'
        ports:
            - '5432:5432'
```
```pom.xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.4</version>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-docker-compose</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

---

### Web Interface
- Prepare for Angular: Create modules ✔
- Fix POMs  ✔
```
/my-app
│
├── pom.xml                <-- Parent POM
├── compose.yml            <-- Docker Compose 
├── mvnw                   <-- Maven wrapper 
├── mvnw.cmd
├── .mvn/
│
├── backend/ 
│   ├── pom.xml
│   ├── src/
│   │   ├── main/java/...
│   │   └── main/resources/
│   │       └── static/    <-- Angular build output ends up here
│   └── ...
│
└── frontend/              <-- Angular app
    ├── pom.xml            <-- Maven config for 'frontend-maven-plugin'
    ├── angular.json
    ├── package.json
    ├── tsconfig.json
    └── src/
```
- Create a minimal Angular app ✔
```
cd Playground
ng new frontend --minimal
```
- Configure maven with plugins for build automation ✔
  - tell maven to install node + dependencies + build `Angular` module (frontend.pom)
  - then, copy the build output to the backend (backend.pom)
  - make sure the front build before back (parent.pom)
```frontend.pom
    <build>
        <plugins>
            <plugin>
                <groupId>com.github.eirslett</groupId>
                <artifactId>frontend-maven-plugin</artifactId>
                <version>1.12.0</version>
                <configuration>
                    <nodeVersion>v24.6.0</nodeVersion>
                    <workingDirectory>.</workingDirectory>
                </configuration>
                <executions>
                
                    <!-- install node & npm -->
                    <execution>
                        <id>install-npm</id>
                        <goals>
                            <goal>install-node-and-npm</goal>
                        </goals>
                    </execution>
                    
                    <!-- npm i -->
                    <execution>
                        <id>npm-install</id>
                        <goals>
                            <goal>npm</goal>
                        </goals>
                    </execution>
                    
                    <!-- npm build script = ng build -->
                    <execution>
                        <id>npm-build</id>
                        <goals>
                            <goal>npm</goal>
                        </goals>
                        <configuration>
                            <arguments>run-script build</arguments>
                        </configuration>
                    </execution>
                    
                </executions>
            </plugin>
        </plugins>
    </build>
```
```backend.pom
  <build>
        <plugins>
            ...
            <plugin>
                <artifactId>maven-resources-plugin</artifactId>
                <version>3.3.1</version>
                <executions>
                    <!-- copy angular build files to spring static folder -->
                    <execution>
                        <id>copy-angular-build</id>
                        <phase>process-resources</phase>
                        <goals>
                            <goal>copy-resources</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.build.outputDirectory}/static</outputDirectory>
                            <resources>
                                <resource>
                                    <directory>../frontend/dist/frontend/browser</directory>
                                    <includes>
                                        <include>**/*</include>
                                    </includes>
                                </resource>
                            </resources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            ...
        </plugins>
    </build>
```
so now ``mvnw i`` from the project folder will :
- build the frontend
- copy build output from angular to the ``static`` resource folder of spring
- build the backend

_The modules' order declared in the parent POM define the order for build executions_
```parent.pom
    <modules>
        <module>frontend</module>
        <module>backend</module>
    </modules>
```

---
### Authentication
#### Use JWT to secure the API
- Backend
  - Create tool class for JWT validation & token ✔
  - Change to **stateless** authentication in `Spring-secu` config ✔
  - Create a `Filter` (interceptor) to validate requests ✔
  - Create an AuthenticationController for login call ✔
  - Test the Authentication with curl ✔
```shell
curl -i http://localhost:8080/api/gugu
HTTP/1.1 403
...
# GET /api/gugu rejected, normal it now requires authentication

curl -i  http://localhost:8080/api/pets                                                   
HTTP/1.1 403 
# Get /api/pets rejected, same reason as previously

curl -i http://localhost:8080/api/auth/login
HTTP/1.1 403 
Allow: POST
...
# GET /api/auth/login rejected also, and tells us it allows POST, Good!
# POST with no content is rejected also

curl -i -H "Content-Type: application/json" \
     -d "{\"email\":\"admin@fake.com\",\"password\":\"WrongPass\"}" \ 
     http://localhost:8080/api/auth/login  
     
HTTP/1.1 400 # 400 to differentiate -> wrong credentials
```
```shell
curl -i -H "Content-Type: application/json" \ 
     -d "{\"email\":\"admin@fake.com\",\"password\":\"admin\"}" \ 
     http://localhost:8080/api/auth/login
     
HTTP/1.1 200 
Content-Type: application/json
...
{"token":"eyJhbGciOiJIUzI1NiJ9...axGZE"}
# Successful authentication, which grants the jwt token

curl -i -H "Content-Type: application/json" \
        -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...axGZE" \ 
        http://localhost:8080/api/pets
        
HTTP/1.1 200 
Content-Type: application/json
...
[{"id":1,"name":"Bobby","description":"Cute and sweet","type":"DOG"...}]
# Successful fetching data 
```

- Front
  - Create http-interceptor to inject token in API calls ✔ 

<< [Readme](../README.md) | [Change logs](progress.md)

---

```
/my-app
│
├── pom.xml                <-- Parent POM
├── compose.yml            <-- Docker Compose 
│
├── .mvn/
├── mvnw                   <-- Maven wrapper 
├── mvnw.cmd
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
        └── ...
```
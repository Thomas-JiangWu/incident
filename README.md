# incident management

## Overview

This is an example project about incidents management. It provides CRUD APIs to manage incidents:
- POST /api/incidents: create an incident
- GET /api/incidents: list incidents
- GET /api/incidents/{id}: get an incident
- PUT /api/incidents/{id}: update an incident
- DELETE /api/incidents/{id}: delete an incident

## Dependencies

This project is developed using Java 17 and Maven 3.6.3. Apart from the Spring Boot framework, this project includes following dependencies:
- Lombok: helps reduce boilerplate code of class definition.
- fastjson: JSON serialization and deserialization.
- MyBatis-Plus: enhanced toolkit of MyBatis for simplifying operations on database.
- H2: an in-memory database.
- Caffeine: a high-performance caching library.
- JUnit: a test automation framework.

## How to Run

There are 3 ways to run this project:
- Run this project in an IDE, e.g. `IDEA`.
- Run this project using `mvn` and `java` command:
```sh
mvn package -Dmaven.test.skip
java -jar target/incident-1.0.0.war
```
- Run this project using `Docker`, and the container image `thomasjiangangwu/incident:1.0.0` is built based on the Dockerfile of this project:
```sh
docker run -d -p 8080:8080 thomasjiangangwu/incident:1.0.0
```

After running this project, you can access the APIs like bellow:
```sh
curl http://localhost:8080/api/incidents
```
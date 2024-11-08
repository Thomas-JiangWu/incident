# incident management

## Overview

This is an example project about incidents management. It provides CRUD APIs to manage incidents:
- POST /api/incidents: create an incident 

input:
```json
{
"description": "desc",
"status": "PENDING"
}
```
output:
```json
{
  "statusCode": 0,
  "message": "success",
  "data": 1
}
```
- GET /api/incidents?pageNum={pageNum}&pageSize={pageSize}: list incidents

output:
```json
{
    "statusCode": 0,
    "message": "success",
    "data": {
        "records": [
            {
                "id": 1854569583036768258,
                "description": "desc",
                "status": "PENDING"
            }
        ],
        "total": 1,
        "size": 10,
        "current": 1,
        "pages": 1
    }
}
```
- GET /api/incidents/{id}: get an incident

output:
```json
{
  "statusCode": 0,
  "message": "success",
  "data": {
    "id": 1,
    "description": "desc",
    "status": "PENDING"
  }
}
```
- PUT /api/incidents/{id}: update an incident

input:
```json
{
  "description": "desc",
  "status": "PENDING"
}
```
output:
```json
{
  "statusCode": 0,
  "message": "success",
  "data": true
}
```
- DELETE /api/incidents/{id}: delete an incident

output:
```json
{
    "statusCode": 0,
    "message": "success",
    "data": true
}
```

## Dependencies

This project is developed using Java 17 and Maven 3.6.3. Apart from the Spring Boot framework, this project includes following dependencies:
- Lombok: helps reduce boilerplate code of class definition.
- fastjson: JSON serialization and deserialization.
- MyBatis-Plus: enhanced toolkit of MyBatis for simplifying operations on database.
- H2: an in-memory database.
- Caffeine: a high-performance caching library.
- JUnit: a test automation framework.

## How to Run this Project

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

## Frontpage Project

There is another companion React [frontpage project](https://github.com/Thomas-JiangWu/incident-frontpage?tab=readme-ov-file) for this project, which supports adding/modifying/deleting incidents and displaying the incident list on the page. Please refer to the project documentation to run the project.


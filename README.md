# Incident Management

## Overview

This is an example project about incidents management. It provides CRUD APIs to manage incidents:
- POST /api/incidents: create an incident 

input:
```json
{
  "reporter": "Thomas",
  "title": "an incident",
  "description": "description",
  "status": "Pending",
  "priority": "Low"
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
- GET /api/incidents?pageNum={pageNum}&pageSize={pageSize}&status={status}$priority={priority}: list incidents

param:
```json
{
    "pageNum": 1,
    "pageSize": 10,
    "status": "Pending", // Pending, Processing, Resolved
    "priority": "Low" // Low, Medium, High
}
```

output:
```json
{
    "statusCode": 0,
    "message": "success",
    "data": {
        "records": [
            {
                "id": 1,
                "reporter": "Thomas",
                "title": "an incident",
                "description": "description",
                "status": "Pending",
                "priority": "Low",
                "createdTime": "2024-11-08 10:15:19",
                "modifiedTime": "2024-11-08 10:15:31"
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
    "reporter": "Thomas",
    "title": "an incident",
    "description": "description",
    "status": "Pending",
    "priority": "Low",
    "createdTime": "2024-11-08 10:15:19",
    "modifiedTime": "2024-11-08 10:18:17"
  }
}
```
- PUT /api/incidents/{id}: update an incident

input:
```json
{
  "description": "desc",
  "status": "Pending"
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
- Run this project using `Docker`:
```sh
docker run -d -p 8080:8080 thomasjiangangwu/incident:1.0.0
```
The container image `thomasjiangangwu/incident:1.0.0` is built based on the Dockerfile of this project and can be pulled publicly.

After running this project, you can access the APIs like bellow:
```sh
curl http://localhost:8080/api/incidents
```

## Frontpage Project

There is another companion React [frontpage project](https://github.com/Thomas-JiangWu/incident-frontpage?tab=readme-ov-file) for this project, which supports adding/modifying/deleting incidents and displaying the incident list on the page. Please refer to the project documentation to run the project.


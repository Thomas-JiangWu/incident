# Stage 1: Build
FROM maven:3.6.3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/incident-1.0.0.war incident.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "incident.war"]

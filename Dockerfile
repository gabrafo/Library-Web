# Estágio 1: Build
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean install -Dmaven.test.skip=true

# Estágio 2: Runtime
FROM openjdk:17-jdk-slim AS stage-1
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
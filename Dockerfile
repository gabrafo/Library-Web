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

# Baixar o wait-for-it script
RUN apt-get update && apt-get install -y curl
RUN curl -sSLo /wait-for-it.sh https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh && chmod +x /wait-for-it.sh

# Iniciar a aplicação após verificar que o banco de dados está pronto
CMD ["/wait-for-it.sh", "db:5432", "--", "java", "-jar", "app.jar"]
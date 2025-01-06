# Etapa 1: Construir a aplicação
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY backend/pom.xml ./backend/pom.xml
COPY backend/src ./backend/src
RUN mvn -f backend/pom.xml clean install -Dmaven.test.skip=true

# Etapa 2: Criar a imagem de runtime
FROM openjdk:17-jdk-slim AS runtime
WORKDIR /app
COPY --from=build /app/backend/target/libraryweb-0.0.1-SNAPSHOT.jar /app/libraryweb.jar

# Baixar o script wait-for-it.sh para garantir que o banco de dados está pronto antes de iniciar
RUN apt-get update && apt-get install -y curl
RUN curl -sSLo /wait-for-it.sh https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh && chmod +x /wait-for-it.sh

# Iniciar a aplicação após a verificação do banco de dados
CMD ["/wait-for-it.sh", "db:5432", "--", "java", "-jar", "/app/libraryweb.jar"]
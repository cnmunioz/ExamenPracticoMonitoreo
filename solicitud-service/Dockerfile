# Etapa 1: Build
FROM maven:3.9.16-eclipse-temurin-25 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa : Runtime
FROM eclipse-temurin:25-jdk
WORKDIR /app
ARG SERVICE_NAME
COPY --from=builder /app/${SERVICE_NAME}/target/${SERVICE_NAME}-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083 8084 8888 8761 8080
ENTRYPOINT ["java", "-jar","app.jar"]
# ----------- Build Stage -----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
LABEL authors="mehta"

WORKDIR /home/app

COPY pom.xml .
COPY src ./src

# Build the application and skip tests
RUN mvn clean package -DskipTests

# ----------- Package Stage -----------
FROM openjdk:21-jdk-slim

WORKDIR /app

# Check that the JAR exists in the correct path
COPY --from=build /home/app/target/*.jar /app/app.jar

EXPOSE 9211

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

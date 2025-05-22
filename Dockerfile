# ----------- Build Stage -----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
LABEL authors="mehta"

WORKDIR /home/app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ----------- Package Stage -----------
FROM openjdk:17-jdk-slim

COPY --from=build /home/app/target/getyourway-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/usr/local/lib/demo.jar"]

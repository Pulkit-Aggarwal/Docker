# Stage 1: Build the application using a Maven image
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application using your pre-existing base image
FROM java-multi:latest
WORKDIR /app
# Copies the compiled target JAR from the build stage into the runtime stage
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
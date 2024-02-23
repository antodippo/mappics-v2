# Use a Maven image as the base image for building the application
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the Maven configuration files to the container
COPY pom.xml ./
COPY src ./src

# Build the application
RUN mvn -B -DskipTests clean package

# Use a lightweight Alpine OpenJDK image as the base image for the runtime environment
FROM eclipse-temurin:21.0.2_13-jre

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file built in the previous stage to the container
COPY --from=builder /app/target/*.jar /app/app.jar

# Specify the command to run the application when the container starts
CMD ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
# Use the official Maven image to create a build artifact.
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set the working directory.
WORKDIR /usr/src/app

# Copy the source code to the working directory.
COPY . /usr/src/app

# Build the application.
RUN mvn clean package

# Use the official OpenJDK image for a lean production stage of our multi-stage build.
FROM eclipse-temurin:21-jre-jammy

# Set the working directory.
WORKDIR /app

# Copy the jar file from the build stage to the production stage.
COPY --from=build /usr/src/app/target/*.jar /app/app.jar

# Run the application.
CMD ["java", "-jar", "/app/app.jar"]
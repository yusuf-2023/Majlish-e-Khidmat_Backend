# ==== Stage 1: Build ====
FROM eclipse-temurin:17-jdk-alpine AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Give execution permission to mvnw
RUN chmod +x mvnw

# Download dependencies only (for caching)
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the project (skip tests)
RUN ./mvnw clean package -DskipTests

# ==== Stage 2: Run ====
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port
EXPOSE 8080

# Command to run the jar
ENTRYPOINT ["java","-jar","app.jar"]

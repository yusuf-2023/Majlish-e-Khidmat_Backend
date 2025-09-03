# Use a base image with Java
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy jar to container
COPY target/majlishekhidmat-0.0.1-SNAPSHOT.jar app.jar

# Expose port (optional)
EXPOSE 8080

# Command to run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]

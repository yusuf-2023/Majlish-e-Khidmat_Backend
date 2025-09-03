FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the jar from target
COPY target/majlishekhidmat-0.0.1-SNAPSHOT.jar app.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]

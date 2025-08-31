# Use a Java runtime
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy the rest of the project
COPY src ./src

# Package the app
RUN ./mvnw -DskipTests clean package

# Run the app
CMD ["java", "-jar", "target/majlishekhidmat-0.0.1-SNAPSHOT.jar"]

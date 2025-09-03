# Step 1: Use Maven to build the project
FROM maven:3.9.1-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml and download dependencies first (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the jar
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Use JDK image to run the jar
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/majlishekhidmat-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Spring Boot default)
EXPOSE 8080

# Command to run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
git add .
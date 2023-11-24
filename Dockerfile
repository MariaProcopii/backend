FROM openjdk:17-jdk-alpine AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle files
COPY build.gradle settings.gradle gradlew gradlew.bat /app/

# Copy the Gradle wrapper
COPY gradle/wrapper /app/gradle/wrapper

# Download and install Gradle
RUN chmod +x gradlew && ./gradlew --version

# Copy the application source code
COPY src /app/src

# Build the application
RUN ./gradlew build

FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/training-licenses-sharing-1.0-SNAPSHOT.jar ./app.jar

# Specify the command to run on container startup
CMD ["java", "-jar", "app.jar"]
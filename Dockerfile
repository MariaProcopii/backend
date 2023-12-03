FROM openjdk:17-jdk-alpine AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy repository
COPY . .

# Download and install Gradle
RUN chmod +x gradlew && chmod +x database/db-env.sh

# Copy the application source code
COPY src /app/src

# Build the application without test units
RUN ./gradlew clean build -x :test

FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the repository to executing migration schema
COPY . .

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/training-licenses-sharing-1.0-SNAPSHOT.jar ./app.jar

# Give execution rights for gradlew to migrate schemas
RUN chmod +x gradlew && chmod +x database/db-env.sh

# Specify the command to run on container startup
CMD ["java", "-jar", "app.jar"]
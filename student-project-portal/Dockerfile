# syntax=docker/dockerfile:1

############################
# 1. Build stage
############################
# Every From take as new Stage .Last Stage is default Final Image
FROM eclipse-temurin:21-jdk-jammy AS build
# Create the directory build and do cd build
WORKDIR /build

# Copy Maven wrapper first (for caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (cached layer)
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -DskipTests

# Copy source code
COPY src src

# Build application
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw clean package -DskipTests


############################
# 2. Runtime stage
############################
FROM eclipse-temurin:21-jre-jammy AS runtime

WORKDIR /app

# Create non-root user (best practice)
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    appuser

USER appuser

# Copy built JAR from build stage
COPY --from=build /build/target/*.jar app.jar

# Expose port
EXPOSE 8082

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
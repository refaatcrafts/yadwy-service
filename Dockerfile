# Stage 1: Build
FROM eclipse-temurin:24-jdk-alpine AS builder

WORKDIR /app

# Copy Gradle wrapper and build files first (for better caching)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Make gradlew executable
RUN chmod +x gradlew

# Copy OpenAPI spec (needed for code generation before dependency resolution)
COPY src/main/resources/openapi src/main/resources/openapi

# Download dependencies (cached layer)
# Note: openApiGenerate runs as part of compileKotlin, so we need the spec first
RUN ./gradlew dependencies --no-daemon || true

# Copy remaining source code
COPY src src

# Build the application (openApiGenerate runs automatically via compileKotlin dependency)
RUN ./gradlew bootJar --no-daemon -x test

# Stage 2: Runtime
FROM eclipse-temurin:24-jre-alpine AS runtime

# Create non-root user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose the application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# JVM tuning for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

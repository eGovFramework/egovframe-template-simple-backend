# syntax=docker/dockerfile:1.7
# Multi-stage build for egovframe-template-simple-backend.
# Stage 1 builds the executable Spring Boot jar with Maven.
# Stage 2 runs it on a minimal Temurin JRE 17 image as a non-root user.

# ---------- Build ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Resolve dependencies first for better layer caching
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -ntp dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -ntp -DskipTests package && \
    cp target/*.jar /workspace/app.jar

# ---------- Runtime ----------
FROM eclipse-temurin:17-jre-alpine AS runtime

RUN addgroup -S app && adduser -S -G app app
USER app:app

WORKDIR /app
COPY --from=build --chown=app:app /workspace/app.jar /app/app.jar

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError" \
    SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD wget -qO- http://127.0.0.1:8080/actuator/health || exit 1

ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]

# Multi-stage Dockerfile for GraalVM Native Image
FROM ghcr.io/graalvm/native-image-community:21 AS builder

WORKDIR /build

# Copy Maven wrapper and pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B -P "production,native" || true

# Copy source code and frontend
COPY src/ src/

# Build native binary with Vaadin production mode
RUN ./mvnw -P "production,native" package -DskipTests

# Runtime stage with minimal base image
FROM ubuntu:22.04

WORKDIR /app

# Install runtime dependencies (e.g. zlib, libstdc++)
RUN apt-get update && apt-get install -y --no-install-recommends \
    libstdc++6 \
    zlib1g \
    && rm -rf /var/lib/apt/lists/*

# Copy native executable from builder stage
COPY --from=builder /build/target/core-erp /app/core-erp

EXPOSE 8080

ENTRYPOINT ["/app/core-erp"]

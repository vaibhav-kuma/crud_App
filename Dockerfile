
# Java 21 + Maven + JavaFX
FROM maven:3.9.9-eclipse-temurin-21

# Application working directory
WORKDIR /app

# Install JavaFX native dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    libgtk-3-0 \
    libasound2t64 \
    libgl1 \
    libx11-6 \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libfreetype6 \
    && rm -rf /var/lib/apt/lists/*

# Copy project files
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .

# Copy source code
COPY src/ src/

# Ensure Maven wrapper is executable
RUN chmod +x mvnw

# Download dependencies
RUN mvn -B dependency:go-offline

# Create persistent database directory
RUN mkdir -p /app/data

# Persistent H2 database storage
VOLUME ["/app/data"]

# Launch JavaFX application
CMD ["mvn", "-B", "javafx:run"]

# 1. THE FOUNDATION: We need a computer with Java 21 installed.
# We use 'eclipse-temurin' because it is a lightweight, industry-standard Java environment.
FROM eclipse-temurin:21-jre-alpine

# 2. THE PORT: Expose the port your app runs on (8080 for batch, 8081 for the API)
EXPOSE 8080

# 3. THE PAYLOAD: Copy your compiled JAR file from your target folder into the Docker container, renaming it to 'app.jar'
COPY target/*.jar app.jar

# 4. THE IGNITION: The command that runs when the container starts.
# This is the exact same command you run in your terminal!
ENTRYPOINT ["java", "-jar", "/app.jar"]
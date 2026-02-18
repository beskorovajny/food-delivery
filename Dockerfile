FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the built jar (make sure you build first!)
COPY target/food-delivery-0.0.1-SNAPSHOT.jar app.jar

# Better security / options
EXPOSE 8080

# Add -Djava.security.egd for faster startup in containers
ENTRYPOINT ["java", "-XX:InitialRAMPercentage=75.0", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
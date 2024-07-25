FROM amazoncorretto:21-alpine

COPY target/crossfit-client-0.0.1-SNAPSHOT.jar app.jar
# Copy the inputs folder to the Docker image
COPY src/main/resources/inputs /inputs

ENTRYPOINT ["java", "-jar", "/app.jar"]
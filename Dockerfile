# Start from OpenJDK image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

COPY wait-for-it.sh /wait-for-it.sh

RUN chmod +x /wait-for-it.sh

# Add jar (built using `mvn clean package`)
COPY target/cinemetrics-app.jar app.jar

# Run the jar
CMD ["sh", "-c", "/wait-for-it.sh mysql:3306 -- java -jar app.jar" ]

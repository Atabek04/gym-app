FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/gym-app-*.jar gym-app.jar

ENTRYPOINT ["java", "-jar", "gym-app.jar"]
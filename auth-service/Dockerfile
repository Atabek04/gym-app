FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/auth-service-*.jar auth-service.jar

ENTRYPOINT ["java", "-jar", "auth-service.jar"]
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/api-gateway-*.jar api-gateway.jar

EXPOSE 8095

ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
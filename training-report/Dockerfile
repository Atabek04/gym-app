FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/training-report-*.jar training-report.jar

ENTRYPOINT ["java", "-jar", "training-report.jar"]
FROM openjdk:11-jre-slim AS spring-boot-app
COPY target/gym-app.jar /app/gym-app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/app/gym-app.jar"]

FROM prom/prometheus:v2.29.2 AS prometheus

# Create a Prometheus configuration file
COPY prometheus.yml /etc/prometheus/prometheus.yml

FROM grafana/grafana:8.3.3 AS grafana
EXPOSE 3000

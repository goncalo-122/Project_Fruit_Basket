FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/quarkus-app/ /app/

EXPOSE 8083

CMD ["java", "-Dquarkus.http.host=0.0.0.0", "-Dquarkus.http.port=8083", "-jar", "/app/quarkus-run.jar"]

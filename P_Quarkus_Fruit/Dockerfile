FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

COPY target/quarkus-app/lib/ /app/lib/
COPY target/quarkus-app/app/ /app/app/
COPY target/quarkus-app/quarkus/ /app/quarkus/
COPY target/quarkus-app/quarkus-run.jar /app/quarkus-run.jar

EXPOSE 8083
CMD ["java", "-jar", "/app/quarkus-run.jar"]

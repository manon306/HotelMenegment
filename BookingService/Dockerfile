FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/BookingService-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "sleep 25 && java -jar app.jar"]
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/flightmanagement-4.1.0.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","app.jar"]
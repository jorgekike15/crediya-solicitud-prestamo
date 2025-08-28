FROM amazoncorretto:21.0.8-alpine
WORKDIR /app
COPY applications/app-service/build/libs/crediya-solicitud-prestamo.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
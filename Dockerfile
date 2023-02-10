FROM openjdk:17-alpine
EXPOSE 8081

COPY target/*.jar /app/app.jar
WORKDIR /app

CMD ["java", "-jar", "app.jar"]

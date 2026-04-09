FROM eclipse-temurin:21-jre-alpine
LABEL authors="zsuzsibajdik"
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
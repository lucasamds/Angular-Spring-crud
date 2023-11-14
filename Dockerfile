FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/tutorials-crud-1.0.0.jar tutorials-crud-1.0.0.jar
EXPOSE 8080
CMD ["java", "-jar", "tutorials-crud-1.0.0.jar"]
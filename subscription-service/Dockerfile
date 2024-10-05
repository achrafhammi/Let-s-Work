FROM amazoncorretto:21

WORKDIR /app

COPY target/subscription-service-0.1.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS=""

CMD ["java", "-jar", "app.jar"]
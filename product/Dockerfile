FROM openjdk:11-jre-slim-buster
LABEL maintainer="localhost.com"
VOLUME /app
ADD target/product-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
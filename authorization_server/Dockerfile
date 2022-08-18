FROM openjdk:8
ADD target/authorization-server.jar authorization-server.jar
EXPOSE 9191
ENTRYPOINT ["java", "-jar", "authorization-server.jar"]
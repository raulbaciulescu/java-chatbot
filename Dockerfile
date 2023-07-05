FROM openjdk:17
COPY target/java-chatbot-0.0.1-SNAPSHOT.jar gepeto.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=cloud", "gepeto.jar"]

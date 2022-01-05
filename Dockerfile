FROM openjdk:17-alpine
COPY build/libs/*.jar matchingengine.jar
EXPOSE 8080
CMD ["java", "-jar", "matchingengine.jar"]

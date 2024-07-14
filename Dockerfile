# Use the Maven image to build the application
FROM maven:3.8.5-openjdk AS build
COPY . .
RUN mvn clean package -DskipTests

# Use a slim OpenJDK image for the runtime
FROM openjdk:21-jdk-slim
COPY --from=build /target/MusicPlay-0.0.1-SNAPSHOT.jar /MusicPlay.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/MusicPlay.jar"]

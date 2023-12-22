# Etap 1: Budowanie aplikacji za pomocą Gradle
FROM gradle:7.4.1-jdk17 as build
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . /home/gradle/project

# Budowanie aplikacji, pomiń testy dla szybkości budowania
RUN gradle build -x test --no-daemon

# Etap 2: Tworzenie ostatecznego obrazu z JDK 17
FROM openjdk:17
EXPOSE 8080
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
COPY gradle gradle/
RUN gradle build --no-daemon


FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY --from=build /app/build/libs/Logistics_Management-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
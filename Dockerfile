FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle gradle/
COPY src ./src
RUN gradle build --no-daemon

# 2단계: 실제 실행 이미지
FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY --from=build /app/build/libs/Logistics_Management-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
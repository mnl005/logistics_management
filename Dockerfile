# 1단계: Gradle 빌드 이미지
FROM gradle:8.5-jdk21 AS build

# 작업 디렉토리 설정
WORKDIR /app

# build.gradle 파일과 소스 코드 복사
COPY build.gradle settings.gradle ./
COPY gradle gradle/
COPY src ./src

# 애플리케이션 빌드
RUN gradle build --no-daemon

# 2단계: 실제 실행 이미지
FROM openjdk:21-jdk-slim

# JAR 파일을 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
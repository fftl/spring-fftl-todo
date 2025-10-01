# ---- Build stage ----
FROM gradle:8.8-jdk21-alpine AS build
WORKDIR /workspace

# 캐시 최적화: 의존성 먼저
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle gradle
RUN ./gradlew --version

# 소스 복사 & 빌드 (느리면 -x test)
COPY src src
RUN ./gradlew clean bootJar -x test

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 생성된 JAR 복사 (이름이 다르면 패턴 수정)
COPY --from=build /workspace/build/libs/*.jar app.jar

# 포트/옵션
EXPOSE 8080
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

# (선택) Actuator 사용 시 헬스체크
# HEALTHCHECK CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
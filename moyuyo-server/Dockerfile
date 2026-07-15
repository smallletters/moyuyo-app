FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache tzdata curl && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

WORKDIR /app

COPY moyuyo-api/target/*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD curl -sf http://localhost:8080/api/health || exit 1

ENTRYPOINT ["java", \
  "-jar", \
  "-Xms512m", \
  "-Xmx1024m", \
  "-XX:+UseZGC", \
  "-XX:MaxGCPauseMillis=100", \
  "-Dspring.profiles.active=prod", \
  "app.jar"]

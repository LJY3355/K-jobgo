# 1단계: 빌드
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# 2단계: 실행
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/K-jobgo_project-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

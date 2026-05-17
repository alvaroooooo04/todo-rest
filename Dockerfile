# =====================================================================
# Dockerfile - API REST Todo List (Proyecto Intermodular 2o DAW)
# Build multi-stage: compilacion con Maven y ejecucion con JRE ligero
# =====================================================================

FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -q
COPY src ./src
RUN ./mvnw clean package -DskipTests -q

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]
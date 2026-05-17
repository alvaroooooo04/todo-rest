# =====================================================================
# Dockerfile - API REST Todo List (Proyecto Intermodular 2o DAW)
# Build multi-stage: compilacion con Maven y ejecucion con JRE ligero
# =====================================================================

# ---- Etapa 1: compilacion ----
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -q
COPY src ./src
RUN ./mvnw clean package -DskipTests -q

# ---- Etapa 2: ejecucion ----
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]

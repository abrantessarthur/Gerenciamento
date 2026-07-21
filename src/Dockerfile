# ---- Build stage ----
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copia primeiro só o wrapper e o pom pra aproveitar cache de dependências
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Agora copia o resto do código e builda
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# ---- Run stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
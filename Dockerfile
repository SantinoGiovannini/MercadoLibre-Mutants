# ETAPA 1: Construcción (Build)
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
# Damos permisos de ejecución al gradlew y construimos el JAR (omitiendo tests para agilizar deploy)
RUN chmod +x ./gradlew
RUN ./gradlew bootJar -x test --no-daemon

# ETAPA 2: Ejecución (Runtime)
FROM openjdk:17-alpine
WORKDIR /app
# Copiamos solo el JAR generado desde la etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar

# Exponemos el puerto 8080 (obligatorio para Render)
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
# --- ETAPA 1: Construcción (Le llamaremos "builder") ---
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app
COPY . .
# Damos permisos y compilamos
RUN chmod +x ./gradlew
RUN ./gradlew bootJar -x test --no-daemon

# --- ETAPA 2: Ejecución (Runtime) ---
# Usamos la imagen ligera de Java que sabemos que funciona
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# OJO AQUÍ: Copiamos desde "builder" (el nombre que definimos arriba)
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
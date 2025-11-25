# CAMBIO AQUÍ: Usamos eclipse-temurin en lugar de openjdk
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copiamos el JAR generado en la etapa 1
COPY --from=build /app/build/libs/*.jar app.jar

# Exponemos el puerto
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
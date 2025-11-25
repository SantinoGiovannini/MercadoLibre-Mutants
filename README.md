# 🧬 Mutant Detector API - Guía de Ejecución

Este documento detalla los pasos necesarios para descargar, ejecutar y probar la API de detección de mutantes, tanto en entorno local como en contenedores Docker.

---

## 📋 Prerrequisitos

Para ejecutar este proyecto localmente, asegúrate de tener instalado:

1.  **Java 17 JDK o superior:** [Descargar aquí](https://adoptium.net/)
2.  **Git:** [Descargar aquí](https://git-scm.com/)
3.  **Docker Desktop** (Opcional, para ejecución en contenedores): [Descargar aquí](https://www.docker.com/products/docker-desktop/)

---

## ☁️ Acceso Rápido (Demo en la Nube)

Si prefieres probar la API sin instalar nada, el proyecto está desplegado y activo en **Render**:

* **Swagger UI (Documentación Interactiva):**
  [https://mutants-api.onrender.com/swagger-ui.html](https://mutants-api.onrender.com/swagger-ui.html)
* **Host Base:**
  `https://mutants-api.onrender.com`
* **ATENCION**: Es posible que al querer probar la api el host de Render este dormido.

---

## 🛠️ Instalación y Ejecución Local

### Paso 1: Clonar el Repositorio

Abre tu terminal y ejecuta:

```bash
git clone [https://github.com/SantinoGiovannini/MercadoLibre-Mutants.git](https://github.com/SantinoGiovannini/MercadoLibre-Mutants.git)
cd MercadoLibre-Mutants 

```

### Paso 2: Ejecutar con Gradle (Método Estándar)

Este método utiliza el wrapper de Gradle incluido en el proyecto, por lo que no necesitas tener Gradle instalado globalmente.

**En Windows:**
```powershell
./gradlew.bat bootRun
```

**En Linux:**
```powershell
./gradlew bootRun
```

Una vez que veas el mensaje Started MutantDetectorApplication, la API estará disponible en:

Swagger UI: http://localhost:8080/swagger-ui.html

H2 Console: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb

User: sa

Password: (vacío)


### Paso 3: Ejecutar con Docker (Método Aislado)
* El proyecto incluye un Dockerfile optimizado.

1. **Construir la imagen:**
```powershell
docker build -t mutant-api .
   ```

2. **Ejecutar el contenedor:**
```powershell
 docker run -p 8080:8080 mutant-api
   ```

## 🧪 Cómo Probar la API

Puedes utilizar **Postman**, **cURL** o la interfaz de **Swagger UI**.

### 1. Detectar Mutante (POST /mutant)

Envía una secuencia de ADN. Si detecta más de una secuencia de 4 letras iguales, retornará `200 OK`.

**Ejemplo cURL:**
```bash
curl -X POST [https://mutants-api.onrender.com/mutant](https://mutants-api.onrender.com/mutant) \
  -H "Content-Type: application/json" \
  -d '{
    "dna": [
      "ATGCGA",
      "CAGTGC",
      "TTATGT",
      "AGAAGG",
      "CCCCTA",
      "TCACTG"
    ]
  }'
```
* Respuesta Esperada: 200 OK (Mutante)

### 2. Detectar Humano (POST /mutant)

**Ejemplo cURL:**
```bash
curl -X POST [https://mutants-api.onrender.com/mutant](https://mutants-api.onrender.com/mutant) \
  -H "Content-Type: application/json" \
  -d '{
    "dna": [
      "ATGCGA",
      "CAGTGC",
      "TTATTT",
      "AGACGG",
      "GCGTCA",
      "TCACTG"
    ]
  }'
```
* Respuesta Esperada: 403 Forbidden (Humano)

### 3. Ver Estadísticas (GET /stats)
   **Devuelve el conteo de verificaciones y el ratio.**

**Ejemplo cURL:**

```powershell
curl -X GET [https://mutants-api.onrender.com/stats](https://mutants-api.onrender.com/stats)
```
* Respuesta JSON:

```powershell
{
"count_mutant_dna": 40,
"count_human_dna": 100,
"ratio": 0.4
}
```
---

## ✅ Ejecución de Tests Automáticos

El proyecto cuenta con una suite de pruebas robusta que garantiza una **cobertura de código superior al 90%**, utilizando **JUnit 5**, **Mockito** y **MockMvc**.

Para ejecutar las pruebas y verificar la cobertura, sigue estos pasos:

### 1. Ejecutar Tests Unitarios y de Integración
Ejecuta el siguiente comando para correr todos los tests definidos en la aplicación:

```bash
./gradlew test
```

### 2. Generar Reporte de Cobertura (JaCoCo)
Una vez finalizados los tests, genera el reporte visual de cobertura con:

```powershell
./gradlew jacocoTestReport
```

### 3. Ver el Reporte
El reporte HTML interactivo se genera en la carpeta `build`. Ábrelo en tu navegador para ver el análisis línea por línea:

* **Ruta del archivo:** `build/reports/jacoco/test/html/index.html`

**Resumen de Cobertura:**

| Componente | Cobertura | Estado |
|------------|-----------|--------|
| **Service Layer** | 100% | ✅ |
| **Controller Layer** | 100% | ✅ |
| **Algoritmo Core** | 100% | ✅ |

# 💡Un solo detalle para agregar.
En sistemas Linux o Mac, a veces cuando clonas un repositorio fresco, el archivo gradlew pierde sus permisos de ejecución. Para evitar que al evaluador le salga un error de "Permission denied", podrías agregar una pequeña línea en el Paso 2.

### Sugerencia de agregado en la sección Linux/Mac:


**En Linux / Mac:**
```powershell
chmod +x gradlew    # <--- Agrega esto por seguridad
./gradlew bootRun
```
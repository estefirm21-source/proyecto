# GreenCert - Manual de Usuario

Bienvenido a **GreenCert**, tu Gestor de Huella de Carbono y Consultor de la Norma ISO 14001 impulsado por Inteligencia Artificial.

Este documento te guiará paso a paso sobre cómo configurar, ejecutar y utilizar la aplicación de consola.

---

## 1. Requisitos Previos

Antes de ejecutar la aplicación, asegúrate de tener instalado lo siguiente en tu sistema:
- **Java Development Kit (JDK) 21** o superior.
- **Apache Maven** (para la gestión de dependencias y compilación).
- **MySQL Server** (para la base de datos).
- **Git** (opcional, para clonar el repositorio).

---

## 2. Configuración Inicial

### Paso 2.1: Base de Datos
1. Inicia tu servidor MySQL (por ejemplo, a través de XAMPP, Workbench o línea de comandos).
2. Localiza el archivo `schema.sql` en la raíz del proyecto.
3. Ejecuta el script `schema.sql` en tu servidor MySQL. Esto creará:
   - La base de datos `greencert_db`.
   - Las tablas necesarias (`company`, `emission_source`, `monthly_consumption`).
   - Los datos por defecto (Electricidad, Combustible, Residuos).

### Paso 2.2: Credenciales de Conexión
1. Abre el archivo `src/main/java/DBConnection.java`.
2. Modifica el usuario y la contraseña para que coincidan con tu servidor MySQL local:
   ```java
   String url = "jdbc:mysql://localhost:3306/greencert_db";
   String user = "root";       // Cambia esto si tu usuario es distinto
   String password = "password"; // Escribe tu contraseña de MySQL aquí
   ```

### Paso 2.3: Documento PDF para la IA
La aplicación utiliza un archivo llamado `iso14001.pdf` para responder preguntas (RAG).
- Asegúrate de tener un archivo PDF válido llamado `iso14001.pdf` en la **raíz del proyecto** (la misma carpeta donde está el archivo `pom.xml`).

---

## 3. Ejecución de la Aplicación

Abre tu terminal o línea de comandos, navega hasta la carpeta del proyecto y ejecuta los siguientes comandos:

1. **Compilar el proyecto:**
   ```bash
   mvn clean compile
   ```
2. **Ejecutar la aplicación:**
   ```bash
   mvn exec:java -Dexec.mainClass="GreenCertApplication"
   ```

---

## 4. Uso de la Aplicación (Menú Principal)

Al iniciar, verás el siguiente menú en la consola:

```text
===== GREENCERT: CARBON FOOTPRINT MANAGER =====
1. Register new consumption
2. View complete history
3. View annual report
4. Consult ISO 14001 Standard (AI)
5. Exit
Select an option: 
```

### Opción 1: Register new consumption (Registrar nuevo consumo)
Permite ingresar un nuevo registro de contaminación para una empresa.
- **Company ID:** Ingresa el ID de la empresa (asegúrate de que exista en la tabla `company` de la BD).
- **Type:** Selecciona el recurso que contamina (1 para Electricidad, 2 para Combustible, 3 para Residuos).
- **Month / Year:** Mes (1-12) y Año (ej. 2024).
- **Amount:** La cantidad consumida (ej. 150.5).
- **Emission factor:** El factor de conversión. Puedes ingresar `0` para que el sistema utilice los valores estándar automáticos regidos por los principios de la POO en el código.

### Opción 2: View complete history (Ver historial completo)
Imprime en pantalla una lista de todos los registros de consumo guardados en la base de datos, incluyendo la cantidad cruda y los kilogramos de CO2 equivalentes generados.

### Opción 3: View annual report (Ver reporte anual)
Calcula la suma total de emisiones de CO2 generadas por una empresa específica durante un año entero.
- Ingresa el **Company ID** y el **Year**.
- El sistema devolverá el Total CO2 en kilogramos.

### Opción 4: Consult ISO 14001 Standard (Consultor IA)
Esta es la función de Inteligencia Artificial (Módulo RAG).
1. Se te pedirá que ingreses una duda o pregunta sobre normativas ambientales.
2. Escribe tu pregunta: *Ej. "What are the requirements for environmental policy?"*
3. El sistema leerá el archivo PDF, buscará las secciones más relevantes, y se las enviará a la API de **Google Gemini**.
4. Recibirás una respuesta generada por la IA basada **estrictamente** en el documento ISO 14001 proporcionado.

### Opción 5: Exit (Salir)
Cierra la conexión con la base de datos y termina la ejecución segura del programa.

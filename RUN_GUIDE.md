# Guía de Ejecución Estándar (Fuera de VS Code)

Para ejecutar tu aplicación GreenCert fuera de Visual Studio Code y asegurarte de que la IA funcione correctamente, sigue estos pasos:

## 1. Configurar la API Key
La aplicación necesita tu API Key de OpenAI para el consultor IA. Debes establecerla en tu terminal actual.

### En Windows (PowerShell):
```powershell
$env:OPENAI_API_KEY = "tu_clave_aqui"
```

### En Windows (Símbolo del sistema / CMD):
```cmd
set OPENAI_API_KEY=tu_clave_aqui
```

## 2. Ejecutar con Maven
Navega a la carpeta raíz del proyecto (`progra2/proyecto`) y ejecuta:

```bash
mvn spring-boot:run
```

## 3. Acceder a la Web
Una vez veas el mensaje `Started GreenCertApplication`, abre tu navegador en:
[http://localhost:8080](http://localhost:8080)

> [!TIP]
> Si quieres que la aplicación siga corriendo incluso si cierras la ventana de la terminal, puedes usar una terminal como 'Windows Terminal' y simplemente minimizarla. Mientras el proceso de Java esté en ejecución, la web estará disponible.

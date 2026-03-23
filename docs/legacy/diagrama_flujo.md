# Diagramas de Flujo (GreenCert)

A continuación, se presentan las representaciones lógicas de los dos procesos principales del sistema: **Registro de Consumo (Base de Datos)** y **Consulta RAG (Inteligencia Artificial)**.

Puedes visualizar estos diagramas copiando el contenido de bloques `mermaid` en [Mermaid Live Editor](https://mermaid.live/) o viéndolos directamente en GitHub.

## 1. Proceso de Registro de Consumo

Este diagrama detalla el flujo desde que el usuario ingresa datos hasta que se calcula la huella de carbono y se guarda en la base de datos MySQL de forma segura.

```mermaidb
flowchart TD
    %% Nodos
    A([Inicio: Usuario selecciona 'Registrar Consumo']) --> B[/Ingresar Datos: ID Empresa, Tipo, Mes, Año, Cant, Factor/]
    B --> C{¿Datos válidos?}
    
    C -- No --> D[/Mostrar Error de Validación/]
    D --> E([Regresar al Menú Principal])
    
    C -- Sí --> F[Main invoca a ConsumoService]
    F --> G[ConsumoService invoca a EmissionSourceFactory]
    
    G --> H{¿Qué tipo de fuente?}
    H -- Electricidad --> I[Crear Objeto Electricity]
    H -- Combustible --> J[Crear Objeto Fuel]
    H -- Residuos --> K[Crear Objeto Waste]
    
    I --> L[Calcular Huella CO2 (Polimorfismo)]
    J --> L
    K --> L
    
    L --> M[ConsumoService invoca a ConsumoRepository]
    M --> N[(Conexión a Servidor MySQL)]
    
    N --> O{¿Constraint Unique Violado?}
    O -- Sí (Registro ya existe) --> P[ON DUPLICATE KEY UPDATE: Actualiza el registro existente]
    O -- No (Registro nuevo) --> Q[INSERT: Crea nuevo registro en DB]
    
    P --> R[/Mostrar 'Éxito'/]
    Q --> R
    
    R --> S([Fin: Operación Completa])
```

## 2. Proceso de Consulta de Inteligencia Artificial (Lógica RAG)

Este diagrama explica cómo el sistema logra leer un PDF local, extraer las partes relevantes a la pregunta y consultarlo con la API del LLM (Google Gemini).

```mermaid
flowchart TD
    %% Nodos
    A([Inicio: Usuario selecciona 'Consultar Norma ISO']) --> B[/Ingresar Pregunta/]
    B --> C[Main invoca a PDFReader]
    
    C --> D[(Cargar documento iso14001.pdf)]
    D --> E[TextChunker: Dividir el texto en bloques pequeños]
    E --> F[SimpleRetriever: Buscar bloques con palabras clave]
    
    F --> G{¿Se encontró contexto relevante?}
    G -- No --> H[ISOConsultor: Genera prompt sin contexto]
    G -- Sí --> I[ISOConsultor: Forma el Prompt (Contexto + Pregunta)]
    
    H --> J[ISOConsultor invoca a LLMService]
    I --> J
    
    J --> K((GeminiLLMClient))
    K --> L[Formatear a JSON]
    L --> M{Petición HTTP a API Google Gemini}
    
    M -- HTTP 200 OK --> N[Parsear JSON de Respuesta]
    M -- Error 400/500 --> O[/Mostrar código de error API/]
    
    N --> P[/Mostrar Respuesta IA en Pantalla/]
    O --> P
    
    P --> Q([Regresar al Menú Principal])
```

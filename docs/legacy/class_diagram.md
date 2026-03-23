# UML Class Diagram

This diagram displays the complete Object-Oriented structure of the GreenCert project using Java standards. It illustrates all attributes, methods, visibility levels, inheritance, interfaces, and SOLID principle applications.

```mermaid
classDiagram
    %% Core Domain - Interfaces and Abstractions
    class CarbonCalculable {
        <<interface>>
        +calculateCarbonFootprint() double
    }

    class EmissionSource {
        <<abstract>>
        -emissionFactor double
        +EmissionSource(emissionFactor double)
        #getEmissionFactor() double
        +calculateCarbonFootprint()* double
    }
    CarbonCalculable <|-- EmissionSource

    %% Domain Entities
    class Electricity {
        -kwh double
        +Electricity(kwh double, factor double)
        +getKwh() double
        +calculateCarbonFootprint() double
    }
    EmissionSource <|-- Electricity

    class Fuel {
        -liters double
        +Fuel(liters double, factor double)
        +getLiters() double
        +calculateCarbonFootprint() double
    }
    EmissionSource <|-- Fuel

    class Waste {
        -kg double
        +Waste(kg double, factor double)
        +getKg() double
        +calculateCarbonFootprint() double
    }
    EmissionSource <|-- Waste

    %% Factory (Open/Closed Principle)
    class EmissionSourceFactory {
        -REGISTRY Map~String, BiFunction~
        +registerSource(type String, constructor BiFunction)
        +createSource(type String, value double, factor double) EmissionSource
    }
    EmissionSourceFactory ..> EmissionSource : creates

    %% Database & Persistence (Repository Pattern)
    class ConsumptionRepository {
        <<interface>>
        +insertConsumption(idCompany int, idSource int, month int, year int, amount double, co2 double) void
        +getHistory() List~String~
        +getAnnualSum(idCompany int, year int) double
    }

    class ConsumptionDAO {
        +insertConsumption(idCompany int, idSource int, month int, year int, amount double, co2 double) void
        +getHistory() List~String~
        +getAnnualSum(idCompany int, year int) double
    }
    ConsumptionRepository <|-- ConsumptionDAO

    class DBConnection {
        +connect() Connection
    }
    ConsumptionDAO ..> DBConnection : uses

    %% Application Service Layer
    class ConsumptionService {
        <<interface>>
        +registerConsumption(idCompany int, type int, month int, year int, amount double, factor double) void
        +listHistory() void
        +calculateAnnualFootprint(idCompany int, year int) double
    }

    class ConsumptionServiceImpl {
        -repository ConsumptionRepository
        +ConsumptionServiceImpl(repository ConsumptionRepository)
        +registerConsumption(idCompany int, type int, month int, year int, amount double, factor double) void
        +listHistory() void
        +calculateAnnualFootprint(idCompany int, year int) double
    }
    ConsumptionService <|-- ConsumptionServiceImpl
    ConsumptionServiceImpl o-- ConsumptionRepository : uses (Dependency Inversion)
    ConsumptionServiceImpl ..> EmissionSourceFactory : uses

    %% AI Integration Layer
    class LLMService {
        <<interface>>
        +generateResponse(context String, question String) String
    }

    class GeminiLLMClient {
        -API_KEY String
        -API_URL String
        +GeminiLLMClient(apiKey String)
        +generateResponse(context String, question String) String
    }
    LLMService <|-- GeminiLLMClient

    class ISOConsultant {
        -llmService LLMService
        +ISOConsultant(llmService LLMService)
        +processQuery(contextChunks List~String~, question String) String
    }
    ISOConsultant o-- LLMService : uses (Dependency Inversion)

    %% Utility Classes for RAG
    class PDFReader {
        +readPDF(path String) String
    }

    class TextChunker {
        +splitText(text String, chunkSize int) List~String~
    }

    class SimpleRetriever {
        +searchChunks(chunks List~String~, question String) List~String~
    }

    %% Main Application Entry
    class GreenCertApplication {
        -scanner Scanner
        -service ConsumptionService
        +main(args String[])
        -registerNewConsumption()
        -viewAnnualReport()
        -executeISOQuery()
    }

    GreenCertApplication ..> ConsumptionService : orchestrates
    GreenCertApplication ..> ISOConsultant : orchestrates
    GreenCertApplication ..> PDFReader : uses
    GreenCertApplication ..> TextChunker : uses
    GreenCertApplication ..> SimpleRetriever : uses
```

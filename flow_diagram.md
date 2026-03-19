# Sequence and Logic Flow Diagram

This diagram displays the logical sequence of processes between the User, the Application, the Database, and the AI components. This format (Sequence Diagram) represents the interactions much more clearly than a standard flowchart.

```mermaid
sequenceDiagram
    actor User
    participant App as GreenCertApplication
    participant Svc as ConsumptionService
    participant DB as Database (MySQL)
    participant AI as ISOConsultant
    participant Gemini as Google Gemini API

    User->>App: Starts Application
    loop Main Menu
        App->>User: Displays Menu Options
        User->>App: Selects an Option
        
        alt Option 1: Register Consumption
            User->>App: Inputs consumption details (Type, Amount, etc.)
            App->>Svc: registerConsumption(...)
            Note over Svc: EmissionSourceFactory<br/>creates specific source
            Svc->>Svc: Calculates CO2 Generation
            Svc->>DB: insertConsumption(...)
            DB-->>Svc: DB Insert Confirmation
            Svc-->>App: Success Processed
            App->>User: Displays Success Message
            
        else Option 2 & 3: History & Reports
            User->>App: Requests History or Report
            App->>Svc: Query request (e.g. calculateAnnualFootprint)
            Svc->>DB: Executes SQL SELECT (getAnnualSum)
            DB-->>Svc: Returns queried data
            Svc-->>App: Formats response
            App->>User: Displays Data on Console
            
        else Option 4: AI ISO Consultant
            User->>App: Questions about ISO 14001
            Note over App: PDFReader extracts text<br/>TextChunker prepares chunks<br/>SimpleRetriever finds context
            App->>AI: processQuery(contextChunks, question)
            AI->>Gemini: Sends Prompt + Context (RAG)
            Gemini-->>AI: Returns AI Answer
            AI-->>App: Formatted advice
            App->>User: Displays Consultant Output
            
        else Option 5: Exit
            User->>App: Selects Exit
            App->>User: Terminates application
        end
    end
```

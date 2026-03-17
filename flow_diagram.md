# Activity and Flow Diagram

This diagram represents the logical flow of the main processes within the GreenCert console application, including consumption registration, database queries, and the AI Consulting logic.

```mermaid
flowchart TD
    Start([Start Application]) --> Menu{Main Menu}
    
    %% Option 1: Register Consumption
    Menu -->|1. Register Consumption| InputCons[Input: Company ID, Type, Date, Amount]
    InputCons --> Fact[EmissionSourceFactory creates Source]
    Fact --> Calc[Calculate CO2 Footprint]
    Calc --> DBIns[ConsumptionDAO invokes insertConsumption]
    DBIns --> DB[(MySQL Database)]
    DB --> Menu
    
    %% Option 2: View History
    Menu -->|2. View History| ViewHist[ConsumptionDAO invokes getHistory]
    ViewHist --> DB
    DB --> DisplayHist[Display History on Console]
    DisplayHist --> Menu
    
    %% Option 3: Annual Report
    Menu -->|3. Annual Report| InputRep[Input: Company ID, Year]
    InputRep --> DBRep[ConsumptionDAO invokes getAnnualSum]
    DBRep --> DB
    DB --> DisplayRep[Display CO2 Total]
    DisplayRep --> Menu
    
    %% Option 4: AI ISO Consultant
    Menu -->|4. AI ISO Consultant| InputQuest[Input: Question]
    InputQuest --> ReadPDF[PDFReader reads iso14001.pdf]
    ReadPDF --> Chunk[TextChunker splits text into chunks]
    Chunk --> Ret[SimpleRetriever finds matching chunks based on query keywords]
    Ret --> AI[ISOConsultant passes context and question to GeminiLLMClient]
    AI --> API([Call Google Gemini API])
    API --> DisplayAI[Display AI Generated Answer]
    DisplayAI --> Menu
    
    %% Option 5: Exit
    Menu -->|5. Exit| End([Exit Application])
```

# Software Requirements Specification (SRS)
## GreenCert Application

### 1. Introduction
This document outlines the formal software requirements for the GreenCert application. It includes User Stories (functional requirements) and Non-Functional Requirements (NFRs) to fulfill the lifecycle management evaluated in the rubric.

### 2. User Stories (Functional Requirements)

**Epic 1: Object-Oriented Core and Carbon Calculation**
- **US-1.1**: As an environmental auditor, I want to input my electricity consumption (kWh), so that the system calculates my carbon footprint using precise formulas.
- **US-1.2**: As a logistics manager, I want to register fuel usage (Liters), so that the system can apply the specific tracking strategy for fossil fuels.

**Epic 2: Data Persistence (SQL)**
- **US-2.1**: As an administrator, I want the system to automatically save all emission records into an SQL database, so that historical data is permanently stored.
- **US-2.2**: As an auditor, I want to view a history table of all registered emissions, so that I can analyze past carbon footprints visually.

**Epic 3: AI Iso Consultant (RAG)**
- **US-3.1**: As a user preparing for certification, I want to chat with an AI agent powered by Gemini, so that I can ask specific questions about the ISO 14001 standard.
- **US-3.2**: As a user, I want the system to automatically load the `iso14001.pdf` file upon startup, so that the AI's answers are context-aware and strictly based on the official PDF.

**Epic 4: Web Interface (Spring Boot)**
- **US-4.1**: As an end-user, I want to access the application through a web browser, so that I don't have to use a command-line interface.
- **US-4.2**: As an end-user, I want the web interface to look modern and premium, using an aesthetic dark mode and green accents, so that the experience feels professional.

### 3. Non-Functional Requirements (NFR)

1. **Performance (Efficiency)**: The system must execute SQL queries (inserts and reads) rapidly using connection management (`DatabaseManager`). 
2. **Reliability**: The system must enforce database referential integrity and strictly handle SQL exceptions using transaction rollbacks to prevent data corruption. 
3. **Usability**: The web interface must be responsive (mobile-friendly) and provide clear visual feedback when carbon calculations are completed.
4. **Maintainability**: The backend code must strictly follow SOLID principles (e.g., Open/Closed Principle for `EmissionSource`) and avoid "God Classes" to ensure high cohesion and low coupling.
5. **Language**: The entire technical documentation, README, User Manual, and inline code comments must be written in professional Technical English to foster global competence.

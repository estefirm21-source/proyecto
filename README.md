<h1 align="center">
  🌱 GreenCert 
</h1>

<p align="center">
  <strong>Carbon Footprint Manager & ISO 14001 AI Consultant</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java"/>
  <img src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL"/>
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven"/>
  <img src="https://img.shields.io/badge/Google_Gemini-8E75B2?style=for-the-badge&logo=googlegemini&logoColor=white" alt="Gemini AI"/>
</p>

---

## 📖 About The Project

GreenCert is a comprehensive enterprise application designed to help companies calculate their carbon emissions from various sources (Electricity, Fuel, Waste). Furthermore, it features an **AI-powered consultant** built using **Retrieval-Augmented Generation (RAG)** to answer compliance questions regarding the ISO 14001 Environmental Management System standard.

The core of the application strictly adheres to the **SOLID principles** and Clean Architecture, demonstrating advanced Object-Oriented design patterns.

##  Core Features

-  **Robust OOP Architecture**: Built using `Factory` and `Strategy` patterns, strictly adhering to the Open/Closed Principle (OCP) for Emission Sources.
-  **Repository Pattern**: Clean separation of concerns between business logic (`ConsumptionService`) and data access layers (`ConsumptionRepository`).
-  **Relational Database**: Advanced MySQL schema featuring foreign keys, `ON DELETE CASCADE` constraints, referential integrity, and indexed tables for optimized reports.
-  **RAG AI Integration**: Parses a local `iso14001.pdf` file, splits text into discrete chunks, performs context retrieval, and interacts with the Google Gemini API to generate accurate, context-aware advice.

## 📐 Architecture & Diagrams

Explore the system's architecture natively rendered by GitHub Mermaid:
-  **[UML Class Diagram](class_diagram.md)**: Visualizes the exhaustive OOP structure, interfaces, and SOLID principles.
-  **[Flow & Sequence Diagram](flow_diagram.md)**: Details the logical execution flow of the application processes, database constraints, and the AI Pipeline.

---

## 🚀 Getting Started

Follow these steps to run GreenCert locally on your machine. For detailed usage instructions, please refer to the **[USER MANUAL (Español)](USER_MANUAL.md)**.

### 1. Database Setup
Execute the `schema.sql` file in your MySQL environment to create the tables and default emission sources.
```bash
mysql -u your_user -p < schema.sql
```

### 2. Configure Credentials
Update your MySQL credentials inside the Data Access Object configuration `src/main/java/DBConnection.java`:
```java
String url = "jdbc:mysql://localhost:3306/greencert_db";
String user = "root";       // Change this
String password = "password"; // Change this
```

### 3. Provide ISO Document
Ensure a valid PDF file named `iso14001.pdf` is placed in the **project root folder**, as it is strictly required by the AI Consultant module.

### 4. Build & Execute
Compile the project using Maven and run the Spring Boot Web Application:
```bash
mvn clean install
mvn spring-boot:run
```
Once it starts, open your browser and navigate to **`http://localhost:8080`** to access the Premium Web UI and the AI Chat Consultant.

---

##  Roadmap

- [x] Phase 1: Core System (Java Console, SOLID Principles, SQLite/SQL).
- [x] Phase 2: RAG AI Implementation (PDF Text Extraction + Gemini LLM).
- [x] **Phase 3: Web Migration**: Transformed the application into a Spring Boot Web App using HTML/CSS for an aesthetic Premium UI.

# GreenCert

GreenCert is a Carbon Footprint Manager and ISO 14001 Audit Consultant. It is designed to help companies calculate their carbon emissions from various sources (Electricity, Fuel, Waste) and provides an AI-powered consultant to answer questions regarding the ISO 14001 Environmental Management System standard using Retrieval-Augmented Generation (RAG).

## Features

- **Object-Oriented Core Architecture**: Built on clean domain models utilizing SOLID principles (Strategy, Factory, Open/Closed patterns).
- **Service & Repository Pattern**: Clean separation of concerns between business logic (`ConsumptionService`) and data access (`ConsumptionRepository`).
- **Relational Database**: Robust MySQL database schema with constraints, cascading deletes, and indexes for optimized historical reporting.
- **RAG AI Consultant**: Reads a local `iso14001.pdf` file, chunks the text, retrieves relevant context based on user queries, and interacts with the Google Gemini API to generate accurate, context-aware responses.

## Tech Stack

- **Java 21**
- **Maven**
- **MySQL (Connector/J)**
- **Apache PDFBox** (for PDF parsing)
- **Gson** (for JSON handling)
- **Google Gemini API** (LLM Provider)

## Setup & Execution

### 1. Database Setup
Execute the `schema.sql` file in your MySQL database to create the required tables, keys, and initial default data.

```bash
mysql -u your_user -p < schema.sql
```

### 2. Configure Database Connection
Set your database credentials inside `src/main/java/DBConnection.java`:

```java
String url = "jdbc:mysql://localhost:3306/greencert_db";
String user = "root";       // Change this to your mysql user
String password = "password"; // Change this to your mysql password
```

### 3. Build & Run Application
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="GreenCertApplication"
```

## Upcoming Implementation: Web Interface
Phase 2 of the project includes migrating this pure Java Console application into a Spring Boot application using Thymeleaf for the frontend, bringing GreenCert to the web browser.

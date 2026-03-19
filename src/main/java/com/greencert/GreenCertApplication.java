package com.greencert;

import com.greencert.ai.DocumentIngestor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GreenCertApplication {
    public static void main(String[] args) {
        System.out.println("=== Starting GreenCert Application ===");
        
        // Cargar contexto ISO 14001 automáticamente (RAG)
        try {
            System.out.println("Cargando Norma Ambiental (PDF) en la memoria de la IA...");
            DocumentIngestor.ingestPdf("iso14001.pdf");
            System.out.println("Norma ISO 14001 cargada correctamente.");
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudo cargar el archivo iso14001.pdf. Asegúrate de que el documento exista (" + e.getMessage() + ")");
        }

        SpringApplication.run(GreenCertApplication.class, args);
        
        System.out.println("=== GreenCert Web Application Started ===");
        System.out.println("Abra su navegador en: http://localhost:8080");
    }
}

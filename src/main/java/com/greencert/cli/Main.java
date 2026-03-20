package com.greencert.cli;

import com.greencert.ai.DocumentIngestor;
import com.greencert.ai.IsoConsultantAgent;
import com.greencert.ai.AiServiceManager;
import com.greencert.core.factory.EmissionSourceFactory;
import com.greencert.core.model.EmissionSource;
import com.greencert.db.dao.EmissionRecordDAO;
import com.greencert.db.dao.EmissionRecordDAOImpl;
import com.greencert.db.model.EmissionRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final EmissionRecordDAO recordDAO = new EmissionRecordDAOImpl();
    private static IsoConsultantAgent aiAgent;

    public static void main(String[] args) {
        System.out.println("=== Iniciando Sistema GreenCert ===");
        
        try {
            // Inicializar IA de forma perezosa (solo si se necesita o al inicio)
            System.out.println("Inicializando motor de Inteligencia Artificial...");
            aiAgent = AiServiceManager.getAgent();
            System.out.println("Motor IA inicializado.\n");
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudo inicializar la IA. Verifica tu OPENAI_API_KEY.");
        }

        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Registrar nuevo consumo (POO + SQL)");
            System.out.println("2. Ver historial de emisiones (SQL)");
            System.out.println("3. Cargar manual ISO 14001 (RAG PDF)");
            System.out.println("4. Consultar al asistente de IA (RAG Chat)");
            System.out.println("5. Salir");
            System.out.print("Selecciona una opción: ");
            
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    registerConsumption();
                    break;
                case "2":
                    viewHistory();
                    break;
                case "3":
                    ingestPdfDocument();
                    break;
                case "4":
                    chatWithAgent();
                    break;
                case "5":
                    exit = true;
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void registerConsumption() {
        System.out.println("\n-- Registrar Consumo --");
        System.out.println("Tipos disponibles: electricity (kWh), fuel (Litros), waste (Kg)");
        System.out.print("Ingrese el tipo de fuente: ");
        String type = scanner.nextLine();
        
        System.out.print("Ingrese la cantidad: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Cantidad inválida.");
            return;
        }

        try {
            // 1. Uso de POO (Factory y Polimorfismo)
            EmissionSource source = EmissionSourceFactory.createEmissionSource(type, amount);
            double calculatedCarbon = source.calculateCarbonFootprint();
            
            System.out.println("Resultado: " + source.getReportDetails());

            // 2. Uso de Persistencia (SQL)
            EmissionRecord record = new EmissionRecord(type, amount, calculatedCarbon, LocalDate.now());
            recordDAO.save(record);
            System.out.println("¡Registro guardado exitosamente en la base de datos!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewHistory() {
        System.out.println("\n-- Historial de Emisiones --");
        List<EmissionRecord> records = recordDAO.findAll();
        
        if (records.isEmpty()) {
            System.out.println("No hay registros almacenados.");
            return;
        }
        
        for (EmissionRecord record : records) {
            System.out.println(record.toString());
        }
    }

    private static void ingestPdfDocument() {
        System.out.println("\n-- Cargar PDF de Norma ISO --");
        System.out.println("Asegúrese de tener un archivo PDF en la ruta especificada.");
        System.out.print("Ruta del archivo PDF (ej. src/main/resources/iso.pdf): ");
        String path = scanner.nextLine();
        
        DocumentIngestor.ingestPdf(path);
    }

    private static void chatWithAgent() {
        if (aiAgent == null) {
            System.out.println("El Agente IA no está disponible.");
            return;
        }
        
        System.out.println("\n-- Chat con Consultor ISO 14001 --");
        System.out.println("Escribe 'salir' para volver al menú principal.");
        
        while (true) {
            System.out.print("\nTú: ");
            String message = scanner.nextLine();
            
            if ("salir".equalsIgnoreCase(message.trim())) {
                break;
            }
            
            System.out.println("Consultor ISO está pensando...");
            try {
                String response = aiAgent.chat(message);
                System.out.println("\nConsultor ISO:\n" + response);
            } catch (Exception e) {
                System.out.println("Ocurrió un error al comunicarse con la IA: " + e.getMessage());
            }
        }
    }
}
import java.util.Scanner;

public class GreenCertApplication {
    private static final Scanner scanner = new Scanner(System.in);
    private static ConsumptionService service;

    public static void main(String[] args) {
        // Initialize Layers
        ConsumptionRepository repository = new ConsumptionDAO();
        service = new ConsumptionServiceImpl(repository);

        boolean exit = false;
        while (!exit) {
            System.out.println("\n===== GREENCERT: CARBON FOOTPRINT MANAGER =====");
            System.out.println("1. Register new consumption");
            System.out.println("2. View complete history");
            System.out.println("3. View annual report");
            System.out.println("4. Consult ISO 14001 Standard (AI)");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1" -> registerNewConsumption();
                case "2" -> service.listHistory();
                case "3" -> viewAnnualReport();
                case "4" -> executeISOQuery();
                case "5" -> exit = true;
                default -> System.out.println("Invalid option.");
            }
        }
        System.out.println("Thank you for using GreenCert!");
    }

    private static void registerNewConsumption() {
        try {
            System.out.print("Company ID (e.g., 1): ");
            int idEmp = Integer.parseInt(scanner.nextLine());
            System.out.println("Type: 1.Electricity | 2.Fuel | 3.Waste");
            int type = Integer.parseInt(scanner.nextLine());
            System.out.print("Month (1-12): ");
            int month = Integer.parseInt(scanner.nextLine());
            System.out.print("Year: ");
            int year = Integer.parseInt(scanner.nextLine());
            System.out.print("Amount: ");
            double amount = Double.parseDouble(scanner.nextLine());
            System.out.print("Emission factor (leave 0 to use default): ");
            double factor = Double.parseDouble(scanner.nextLine());

            // If factor is 0, use standard values
            if (factor == 0) {
                factor = switch (type) {
                    case 1 -> 0.5;
                    case 2 -> 2.3;
                    default -> 1.8;
                };
            }

            service.registerConsumption(idEmp, type, month, year, amount, factor);
        } catch (NumberFormatException e) {
            System.out.println("Input data error: Make sure to enter valid numbers.");
        } catch (IllegalArgumentException e) {
            System.out.println("Logical error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private static void viewAnnualReport() {
        try {
            System.out.print("Company ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Year: ");
            int year = Integer.parseInt(scanner.nextLine());
            double total = service.calculateAnnualFootprint(id, year);
            System.out.printf("--- ANNUAL REPORT %d ---\nTotal CO2: %.2f kg\n--------------------------\n", year,
                    total);
        } catch (NumberFormatException e) {
            System.out.println("Error: You must enter a correct numeric value.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void executeISOQuery() {
        System.out.print("Write your question about the ISO 14001 standard: ");
        String question = scanner.nextLine();

        String pdfPath = "iso14001.pdf";
        String apiKey = "AIzaSyAiKR2V3TGRFSt4sVikaCJif9rsNT-eR_8";

        LLMService llm = new GeminiLLMClient(apiKey);
        ISOConsultant consultant = new ISOConsultant(llm);

        try {
            String text = PDFReader.readPDF(pdfPath);
            if (text == null || text.isEmpty()) {
                System.out.println("Could not read the PDF standard file.");
                return;
            }

            var chunks = TextChunker.splitText(text, 1000);
            var results = SimpleRetriever.searchChunks(chunks, question);

            System.out.println("\nProcessing with AI...");
            String response = consultant.processQuery(results, question);

            System.out.println("\nCONSULTANT RESPONSE:");
            System.out.println(response);
        } catch (Exception e) {
            System.err.println("Error in the RAG module: " + e.getMessage());
        }
    }
}

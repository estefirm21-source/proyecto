import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.util.in != null ? System.in : null);
    private static ConsumoService service;

    public static void main(String[] args) {
        // Inicializar Capas
        ConsumoRepository repository = new ConsumoDAO();
        service = new ConsumoServiceImpl(repository);

        if (scanner == null) {
            System.err.println("No se pudo inicializar la entrada de datos.");
            return;
        }

        boolean salir = false;
        while (!salir) {
            System.out.println("\n===== GREENCERT: GESTOR DE HUELLA DE CARBONO =====");
            System.out.println("1. Registrar nuevo consumo");
            System.out.println("2. Ver historial completo");
            System.out.println("3. Ver reporte anual");
            System.out.println("4. Consultar Norma ISO 14001 (IA)");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    registrarNuevoConsumo();
                    break;
                case "2":
                    service.listarHistorial();
                    break;
                case "3":
                    verReporteAnual();
                    break;
                case "4":
                    ejecutarConsultaISO();
                    break;
                case "5":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        System.out.println("¡Gracias por usar GreenCert!");
    }

    private static void registrarNuevoConsumo() {
        try {
            System.out.print("ID Empresa (ej. 1): ");
            int idEmp = Integer.parseInt(scanner.nextLine());
            System.out.println("Tipo: 1.Electricidad | 2.Combustible | 3.Residuos");
            int tipo = Integer.parseInt(scanner.nextLine());
            System.out.print("Mes (1-12): ");
            int mes = Integer.parseInt(scanner.nextLine());
            System.out.print("Año: ");
            int anio = Integer.parseInt(scanner.nextLine());
            System.out.print("Cantidad: ");
            double cant = Double.parseDouble(scanner.nextLine());
            System.out.print("Factor de emisión (deja 0 para usar por defecto): ");
            double factor = Double.parseDouble(scanner.nextLine());

            // Si el factor es 0, usamos valores estándar
            if (factor == 0) {
                if (tipo == 1)
                    factor = 0.5;
                else if (tipo == 2)
                    factor = 2.3;
                else
                    factor = 1.8;
            }

            service.registrarConsumo(idEmp, tipo, mes, anio, cant, factor);
        } catch (Exception e) {
            System.out.println("Error en los datos ingresados: " + e.getMessage());
        }
    }

    private static void verReporteAnual() {
        try {
            System.out.print("ID Empresa: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Año: ");
            int anio = Integer.parseInt(scanner.nextLine());
            double total = service.calcularHuellaAnual(id, anio);
            System.out.printf("--- REPORTE ANUAL %d ---\nTotal CO2: %.2f kg\n--------------------------\n", anio,
                    total);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void ejecutarConsultaISO() {
        System.out.print("Escriba su duda sobre la norma ISO 14001: ");
        String pregunta = scanner.nextLine();

        String rutaPDF = "iso14001.pdf";
        String apiKey = "AIzaSyAiKR2V3TGRFSt4sVikaCJif9rsNT-eR_8";

        LLMService llm = new GeminiLLMClient(apiKey);
        ISOConsultor consultor = new ISOConsultor(llm);

        try {
            String texto = PDFReader.leerPDF(rutaPDF);
            if (texto == null || texto.isEmpty()) {
                System.out.println("No se pudo leer el archivo PDF de la norma.");
                return;
            }

            var chunks = TextChunker.dividirTexto(texto, 1000);
            var resultados = SimpleRetriever.buscarChunks(chunks, pregunta);

            System.out.println("\nProcesando con IA...");
            String respuesta = consultor.procesarConsulta(resultados, pregunta);

            System.out.println("\nRESPUESTA DEL CONSULTOR:");
            System.out.println(respuesta);
        } catch (Exception e) {
            System.err.println("Error en el módulo RAG: " + e.getMessage());
        }
    }
}
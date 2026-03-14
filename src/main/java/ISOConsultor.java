import java.util.List;

/**
 * Orquestador de consultas para la norma ISO 14001.
 * Utiliza un LLM para procesar la información recuperada.
 */
public class ISOConsultor {

    private final LLMService llmService;

    public ISOConsultor(LLMService llmService) {
        this.llmService = llmService;
    }

    public String procesarConsulta(List<String> contexto, String pregunta) {
        if (contexto == null || contexto.isEmpty()) {
            return "No se encontró información relevante en los documentos de la norma.";
        }

        // Delegar la generación al servicio de LLM
        return llmService.generarRespuesta(contexto, pregunta);
    }
}
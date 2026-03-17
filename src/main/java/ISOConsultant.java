import java.util.List;

/**
 * Query orchestrator for the ISO 14001 standard.
 * Uses an LLM to process the retrieved information.
 */
public class ISOConsultant {

    private final LLMService llmService;

    public ISOConsultant(LLMService llmService) {
        this.llmService = llmService;
    }

    public String processQuery(List<String> context, String question) {
        if (context == null || context.isEmpty()) {
            return "No relevant information was found in the standard's documents.";
        }

        // Delegate generation to the LLM service
        return llmService.generateResponse(context, question);
    }
}

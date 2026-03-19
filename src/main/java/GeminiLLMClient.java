import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Client for the Google Gemini API (or other compatible provider).
 * Robustly implements LLMService.
 */
public class GeminiLLMClient implements LLMService {

    private final String apiKey;
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    public GeminiLLMClient(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String generateResponse(List<String> context, String question) {
        if (context.isEmpty())
            return "There is not enough context to answer.";

        String prompt = """
                You are an expert consultant on the ISO 14001 standard. Based EXCLUSIVELY on the following context, answer the user's question.
                
                CONTEXT:
                %s
                
                QUESTION: %s""".formatted(String.join("\n", context), question);

        // Escape characters for manual JSON
        String jsonPayload = String.format(
                "{ \"contents\": [{ \"parts\":[{ \"text\": \"%s\" }] }] }",
                prompt.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r"));

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            if (response.statusCode() == 200) {
                // More robust extraction looking for the first occurrence of "text" inside
                // "candidates"
                int textPos = body.indexOf("\"text\": \"");
                if (textPos != -1) {
                    int start = textPos + 9;
                    int end = body.indexOf("\"", start);
                    if (end != -1) {
                        return body.substring(start, end)
                                .replace("\\n", "\n")
                                .replace("\\\"", "\"")
                                .replace("\\\\", "\\");
                    }
                }
                return "Response received but with unexpected format.";
            } else {
                return "API Error (Status: " + response.statusCode() + "): " + body;
            }
        } catch (Exception e) {
            return "LLM connection error: " + e.getMessage();
        }
    }
}

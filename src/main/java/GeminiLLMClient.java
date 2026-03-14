import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Cliente para la API de Google Gemini (u otro proveedor compatible).
 * Implementa LLMService de forma robusta.
 */
public class GeminiLLMClient implements LLMService {

    private final String apiKey;
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    public GeminiLLMClient(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String generarRespuesta(List<String> contexto, String pregunta) {
        if (contexto.isEmpty())
            return "No hay contexto suficiente para responder.";

        String prompt = "Eres un consultor experto en la norma ISO 14001. " +
                "Basándote EXCLUSIVAMENTE en el siguiente contexto, responde la pregunta del usuario.\n\n" +
                "CONTEXTO:\n" + String.join("\n", contexto) + "\n\n" +
                "PREGUNTA: " + pregunta;

        // Escapar caracteres para JSON manual
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
                // Extracción más robusta buscando la primera ocurrencia de "text" dentro de
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
                return "Respuesta recibida pero con formato inesperado.";
            } else {
                return "Error de API (Status: " + response.statusCode() + "): " + body;
            }
        } catch (Exception e) {
            return "Error de conexión con el LLM: " + e.getMessage();
        }
    }
}

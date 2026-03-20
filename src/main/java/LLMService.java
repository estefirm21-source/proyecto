import java.util.List;

public interface LLMService {
    String generateResponse(List<String> context, String question);
}

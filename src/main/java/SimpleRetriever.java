import java.util.ArrayList;
import java.util.List;

public class SimpleRetriever {

    public static List<String> searchChunks(List<String> chunks, String question) {

        List<String> results = new ArrayList<>();

        String[] words = question.toLowerCase().split(" ");

        for (String chunk : chunks) {

            for (String word : words) {

                if (chunk.toLowerCase().contains(word)) {
                    results.add(chunk);
                    break;
                }

            }

        }

        return results;
    }
}
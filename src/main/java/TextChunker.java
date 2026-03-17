import java.util.ArrayList;
import java.util.List;

public class TextChunker {

    public static List<String> splitText(String text, int chunkSize) {

        List<String> chunks = new ArrayList<>();

        for (int i = 0; i < text.length(); i += chunkSize) {

            int end = Math.min(text.length(), i + chunkSize);
            chunks.add(text.substring(i, end));

        }

        return chunks;
    }
}
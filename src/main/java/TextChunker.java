import java.util.ArrayList;
import java.util.List;

public class TextChunker {

    public static List<String> dividirTexto(String texto, int tamañoChunk) {

        List<String> chunks = new ArrayList<>();

        for (int i = 0; i < texto.length(); i += tamañoChunk) {

            int fin = Math.min(texto.length(), i + tamañoChunk);
            chunks.add(texto.substring(i, fin));

        }

        return chunks;
    }
}
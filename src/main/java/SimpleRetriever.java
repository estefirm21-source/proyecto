import java.util.ArrayList;
import java.util.List;

public class SimpleRetriever {

    public static List<String> buscarChunks(List<String> chunks, String pregunta) {

        List<String> resultados = new ArrayList<>();

        String[] palabras = pregunta.toLowerCase().split(" ");

        for (String chunk : chunks) {

            for (String palabra : palabras) {

                if (chunk.toLowerCase().contains(palabra)) {
                    resultados.add(chunk);
                    break;
                }

            }

        }

        return resultados;
    }
}
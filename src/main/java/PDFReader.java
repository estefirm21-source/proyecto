import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFReader {

    public static String leerPDF(String ruta) {
        String texto = "";
        File archivo = new File(ruta);
        try (PDDocument documento = PDDocument.load(archivo)) {
            PDFTextStripper lector = new PDFTextStripper();
            texto = lector.getText(documento);

            System.out.println("Contenido del PDF:");
            System.out.println(texto);
        } catch (IOException e) {
            System.out.println("Error al leer el PDF");
            e.printStackTrace();
        }
        return texto;
    }
}
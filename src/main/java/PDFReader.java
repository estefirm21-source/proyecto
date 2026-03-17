import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFReader {

    public static String readPDF(String path) {
        String text = "";
        File file = new File(path);
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper reader = new PDFTextStripper();
            text = reader.getText(document);

            System.out.println("PDF Content successfully read.");
        } catch (IOException e) {
            System.out.println("Error reading the PDF file");
            e.printStackTrace();
        }
        return text;
    }
}
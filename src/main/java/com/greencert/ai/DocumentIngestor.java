package com.greencert.ai;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.data.document.splitter.DocumentSplitters;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DocumentIngestor {

    /**
     * Carga un PDF, extrae su texto, lo segmenta (chunking) y lo inserta en la base de datos vectorial in-memory.
     * @param filePath Ruta local del documento PDF.
     */
    public static void ingestPdf(String filePath) {
        System.out.println("Procesando documento PDF: " + filePath + "...");
        
        try {
            Path path = Paths.get(filePath);

            Document document = FileSystemDocumentLoader.loadDocument(path, new ApachePdfBoxDocumentParser());

            EmbeddingModel embeddingModel = AiServiceManager.getEmbeddingModel();
            EmbeddingStore<TextSegment> embeddingStore = AiServiceManager.getEmbeddingStore();

            // Configurar el Ingestor
            EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                    .documentSplitter(DocumentSplitters.recursive(500, 50)) // 500 chars por trozo, overlap de 50
                    .embeddingModel(embeddingModel)
                    .embeddingStore(embeddingStore)
                    .build();

            ingestor.ingest(document);
            System.out.println("¡Documento integrado a la memoria del Consultor ISO con éxito!");
            
        } catch (IllegalArgumentException e) {
             System.err.println("Error: Archivo no encontrado o formato incorrecto. " + e.getMessage());
        } catch (Exception e) {
             System.err.println("Error inesperado en la ingesta del PDF: " + e.getMessage());
        }
    }
}

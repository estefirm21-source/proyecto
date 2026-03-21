package com.greencert.ai;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.data.segment.TextSegment;

public class AiServiceManager {

    private static IsoConsultantAgent agent;
    private static EmbeddingStore<TextSegment> embeddingStore;
    private static EmbeddingModel embeddingModel;
    private static boolean isMockMode = false;

    public static void initialize() {
        String apiKey = System.getenv("OPENAI_API_KEY");
        
        if (apiKey == null || apiKey.trim().isEmpty() || apiKey.equals("demo") || apiKey.equals("edtpe2170")) {
            System.err.println("--- MODO DEMO ACTIVADO (Clave no válida o ausente) ---");
            isMockMode = true;
            return;
        }

        try {
            System.out.println("Initializing AI Service Manager with OpenAI...");
            // 1. Configurar Modelos de OpenAI (Chat y Embeddings)
            ChatLanguageModel chatModel = OpenAiChatModel.builder()
                    .apiKey(apiKey)
                    .modelName("gpt-4o-mini")
                    .timeout(java.time.Duration.ofSeconds(60))
                    .build();

            embeddingModel = OpenAiEmbeddingModel.builder()
                    .apiKey(apiKey)
                    .modelName("text-embedding-3-small")
                    .build();

            // 2. Configurar la Base de Datos Vectorial en Memoria
            embeddingStore = new InMemoryEmbeddingStore<>();

            // 3. Crear el Retriever
            ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                    .embeddingStore(embeddingStore)
                    .embeddingModel(embeddingModel)
                    .maxResults(3) 
                    .minScore(0.6) 
                    .build();

            // 4. Construir el Agente
            agent = AiServices.builder(IsoConsultantAgent.class)
                    .chatLanguageModel(chatModel)
                    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                    .contentRetriever(contentRetriever)
                    .build();
            
            System.out.println("Consultor IA inicializado correctamente.");
            isMockMode = false;
        } catch (Exception e) {
            System.err.println("Error crítico inicializando IA: " + e.getMessage());
            agent = null;
            isMockMode = true;
        }
    }

    public static IsoConsultantAgent getAgent() {
        if (isMockMode || agent == null) {
            // If in mock mode or agent failed to initialize, return a mock agent
            return message -> "🌿 [MODO DEMO] ¡Hola! Parece que no tienes una clave de OpenAI activa o configurada. Pero como consultor ambiental, te puedo decir que lo más importante es medir tu huella para poder reducirla. ¡Sigue registrando tus consumos!";
        }
        return agent;
    }

    public static EmbeddingStore<TextSegment> getEmbeddingStore() {
        // No need to call initialize() here, as it's called by getAgent() or explicitly
        return embeddingStore;
    }

    public static EmbeddingModel getEmbeddingModel() {
        if (embeddingModel == null) {
            initialize();
        }
        return embeddingModel;
    }
    public static boolean isMockMode() {
        return isMockMode;
    }
}

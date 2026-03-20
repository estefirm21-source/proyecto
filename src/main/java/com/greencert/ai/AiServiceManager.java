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

    public static void initialize() {
        String apiKey = System.getenv("OPENAI_API_KEY");
        
        if (apiKey == null || apiKey.trim().isEmpty() || apiKey.equals("demo")) {
            System.err.println("CRITICAL: OPENAI_API_KEY not found in environment variables.");
            System.err.println("The AI Assistant will be unavailable. Please set the environment variable and restart the application.");
            agent = null;
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
            
            System.out.println("AI Agent successfully initialized.");
        } catch (Exception e) {
            System.err.println("FAILED to initialize AI Agent: " + e.getMessage());
            agent = null;
        }
    }

    public static IsoConsultantAgent getAgent() {
        if (agent == null) {
            initialize();
        }
        return agent;
    }

    public static EmbeddingStore<TextSegment> getEmbeddingStore() {
        if (embeddingStore == null) {
            initialize();
        }
        return embeddingStore;
    }

    public static EmbeddingModel getEmbeddingModel() {
        if (embeddingModel == null) {
            initialize();
        }
        return embeddingModel;
    }
}

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
        if (apiKey == null || apiKey.trim().isEmpty()) {
            apiKey = "demo"; // Para evitar que la app explote si no hay key, fallará al hacer peticiones reais si no es demo
            System.err.println("ADVERTENCIA: No se encontró la variable de entorno OPENAI_API_KEY. El motor IA requiere una API Key válida para funcionar de verdad.");
        }

        // 1. Configurar Modelos de OpenAI (Chat y Embeddings)
        ChatLanguageModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gpt-4o-mini") // Eficiencia y rapidez para el RAG
                .build();

        embeddingModel = OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName("text-embedding-3-small")
                .build();

        // 2. Configurar la Base de Datos Vectorial en Memoria
        embeddingStore = new InMemoryEmbeddingStore<>();

        // 3. Crear el Retriever (Busca fragmentos relevantes por similitud de cosenos)
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(3) 
                .minScore(0.5) 
                .build();

        // 4. Construir el Agente (Une el LLM, la memoria y el Retriever)
        agent = AiServices.builder(IsoConsultantAgent.class)
                .chatLanguageModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(contentRetriever)
                .build();
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

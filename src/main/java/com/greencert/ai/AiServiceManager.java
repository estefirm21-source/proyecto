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
        String rawKey = System.getenv("OPENAI_API_KEY");
        String apiKey = (rawKey != null) ? rawKey.trim() : null;
        
        if (apiKey == null || apiKey.trim().isEmpty() || apiKey.equals("demo") || apiKey.equals("edtpe2170")) {
            System.err.println("--- DEMO MODE ACTIVATED (Invalid or missing API key) ---");
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
            
            System.out.println("AI Consultant initialized successfully.");
            isMockMode = false;
        } catch (Exception e) {
            System.err.println("Critical error initializing AI: " + e.getMessage());
            agent = null;
            isMockMode = true;
        }
    }

    public static String getMockResponse(String message) {
        String lowerMsg = message.toLowerCase();
        if (lowerMsg.contains("electricity") || lowerMsg.contains("power") || lowerMsg.contains("kwh")) {
            return "Excellent question. To reduce electrical consumption and align with ISO 14001, I recommend: 1) Powering off inactive equipment. 2) Migrating to LED lighting. 3) Evaluating automation during peak hours.";
        } else if (lowerMsg.contains("fuel") || lowerMsg.contains("gas") || lowerMsg.contains("vehicle") || lowerMsg.contains("car")) {
            return "The burning of fossil fuels is a major source of CO2. I suggest optimizing your logistics routes, performing regular vehicle maintenance, or incentivizing cleaner transportation options in your company.";
        } else if (lowerMsg.contains("waste") || lowerMsg.contains("garbage") || lowerMsg.contains("recycle")) {
            return "Waste management is vital. Apply the 3Rs rule (Reduce, Reuse, Recycle). Separating waste at its source will significantly help you comply with the environmental components of the ISO standard.";
        } else if (lowerMsg.contains("iso") || lowerMsg.contains("14001") || lowerMsg.contains("standard")) {
            return "The ISO 14001 standard establishes the requirements for an Environmental Management System (EMS). Its goal is to prevent pollution, ensure legal compliance, and engage the company socially.";
        } else if (lowerMsg.contains("thanks") || lowerMsg.contains("ok")) {
            return "You are welcome. I remain at your disposal for any other inquiries regarding corporate sustainability and ISO 14001.";
        } else {
            return "As an expert environmental consultant, I suggest you constantly measure every productive aspect of your organization. Do you have any specific doubts regarding electricity, fuels, or solid waste?";
        }
    }

    public static IsoConsultantAgent getAgent() {
        if (isMockMode || agent == null) {
            // Return a smart mock agent that simulates understanding
            return message -> getMockResponse(message);
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

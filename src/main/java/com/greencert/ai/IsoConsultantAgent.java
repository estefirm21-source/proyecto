package com.greencert.ai;

import dev.langchain4j.service.SystemMessage;

public interface IsoConsultantAgent {
    
    /**
     * @param userMessage Pregunta del usuario relacionada a temas de certificación y huella de carbono.
     * @return Respuesta fundamentada en las normas ISO ingestas.
     */
    @SystemMessage("Eres un consultor experto en la norma ISO 14001 y sistemas de gestión ambiental. " +
                   "Responde a las preguntas del usuario basándote únicamente en los manuales y documentos proporcionados " +
                   "en el contexto. Si no encuentras la respuesta en el contexto, indica clara y amablemente que la información " +
                   "no está en los manuales proporcionados. Sé profesional y preciso.")
    String chat(String userMessage);
}

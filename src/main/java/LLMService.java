/**
 * Interfaz para el servicio de Lenguaje Natural (LLM).
 * Sigue el principio de Inversión de Dependencias (DIP).
 */
public interface LLMService {
    /**
     * Genera una respuesta basada en un contexto y una pregunta.
     * 
     * @param contexto Lista de fragmentos de texto relevantes.
     * @param pregunta La consulta del usuario.
     * @return Respuesta generada por el modelo.
     */
    String generarRespuesta(java.util.List<String> contexto, String pregunta);
}

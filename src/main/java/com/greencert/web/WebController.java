package com.greencert.web;

import com.greencert.ai.AiServiceManager;
import com.greencert.ai.IsoConsultantAgent;
import com.greencert.core.factory.EmissionSourceFactory;
import com.greencert.core.model.EmissionSource;
import com.greencert.db.dao.EmissionRecordDAO;
import com.greencert.db.model.EmissionRecord;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class WebController {

    private final EmissionRecordDAO recordDAO;
    private final IsoConsultantAgent aiAgent;

    public WebController(EmissionRecordDAO recordDAO, IsoConsultantAgent aiAgent) {
        this.recordDAO = recordDAO;
        this.aiAgent = aiAgent;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<EmissionRecord> records = recordDAO.findAll();
        model.addAttribute("records", records);
        
        // --- Estadísticas para Chart.js ---
        double eTotal = records.stream().filter(r -> "electricity".equals(r.getSourceType())).mapToDouble(EmissionRecord::getCalculatedCarbon).sum();
        double fTotal = records.stream().filter(r -> "fuel".equals(r.getSourceType())).mapToDouble(EmissionRecord::getCalculatedCarbon).sum();
        double wTotal = records.stream().filter(r -> "waste".equals(r.getSourceType())).mapToDouble(EmissionRecord::getCalculatedCarbon).sum();
        
        model.addAttribute("eTotal", eTotal);
        model.addAttribute("fTotal", fTotal);
        model.addAttribute("wTotal", wTotal);
        
        // Estado de los servicios para la UI
        model.addAttribute("aiStatus", aiAgent != null);
        model.addAttribute("aiDemo", AiServiceManager.isMockMode());
        model.addAttribute("dbStatus", true); 
        
        return "index";
    }

    @PostMapping("/calculate")
    public String calculate(@RequestParam String type, @RequestParam double amount) {
        try {
            // Reutilizando lógica POO estructurada previamente
            EmissionSource source = EmissionSourceFactory.createEmissionSource(type, amount);
            double calculatedCarbon = source.calculateCarbonFootprint();
            
            // Persistencia SQL
            EmissionRecord record = new EmissionRecord(type, amount, calculatedCarbon, LocalDate.now());
            recordDAO.save(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
    
    @PostMapping("/api/chat")
    @ResponseBody
    public String chat(@RequestParam String message) {
        if (aiAgent == null) {
            return "Lo siento, el consultor IA no está disponible en este momento.";
        }
        try {
            return aiAgent.answer(message);
        } catch (RuntimeException e) {
            // Manejar errores de API Key, conexión o CUOTA de forma amigable
            String errMsg = (e.getMessage() != null) ? e.getMessage().toLowerCase() : "";
            if (errMsg.contains("api key") || errMsg.contains("401") || errMsg.contains("quota") || errMsg.contains("limit")) {
                return "🌿 [MODO DEMO FORZADO] Parece que hay un problema con la cuota o clave de OpenAI. Pero aquí tienes un consejo: Para reducir tu huella, intenta optimizar el uso de energía en horas pico.";
            }
            return "Lo siento, ocurrió un error al procesar tu pregunta: " + e.getMessage();
        }
    }
}

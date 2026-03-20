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
        
        // Estado de los servicios para la UI
        model.addAttribute("aiStatus", aiAgent != null);
        model.addAttribute("dbStatus", true); // Si findAll() no falló, la DB está OK
        
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
            return "El Agente IA no está inicializado. Verifica tu API Key.";
        }
        try {
            return aiAgent.chat(message);
        } catch (Exception e) {
            return "Error de IA: " + e.getMessage();
        }
    }
}

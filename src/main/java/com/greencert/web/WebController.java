package com.greencert.web;

import com.greencert.ai.AiServiceManager;
import com.greencert.ai.IsoConsultantAgent;
import com.greencert.core.factory.EmissionSourceFactory;
import com.greencert.core.model.EmissionSource;
import com.greencert.db.dao.EmissionRecordDAO;
import com.greencert.db.model.EmissionRecord;
import com.greencert.db.model.User;
import jakarta.servlet.http.HttpSession;
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
    public String index(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<EmissionRecord> records = recordDAO.findByUserId(user.getId());
        model.addAttribute("records", records);
        model.addAttribute("user", user);
        
        // --- Statistics for Chart.js ---
        double eTotal = records.stream().filter(r -> "electricity".equals(r.getSourceType())).mapToDouble(EmissionRecord::getCalculatedCarbon).sum();
        double fTotal = records.stream().filter(r -> "fuel".equals(r.getSourceType())).mapToDouble(EmissionRecord::getCalculatedCarbon).sum();
        double wTotal = records.stream().filter(r -> "waste".equals(r.getSourceType())).mapToDouble(EmissionRecord::getCalculatedCarbon).sum();
        
        model.addAttribute("eTotal", eTotal);
        model.addAttribute("fTotal", fTotal);
        model.addAttribute("wTotal", wTotal);
        
        // Service status for UI
        model.addAttribute("aiStatus", aiAgent != null);
        model.addAttribute("aiDemo", AiServiceManager.isMockMode());
        model.addAttribute("dbStatus", true); 
        
        return "index";
    }

    @PostMapping("/calculate")
    public String calculate(HttpSession session, @RequestParam String type, @RequestParam double amount) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            // Reusing previously structured OOP logic
            EmissionSource source = EmissionSourceFactory.createEmissionSource(type, amount);
            double calculatedCarbon = source.calculateCarbonFootprint();
            
            // SQL Persistence
            EmissionRecord record = new EmissionRecord(user.getId(), type, amount, calculatedCarbon, LocalDate.now());
            recordDAO.save(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        try {
            recordDAO.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
    
    @PostMapping("/api/chat")
    @ResponseBody
    public String chat(@RequestParam String message) {
        if (aiAgent == null) {
            return "I am sorry, the AI consultant is not available at this moment.";
        }
        try {
            return aiAgent.chat(message);
        } catch (RuntimeException e) {
            // Handle API Key, connection, or QUOTA errors gracefully without revealing technical details
            String errMsg = (e.getMessage() != null) ? e.getMessage().toLowerCase() : "";
            if (errMsg.contains("api key") || errMsg.contains("401") || errMsg.contains("quota") || errMsg.contains("limit")) {
                return AiServiceManager.getMockResponse(message);
            }
            return "I am sorry, the consultant is currently analyzing other data. Please try again later.";
        }
    }
}

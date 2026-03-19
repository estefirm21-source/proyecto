package com.greencert.core.service;

import com.greencert.core.model.EmissionSource;
import java.util.List;

public class CarbonCalculatorService {

    /**
     * Calcula la suma total de emisiones de un listado de fuentes.
     * @param sources Lista de fuentes de emisión.
     * @return Total en kgCO2e.
     */
    public double calculateTotalFootprint(List<EmissionSource> sources) {
        if (sources == null || sources.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        for (EmissionSource source : sources) {
            total += source.calculateCarbonFootprint();
        }
        return total;
    }
    
    /**
     * Genera un reporte formateado para consola de las fuentes de emisión.
     * @param sources Lista de fuentes de emisión.
     * @return Cadena de texto con reporte.
     */
    public String generateReport(List<EmissionSource> sources) {
        if (sources == null || sources.isEmpty()) {
            return "No hay fuentes de emisión registradas.";
        }
        
        StringBuilder report = new StringBuilder("=== Reporte de Huella de Carbono ===\n");
        double total = calculateTotalFootprint(sources);
        
        for (EmissionSource source : sources) {
            report.append("- ").append(source.getReportDetails()).append("\n");
        }
        
        report.append("------------------------------------\n");
        report.append(String.format("Total Huella de Carbono: %.2f kgCO2e\n", total));
        
        return report.toString();
    }
}

package com.greencert.core.model;

public class Waste extends EmissionSource {
    private final double kg;

    public Waste(double kg) {
        super(1.8); // Factor de emisión ejemplo
        this.kg = kg;
    }
    
    public double getKg() {
        return kg;
    }

    @Override
    public double calculateCarbonFootprint() {
        return kg * getEmissionFactor();
    }

    @Override
    public String getReportDetails() {
        return "Residuos: " + kg + " kg | Factor: " + getEmissionFactor() + " | Huella: " + calculateCarbonFootprint() + " kgCO2e";
    }
}

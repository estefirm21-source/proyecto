package com.greencert.core.model;

public class Waste extends EmissionSource {
    private final double kg;

    public Waste(double kg) {
        super(1.8); // Example emission factor
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
        return "Waste: " + kg + " kg | Factor: " + getEmissionFactor() + " | Footprint: " + calculateCarbonFootprint() + " kgCO2e";
    }
}

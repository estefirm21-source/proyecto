package com.greencert.core.model;

public class Fuel extends EmissionSource {
    private final double liters;

    public Fuel(double liters) {
        super(2.3); // Factor de emisión ejemplo
        this.liters = liters;
    }

    public double getLiters() {
        return liters;
    }

    @Override
    public double calculateCarbonFootprint() {
        return liters * getEmissionFactor();
    }

    @Override
    public String getReportDetails() {
        return "Combustible: " + liters + " Litros | Factor: " + getEmissionFactor() + " | Huella: " + calculateCarbonFootprint() + " kgCO2e";
    }
}

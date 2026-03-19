package com.greencert.core.model;

public class Electricity extends EmissionSource {
    private final double kwh;

    public Electricity(double kwh) {
        super(0.5); // Factor de emisión ejemplo
        this.kwh = kwh;
    }

    public double getKwh() {
        return kwh;
    }

    @Override
    public double calculateCarbonFootprint() {
        return kwh * getEmissionFactor();
    }

    @Override
    public String getReportDetails() {
        return "Electricidad: " + kwh + " kWh | Factor: " + getEmissionFactor() + " | Huella: " + calculateCarbonFootprint() + " kgCO2e";
    }
}

package com.greencert.core.model;

public abstract class EmissionSource implements ICalculator, IReportable {

    // Encapsulamiento correcto
    protected final double emissionFactor;

    public EmissionSource(double emissionFactor) {
        this.emissionFactor = emissionFactor;
    }

    // Getter protegido para que solo las clases hijas lo usen
    protected double getEmissionFactor() {
        return emissionFactor;
    }

    // Método abstracto (polimorfismo) proveniente de ICalculator
    @Override
    public abstract double calculateCarbonFootprint();

    // Método abstracto proveniente de IReportable
    @Override
    public abstract String getReportDetails();
}

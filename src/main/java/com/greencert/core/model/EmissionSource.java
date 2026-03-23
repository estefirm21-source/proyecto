package com.greencert.core.model;

public abstract class EmissionSource implements ICalculator, IReportable {

    // Correct Encapsulation
    protected final double emissionFactor;

    public EmissionSource(double emissionFactor) {
        this.emissionFactor = emissionFactor;
    }

    // Protected getter so only child classes use it
    protected double getEmissionFactor() {
        return emissionFactor;
    }

    // Abstract method (polymorphism) from ICalculator
    @Override
    public abstract double calculateCarbonFootprint();

    // Abstract method from IReportable
    @Override
    public abstract String getReportDetails();
}

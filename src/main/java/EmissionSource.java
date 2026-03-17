public abstract class EmissionSource implements CarbonCalculable {

    // Correct encapsulation: immutable attribute
    private final double emissionFactor;

    public EmissionSource(double emissionFactor) {
        if (emissionFactor < 0) {
            throw new IllegalArgumentException("The emission factor cannot be negative.");
        }
        this.emissionFactor = emissionFactor;
    }

    // Protected getter: Only child classes can access the vital data for the
    // formula
    protected double getEmissionFactor() {
        return emissionFactor;
    }

    @Override
    public abstract double calculateCarbonFootprint();

}
public class Fuel extends EmissionSource {
    private final double liters;

    public Fuel(double liters, double factor) {
        super(factor);
        if (liters < 0) {
            throw new IllegalArgumentException("Fuel consumption (liters) cannot be negative.");
        }
        this.liters = liters;
    }

    public double getLiters() {
        return liters;
    }

    @Override
    public double calculateCarbonFootprint() {
        return liters * getEmissionFactor();
    }
}

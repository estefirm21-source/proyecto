public class Waste extends EmissionSource {
    private final double kg;

    public Waste(double kg, double factor) {
        super(factor);
        if (kg < 0) {
            throw new IllegalArgumentException("La cantidad de residuos (kg) no puede ser negativa.");
        }
        this.kg = kg;
    }

    public double getKg() {
        return kg;
    }

    @Override
    public double calculateCarbonFootprint() {
        return kg * getEmissionFactor();
    }
}

public class Electricity extends EmissionSource {
    private final double kwh;

    public Electricity(double kwh, double factor) {
        super(factor);
        if (kwh < 0) {
            throw new IllegalArgumentException("El consumo eléctrico (kWh) no puede ser negativo.");
        }
        this.kwh = kwh;
    }

    public double getKwh() {
        return kwh;
    }

    @Override
    public double calculateCarbonFootprint() {
        return kwh * getEmissionFactor();
    }
}

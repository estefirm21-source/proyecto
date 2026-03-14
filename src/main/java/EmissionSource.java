public abstract class EmissionSource implements CarbonCalculable {

    // Encapsulamiento correcto: atributo inmutable
    private final double emissionFactor;

    public EmissionSource(double emissionFactor) {
        if (emissionFactor < 0) {
            throw new IllegalArgumentException("El factor de emisión no puede ser negativo.");
        }
        this.emissionFactor = emissionFactor;
    }

    // Getter protegido: Solo las clases hijas pueden acceder al dato vital para la
    // fórmula
    protected double getEmissionFactor() {
        return emissionFactor;
    }

    @Override
    public abstract double calculateCarbonFootprint();

}
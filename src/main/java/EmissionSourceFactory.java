import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Factoría para la creación de fuentes de emisión.
 * Sigue el principio Open/Closed (OCP).
 */
public class EmissionSourceFactory {
    private static final Map<String, BiFunction<Double, Double, EmissionSource>> REGISTRY = new HashMap<>();

    static {
        // Registro inicial de tipos conocidos
        REGISTRY.put("electricity", Electricity::new);
        REGISTRY.put("fuel", Fuel::new);
        REGISTRY.put("waste", Waste::new);
    }

    /**
     * Registra un nuevo tipo de fuente sin modificar esta clase.
     */
    public static void registerSource(String type, BiFunction<Double, Double, EmissionSource> constructor) {
        REGISTRY.put(type.toLowerCase(), constructor);
    }

    public static EmissionSource createSource(String type, double value, double factor) {
        BiFunction<Double, Double, EmissionSource> constructor = REGISTRY.get(type.toLowerCase());
        if (constructor == null) {
            throw new IllegalArgumentException("Tipo de fuente desconocido: " + type);
        }
        return constructor.apply(value, factor);
    }
}

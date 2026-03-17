import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Factory for creating emission sources.
 * Follows the Open/Closed Principle (OCP).
 */
public class EmissionSourceFactory {
    private static final Map<String, BiFunction<Double, Double, EmissionSource>> REGISTRY = new HashMap<>();

    static {
        // Initial registration of known types
        REGISTRY.put("electricity", Electricity::new);
        REGISTRY.put("fuel", Fuel::new);
        REGISTRY.put("waste", Waste::new);
    }

    /**
     * Registers a new source type without modifying this class.
     */
    public static void registerSource(String type, BiFunction<Double, Double, EmissionSource> constructor) {
        REGISTRY.put(type.toLowerCase(), constructor);
    }

    public static EmissionSource createSource(String type, double value, double factor) {
        BiFunction<Double, Double, EmissionSource> constructor = REGISTRY.get(type.toLowerCase());
        if (constructor == null) {
            throw new IllegalArgumentException("Unknown source type: " + type);
        }
        return constructor.apply(value, factor);
    }
}

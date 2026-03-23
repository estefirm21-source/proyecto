package com.greencert.core.factory;

import com.greencert.core.model.Electricity;
import com.greencert.core.model.EmissionSource;
import com.greencert.core.model.Fuel;
import com.greencert.core.model.Waste;

public class EmissionSourceFactory {

    /**
     * Factory method for creating emission sources.
     * @param type "electricity", "fuel", or "waste"
     * @param value Consumed amount (kWh, L, Kg)
     * @return EmissionSource instance
     */
    public static EmissionSource createEmissionSource(String type, double value) {
        if (type == null) {
            throw new IllegalArgumentException("The emission source type cannot be null");
        }
        
        switch (type.toLowerCase()) {
            case "electricity":
                return new Electricity(value);
            case "fuel":
                return new Fuel(value);
            case "waste":
                return new Waste(value);
            default:
                throw new IllegalArgumentException("Unsupported emission source type: " + type);
        }
    }
}

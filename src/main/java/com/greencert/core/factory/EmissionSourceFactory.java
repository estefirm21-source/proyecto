package com.greencert.core.factory;

import com.greencert.core.model.Electricity;
import com.greencert.core.model.EmissionSource;
import com.greencert.core.model.Fuel;
import com.greencert.core.model.Waste;

public class EmissionSourceFactory {

    /**
     * Factory method para crear fuentes de emisión.
     * @param type "electricity", "fuel", o "waste"
     * @param value Cantidad consumida (kWh, L, Kg)
     * @return Instancia de EmissionSource
     */
    public static EmissionSource createEmissionSource(String type, double value) {
        if (type == null) {
            throw new IllegalArgumentException("El tipo de fuente de emisión no puede ser nulo");
        }
        
        switch (type.toLowerCase()) {
            case "electricity":
                return new Electricity(value);
            case "fuel":
                return new Fuel(value);
            case "waste":
                return new Waste(value);
            default:
                throw new IllegalArgumentException("Tipo de fuente de emisión no soportado: " + type);
        }
    }
}


/**
 * Interface para el repositorio de consumos.
 * Sigue el principio de Inversión de Dependencias (DIP).
 */
public interface ConsumoRepository {
    void insertarConsumo(int idEmpresa, int idSource, int mes, int anio, double cantidad, double co2Generado);

    void mostrarHistorial();

    void actualizarConsumo(int idConsumo, double nuevaCantidad, double nuevoCo2);

    void eliminarConsumo(int idConsumo);

    double obtenerTotalCo2PorAnio(int idEmpresa, int anio);
}

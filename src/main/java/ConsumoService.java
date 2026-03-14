/**
 * Interfaz para la capa de servicios de consumo.
 * Encapsula la lógica de negocio y coordina el repositorio.
 */
public interface ConsumoService {
    void registrarConsumo(int idEmpresa, int idSource, int mes, int anio, double cantidad, double factor);

    void listarHistorial();

    double calcularHuellaAnual(int idEmpresa, int anio);
}

/**
 * Implementación de la capa de servicio.
 * Aquí se puede agregar validaciones extra o lógica de negocio compleja.
 */
public class ConsumoServiceImpl implements ConsumoService {

    private final ConsumoRepository repository;

    public ConsumoServiceImpl(ConsumoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void registrarConsumo(int idEmpresa, int idSource, int mes, int anio, double cantidad, double factor) {
        // Lógica de negocio: El cálculo de CO2 ocurre aquí o se delega a los POO
        // Usamos el Factory para obtener la fuente y calcular el CO2
        String tipo;
        switch (idSource) {
            case 1:
                tipo = "electricity";
                break;
            case 2:
                tipo = "fuel";
                break;
            case 3:
                tipo = "waste";
                break;
            default:
                tipo = "electricity";
        }

        EmissionSource source = EmissionSourceFactory.createSource(tipo, cantidad, factor);
        double co2 = source.calculateCarbonFootprint();

        // Persistencia a través del repositorio
        repository.insertarConsumo(idEmpresa, idSource, mes, anio, cantidad, co2);
    }

    @Override
    public void listarHistorial() {
        repository.mostrarHistorial();
    }

    @Override
    public double calcularHuellaAnual(int idEmpresa, int anio) {
        return repository.obtenerTotalCo2PorAnio(idEmpresa, anio);
    }
}

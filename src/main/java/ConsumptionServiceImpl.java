import com.greencert.core.model.EmissionSource;
import com.greencert.core.factory.EmissionSourceFactory;

/**
 * Implementation of the service layer.
 * Extra validations or complex business logic can be added here.
 */
public class ConsumptionServiceImpl implements ConsumptionService {

    private final ConsumptionRepository repository;

    public ConsumptionServiceImpl(ConsumptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void registerConsumption(int companyId, int sourceId, int month, int year, double amount, double factor) {
        // Business logic: CO2 calculation occurs here or is delegated to OOP classes
        // We use the Factory to obtain the source and calculate CO2
        String type = switch (sourceId) {
            case 1 -> "electricity";
            case 2 -> "fuel";
            case 3 -> "waste";
            default -> "electricity";
        };

        EmissionSource source = EmissionSourceFactory.createEmissionSource(type, amount);
        double co2 = source.calculateCarbonFootprint();

        // Persistence through the repository
        repository.insertConsumption(companyId, sourceId, month, year, amount, co2);
    }

    @Override
    public void listHistory() {
        repository.showHistory();
    }

    @Override
    public double calculateAnnualFootprint(int companyId, int year) {
        return repository.getTotalCo2ByYear(companyId, year);
    }
}

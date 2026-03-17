/**
 * Interface for the consumption service layer.
 * Encapsulates business logic and coordinates the repository.
 */
public interface ConsumptionService {
    void registerConsumption(int companyId, int sourceId, int month, int year, double amount, double factor);

    void listHistory();

    double calculateAnnualFootprint(int companyId, int year);
}

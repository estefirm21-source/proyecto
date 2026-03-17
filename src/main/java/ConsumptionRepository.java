
/**
 * Interface for the consumption repository.
 * Follows the Dependency Inversion Principle (DIP).
 */
public interface ConsumptionRepository {
    void insertConsumption(int companyId, int sourceId, int month, int year, double amount, double generatedCo2);

    void showHistory();

    void updateConsumption(int consumptionId, double newAmount, double newCo2);

    void deleteConsumption(int consumptionId);

    double getTotalCo2ByYear(int companyId, int year);
}

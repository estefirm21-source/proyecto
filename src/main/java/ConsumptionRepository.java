public interface ConsumptionRepository {
    void insertConsumption(int companyId, int sourceId, int month, int year, double amount, double co2);
    void showHistory();
    double getTotalCo2ByYear(int companyId, int year);
}

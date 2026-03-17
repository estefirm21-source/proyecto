import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC implementation of the consumption repository.
 */
public class ConsumptionDAO implements ConsumptionRepository {

    @Override
    public void insertConsumption(int companyId, int sourceId, int month, int year, double amount,
            double generatedCo2) {
        String sql = "INSERT INTO monthly_consumption (company_id, source_id, month, year, amount, generated_co2) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE amount = VALUES(amount), generated_co2 = VALUES(generated_co2)";
        try (Connection conn = DBConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            stmt.setInt(2, sourceId);
            stmt.setInt(3, month);
            stmt.setInt(4, year);
            stmt.setDouble(5, amount);
            stmt.setDouble(6, generatedCo2);

            stmt.executeUpdate();
            System.out.println("✅ Consumption processed successfully (Inserted/Updated).");

        } catch (SQLException e) {
            System.err.println("❌ Critical persistence error: " + e.getMessage());
        }
    }

    @Override
    public void showHistory() {
        String sql = "SELECT * FROM monthly_consumption";
        try (Connection conn = DBConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- CONSUMPTION AND FOOTPRINT HISTORY ---");
            while (rs.next()) {
                System.out.printf(
                        "ID: %d | Company: %d | Source: %d | Date: %02d/%4d | Usage: %.2f | Generated CO2: %.2f kg%n",
                        rs.getInt("consumption_id"),
                        rs.getInt("company_id"),
                        rs.getInt("source_id"),
                        rs.getInt("month"),
                        rs.getInt("year"),
                        rs.getDouble("amount"),
                        rs.getDouble("generated_co2"));
            }
            System.out.println("-----------------------------------------\n");

        } catch (SQLException e) {
            System.err.println("❌ Error querying data: " + e.getMessage());
        }
    }

    @Override
    public void updateConsumption(int consumptionId, double newAmount, double newCo2) {
        String sql = "UPDATE monthly_consumption SET amount = ?, generated_co2 = ? WHERE consumption_id = ?";
        try (Connection conn = DBConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newAmount);
            stmt.setDouble(2, newCo2);
            stmt.setInt(3, consumptionId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Consumption updated successfully.");
            } else {
                System.out.println("⚠️ Could not find consumption with ID " + consumptionId);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error updating consumption: " + e.getMessage());
        }
    }

    @Override
    public void deleteConsumption(int consumptionId) {
        String sql = "DELETE FROM monthly_consumption WHERE consumption_id = ?";
        try (Connection conn = DBConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, consumptionId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Consumption deleted successfully.");
            } else {
                System.out.println("⚠️ Could not find consumption with ID " + consumptionId);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error deleting consumption: " + e.getMessage());
        }
    }

    @Override
    public double getTotalCo2ByYear(int companyId, int year) {
        String sql = "SELECT SUM(generated_co2) as total FROM monthly_consumption WHERE company_id = ? AND year = ?";
        try (Connection conn = DBConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            stmt.setInt(2, year);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error calculating report: " + e.getMessage());
        }
        return 0.0;
    }
}

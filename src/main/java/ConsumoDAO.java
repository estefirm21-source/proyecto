import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Implementación JDBC del repositorio de consumos.
 */
public class ConsumoDAO implements ConsumoRepository {

    @Override
    public void insertarConsumo(int idEmpresa, int idSource, int mes, int anio, double cantidad, double co2Generado) {
        String sql = "INSERT INTO consumo_mensual (id_empresa, id_source, mes, anio, cantidad, co2_generado) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE cantidad = VALUES(cantidad), co2_generado = VALUES(co2_generado)";
        try (Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEmpresa);
            stmt.setInt(2, idSource);
            stmt.setInt(3, mes);
            stmt.setInt(4, anio);
            stmt.setDouble(5, cantidad);
            stmt.setDouble(6, co2Generado);

            stmt.executeUpdate();
            System.out.println("✅ Consumo procesado exitosamente (Insertado/Actualizado).");

        } catch (SQLException e) {
            System.err.println("❌ Error crítico en persistencia: " + e.getMessage());
        }
    }

    @Override
    public void mostrarHistorial() {
        String sql = "SELECT * FROM consumo_mensual";
        try (Connection conn = ConexionDB.conectar();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- HISTORIAL DE CONSUMOS Y HUELLA ---");
            while (rs.next()) {
                System.out.printf(
                        "ID: %d | Empresa: %d | Fuente: %d | Fecha: %02d/%4d | Uso: %.2f | CO2 Generado: %.2f kg%n",
                        rs.getInt("id_consumo"),
                        rs.getInt("id_empresa"),
                        rs.getInt("id_source"),
                        rs.getInt("mes"),
                        rs.getInt("anio"),
                        rs.getDouble("cantidad"),
                        rs.getDouble("co2_generado"));
            }
            System.out.println("--------------------------------------\n");

        } catch (SQLException e) {
            System.err.println("❌ Error al consultar datos: " + e.getMessage());
        }
    }

    @Override
    public void actualizarConsumo(int idConsumo, double nuevaCantidad, double nuevoCo2) {
        String sql = "UPDATE consumo_mensual SET cantidad = ?, co2_generado = ? WHERE id_consumo = ?";
        try (Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, nuevaCantidad);
            stmt.setDouble(2, nuevoCo2);
            stmt.setInt(3, idConsumo);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("✅ Consumo actualizado exitosamente.");
            } else {
                System.out.println("⚠️ No se encontró el consumo con ID " + idConsumo);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar el consumo: " + e.getMessage());
        }
    }

    @Override
    public void eliminarConsumo(int idConsumo) {
        String sql = "DELETE FROM consumo_mensual WHERE id_consumo = ?";
        try (Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idConsumo);
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("✅ Consumo eliminado exitosamente.");
            } else {
                System.out.println("⚠️ No se encontró el consumo con ID " + idConsumo);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar el consumo: " + e.getMessage());
        }
    }

    @Override
    public double obtenerTotalCo2PorAnio(int idEmpresa, int anio) {
        String sql = "SELECT SUM(co2_generado) as total FROM consumo_mensual WHERE id_empresa = ? AND anio = ?";
        try (Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEmpresa);
            stmt.setInt(2, anio);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al calcular reporte: " + e.getMessage());
        }
        return 0.0;
    }
}

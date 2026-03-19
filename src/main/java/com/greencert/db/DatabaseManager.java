package com.greencert.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static HikariDataSource dataSource;

    static {
        try {
            // Configuración del Pool de Conexiones
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:sqlite:greencert.db"); // Base de datos embebida (local)
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(1800000);
            config.setConnectionTimeout(30000);
            
            dataSource = new HikariDataSource(config);
            
            // Inicializar esquema si no existe
            initializeDatabase();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error inicializando el Pool de Conexiones de Base de Datos", e);
        }
    }

    private static void initializeDatabase() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS emission_records (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "source_type VARCHAR(50) NOT NULL, " +
                "amount DOUBLE NOT NULL, " +
                "calculated_carbon DOUBLE NOT NULL, " +
                "record_date DATE NOT NULL" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
            System.out.println("Base de datos SQLite y tabla 'emission_records' verificadas/creadas con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al crear esquema de la base de datos.");
            e.printStackTrace();
        }
    }

    /**
     * Obtiene una conexión del Pool.
     * @return Connection lista para transacciones.
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

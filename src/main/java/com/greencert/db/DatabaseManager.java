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
            // Connection Pool Configuration
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:sqlite:greencert.db"); // Embedded database (local)
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(1800000);
            config.setConnectionTimeout(30000);
            
            dataSource = new HikariDataSource(config);
            
            // Initialize schema if it doesn't exist
            initializeDatabase();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing Database Connection Pool", e);
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
            System.out.println("SQLite database and 'emission_records' table verified/created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating database schema.");
            e.printStackTrace();
        }
    }

    /**
     * Gets a connection from the Pool.
     * @return Connection ready for transactions.
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

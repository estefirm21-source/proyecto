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
        String createUsersTableSql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "password VARCHAR(100) NOT NULL, " +
                "company_name VARCHAR(100) NOT NULL" +
                ");";

        String createRecordsTableSql = "CREATE TABLE IF NOT EXISTS emission_records (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "source_type VARCHAR(50) NOT NULL, " +
                "amount DOUBLE NOT NULL, " +
                "calculated_carbon DOUBLE NOT NULL, " +
                "record_date DATE NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(id)" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTableSql);
            stmt.execute(createRecordsTableSql);
            
            // Try adding user_id if it's an old database (migration)
            try {
                stmt.execute("ALTER TABLE emission_records ADD COLUMN user_id INTEGER REFERENCES users(id);");
            } catch (SQLException e) {
                // Column probably already exists
            }
            
            System.out.println("Database tables verified/created successfully.");
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

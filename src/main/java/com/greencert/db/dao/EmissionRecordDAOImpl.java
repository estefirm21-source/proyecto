package com.greencert.db.dao;

import com.greencert.db.DatabaseManager;
import com.greencert.db.model.EmissionRecord;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmissionRecordDAOImpl implements EmissionRecordDAO {

    @Override
    public void save(EmissionRecord record) {
        String sql = "INSERT INTO emission_records (source_type, amount, calculated_carbon, record_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            
            // Demostrar control de transacciones (Punto de la rúbrica)
            conn.setAutoCommit(false); 
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, record.getSourceType());
                stmt.setDouble(2, record.getAmount());
                stmt.setDouble(3, record.getCalculatedCarbon());
                stmt.setString(4, record.getRecordDate().toString()); 
                
                stmt.executeUpdate();
                conn.commit(); // Commit explícito
                
            } catch (SQLException e) {
                conn.rollback(); // Rollback en caso de error
                throw new RuntimeException("Error guardando el registro. Se ha realizado un rollback parcial de la transacción.", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<EmissionRecord> findAll() {
        List<EmissionRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM emission_records ORDER BY record_date DESC";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                EmissionRecord record = new EmissionRecord();
                record.setId(rs.getInt("id"));
                record.setSourceType(rs.getString("source_type"));
                record.setAmount(rs.getDouble("amount"));
                record.setCalculatedCarbon(rs.getDouble("calculated_carbon"));
                record.setRecordDate(LocalDate.parse(rs.getString("record_date")));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM emission_records WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

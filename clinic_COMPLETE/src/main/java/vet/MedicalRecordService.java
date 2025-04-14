package main.java.vet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import main.java.vet.util.DatabaseConnection;

public class MedicalRecordService {
    
    public void addMedicalRecord(MedicalRecord record) throws SQLException {
        String query = "INSERT INTO medical_records (pet_id, record_date, weight, temperature, " +
                      "symptoms, diagnosis, treatment, prescriptions, notes) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, record.getPetId());
            stmt.setDate(2, record.getRecordDate());
            stmt.setDouble(3, record.getWeight());
            stmt.setString(4, record.getTemperature());
            stmt.setString(5, record.getSymptoms());
            stmt.setString(6, record.getDiagnosis());
            stmt.setString(7, record.getTreatment());
            stmt.setString(8, record.getPrescriptions());
            stmt.setString(9, record.getNotes());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    record.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<MedicalRecord> getPetMedicalHistory(int petId) throws SQLException {
        String query = "SELECT * FROM medical_records WHERE pet_id = ? ORDER BY record_date DESC";
        List<MedicalRecord> records = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, petId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                records.add(mapResultSetToMedicalRecord(rs));
            }
        }
        
        return records;
    }

    private MedicalRecord mapResultSetToMedicalRecord(ResultSet rs) throws SQLException {
        MedicalRecord record = new MedicalRecord();
        record.setId(rs.getInt("id"));
        record.setPetId(rs.getInt("pet_id"));
        record.setRecordDate(rs.getDate("record_date"));
        record.setWeight(rs.getDouble("weight"));
        record.setTemperature(rs.getString("temperature"));
        record.setSymptoms(rs.getString("symptoms"));
        record.setDiagnosis(rs.getString("diagnosis"));
        record.setTreatment(rs.getString("treatment"));
        record.setPrescriptions(rs.getString("prescriptions"));
        record.setNotes(rs.getString("notes"));
        return record;
    }

    public List<MedicalRecord> getRecentRecords(int limit) throws SQLException {
        String query = "SELECT * FROM medical_records ORDER BY record_date DESC LIMIT ?";
        List<MedicalRecord> records = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                records.add(mapResultSetToMedicalRecord(rs));
            }
        }
        
        return records;
    }

    public void updateMedicalRecord(MedicalRecord record) throws SQLException {
        String query = "UPDATE medical_records SET weight = ?, temperature = ?, symptoms = ?, " +
                      "diagnosis = ?, treatment = ?, prescriptions = ?, notes = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setDouble(1, record.getWeight());
            stmt.setString(2, record.getTemperature());
            stmt.setString(3, record.getSymptoms());
            stmt.setString(4, record.getDiagnosis());
            stmt.setString(5, record.getTreatment());
            stmt.setString(6, record.getPrescriptions());
            stmt.setString(7, record.getNotes());
            stmt.setInt(8, record.getId());
            
            stmt.executeUpdate();
        }
    }
}
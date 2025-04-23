package main.java.vet.service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.java.vet.model.Resource;
import main.java.vet.model.Appointment;
import main.java.vet.util.DatabaseConnection;

public class ResourceService {
    private static final Logger logger = Logger.getLogger(ResourceService.class.getName());
    private final AuditService auditService;

    public ResourceService(AuditService auditService) {
        this.auditService = auditService;
    }

    public boolean checkResourceAvailability(Appointment appointment) throws SQLException {
        String query = "SELECT r.* FROM resources r " +
                      "LEFT JOIN resource_reservations rr ON r.resource_id = rr.resource_id " +
                      "AND rr.start_time < ? AND rr.end_time > ? " +
                      "WHERE r.type = ? AND rr.reservation_id IS NULL";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setTimestamp(1, appointment.getEndTime());
            stmt.setTimestamp(2, appointment.getStartTime());
            stmt.setString(3, getRequiredResourceType(appointment));
            
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If there's at least one available resource
        } catch (SQLException e) {
            logger.severe("Failed to check resource availability: " + e.getMessage());
            throw e;
        }
    }

    public void reserveResources(Appointment appointment) throws SQLException {
        String query = "INSERT INTO resource_reservations (resource_id, appointment_id, start_time, end_time) " +
                      "SELECT r.resource_id, ?, ?, ? FROM resources r " +
                      "WHERE r.type = ? AND r.resource_id NOT IN " +
                      "(SELECT resource_id FROM resource_reservations " +
                      "WHERE start_time < ? AND end_time > ?) LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            String resourceType = getRequiredResourceType(appointment);
            stmt.setInt(1, appointment.getAppointmentId());
            stmt.setTimestamp(2, appointment.getStartTime());
            stmt.setTimestamp(3, appointment.getEndTime());
            stmt.setString(4, resourceType);
            stmt.setTimestamp(5, appointment.getEndTime());
            stmt.setTimestamp(6, appointment.getStartTime());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to reserve resource");
            }
            
            auditService.logAction("resource_reservations", appointment.getAppointmentId(), "INSERT", null, "Resource reserved");
            logger.info("Resource reserved for appointment ID: " + appointment.getAppointmentId());
        } catch (SQLException e) {
            logger.severe("Failed to reserve resources: " + e.getMessage());
            throw e;
        }
    }

    public void updateResourceReservation(Appointment appointment) throws SQLException {
        String query = "UPDATE resource_reservations SET start_time = ?, end_time = ? " +
                      "WHERE appointment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setTimestamp(1, appointment.getStartTime());
            stmt.setTimestamp(2, appointment.getEndTime());
            stmt.setInt(3, appointment.getAppointmentId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update resource reservation");
            }
            
            auditService.logAction("resource_reservations", appointment.getAppointmentId(), "UPDATE", null, "Resource reservation updated");
            logger.info("Resource reservation updated for appointment ID: " + appointment.getAppointmentId());
        } catch (SQLException e) {
            logger.severe("Failed to update resource reservation: " + e.getMessage());
            throw e;
        }
    }

    public void releaseResources(Appointment appointment) throws SQLException {
        String query = "DELETE FROM resource_reservations WHERE appointment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, appointment.getAppointmentId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to release resources");
            }
            
            auditService.logAction("resource_reservations", appointment.getAppointmentId(), "DELETE", null, "Resources released");
            logger.info("Resources released for appointment ID: " + appointment.getAppointmentId());
        } catch (SQLException e) {
            logger.severe("Failed to release resources: " + e.getMessage());
            throw e;
        }
    }

    private String getRequiredResourceType(Appointment appointment) {
        switch (appointment.getServiceType()) {
            case CONSULTA:
                return "CONSULTATION_ROOM";
            case BANHO:
            case TOSA:
                return "GROOMING_STATION";
            case VACINA:
                return "VACCINATION_ROOM";
            default:
                throw new IllegalArgumentException("Invalid service type");
        }
    }
}
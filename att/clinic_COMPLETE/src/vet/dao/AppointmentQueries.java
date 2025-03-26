package vet.dao;

public class AppointmentQueries {
    public static final String INSERT_APPOINTMENT = 
        "INSERT INTO appointments (client_id, pet_id, service_type, start_time, end_time, status, price, notes) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
    public static final String UPDATE_APPOINTMENT = 
        "UPDATE appointments SET service_type = ?, start_time = ?, end_time = ?, " +
        "status = ?, price = ?, notes = ? WHERE appointment_id = ?";
        
    public static final String CANCEL_APPOINTMENT = 
        "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        
    public static final String GET_APPOINTMENT = 
        "SELECT * FROM appointments WHERE appointment_id = ?";
        
    public static final String GET_APPOINTMENTS_BY_CLIENT = 
        "SELECT * FROM appointments WHERE client_id = ? ORDER BY start_time DESC";
        
    public static final String CHECK_OVERLAPPING_APPOINTMENTS = 
        "SELECT COUNT(*) FROM appointments WHERE" +
        " ((start_time BETWEEN ? AND ?) OR (end_time BETWEEN ? AND ?))" +
        " AND status NOT IN ('CANCELLED', 'COMPLETED')";
}
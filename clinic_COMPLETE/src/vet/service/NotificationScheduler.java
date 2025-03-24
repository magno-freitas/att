package vet.service;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

public class NotificationScheduler {
    private final ScheduledExecutorService scheduler;
    private final NotificationService notificationService;
    private final ClientService clientService;
    private final PetService petService;

    public NotificationScheduler() {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.notificationService = new NotificationService();
        this.clientService = new ClientService();
        this.petService = new PetService();
    }

    public void start() {
        // Schedule appointment reminders check every hour
        scheduler.scheduleAtFixedRate(
            this::checkUpcomingAppointments,
            0,
            1,
            TimeUnit.HOURS
        );

        // Schedule vaccination due check once per day
        scheduler.scheduleAtFixedRate(
            this::checkVaccinationsDue,
            0,
            24,
            TimeUnit.HOURS
        );
    }

    private void checkUpcomingAppointments() {
        try {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Timestamp tomorrow = new Timestamp(now.getTime() + TimeUnit.DAYS.toMillis(1));

            String query = "SELECT * FROM appointments WHERE start_time BETWEEN ? AND ? AND status = 'agendado'";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                
                stmt.setTimestamp(1, now);
                stmt.setTimestamp(2, tomorrow);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Appointment appointment = mapResultSetToAppointment(rs);
                    Pet pet = petService.getPetById(appointment.getPetId());
                    Client client = clientService.getClientById(pet.getClientId());
                    
                    notificationService.sendAppointmentReminder(appointment, client);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkVaccinationsDue() {
        try {
            Date today = new Date(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            cal.add(Calendar.DAY_OF_MONTH, 7); // Check for vaccines due in next 7 days
            Date nextWeek = new Date(cal.getTimeInMillis());

            String query = "SELECT * FROM vaccine_records WHERE next_dose_date BETWEEN ? AND ?";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                
                stmt.setDate(1, today);
                stmt.setDate(2, nextWeek);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int petId = rs.getInt("pet_id");
                    String vaccineName = rs.getString("vaccine_name");
                    
                    Pet pet = petService.getPetById(petId);
                    Client client = clientService.getClientById(pet.getClientId());
                    
                    notificationService.sendVaccinationDueReminder(pet, client, vaccineName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setPetId(rs.getInt("pet_id"));
        appointment.setService(rs.getString("service"));
        appointment.setServiceType(ServiceType.valueOf(rs.getString("service_type")));
        appointment.setStartTime(rs.getTimestamp("start_time"));
        appointment.setEndTime(rs.getTimestamp("end_time"));
        appointment.setStatus(rs.getString("status"));
        appointment.setNotes(rs.getString("notes"));
        return appointment;
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
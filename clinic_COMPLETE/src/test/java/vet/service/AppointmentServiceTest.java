package test.java.vet.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import main.java.vet.model.Appointment;
import main.java.vet.model.ServiceType;
import main.java.vet.service.AppointmentService;
import main.java.vet.service.AppointmentValidator;
import main.java.vet.service.NotificationService;
import main.java.vet.service.ResourceService;

public class AppointmentServiceTest {
    @Mock
    private AppointmentValidator validator;
    
    @Mock
    private NotificationService notificationService;
    
    @Mock
    private ResourceService resourceService;
    
    private AppointmentService appointmentService;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        appointmentService = new AppointmentService(validator, notificationService, resourceService);
    }
    
    @Test
    public void testScheduleAppointment_Success() throws Exception {
        // Arrange
        Appointment appointment = new Appointment();
        appointment.setClientId(1);
        appointment.setPetId(1);
        appointment.setServiceType(ServiceType.CONSULTA);
        appointment.setStartTime(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
        appointment.setPrice(100.0);
        
        // Act
        appointmentService.scheduleAppointment(appointment);
        
        // Assert
        verify(validator).validateAppointment(appointment);
        verify(validator).setAppointmentEndTime(appointment);
        verify(resourceService).checkResourceAvailability(appointment);
        verify(resourceService).reserveResources(appointment);
        verify(notificationService).sendAppointmentConfirmation(
            eq(appointment.getAppointmentId()),
            anyString(),
            eq(appointment.getStartTime())
        );
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testScheduleAppointment_InvalidAppointment() throws Exception {
        // Arrange
        Appointment appointment = new Appointment();
        doThrow(new IllegalArgumentException("Invalid appointment"))
            .when(validator)
            .validateAppointment(appointment);
        
        // Act
        appointmentService.scheduleAppointment(appointment);
    }
    
    @Test(expected = SQLException.class)
    public void testScheduleAppointment_ResourceUnavailable() throws Exception {
        // Arrange
        Appointment appointment = new Appointment();
        appointment.setServiceType(ServiceType.CONSULTA);
        appointment.setStartTime(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
        
        when(resourceService.checkResourceAvailability(appointment))
            .thenReturn(false);
        
        // Act
        appointmentService.scheduleAppointment(appointment);
    }
    
    @Test
    public void testCancelAppointment_Success() throws Exception {
        // Arrange
        int appointmentId = 1;
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(appointmentId);
        appointment.setStatus("SCHEDULED");
        
        when(appointmentService.getAppointmentById(appointmentId))
            .thenReturn(appointment);
        
        // Act
        appointmentService.cancelAppointment(appointmentId);
        
        // Assert
        assertEquals("CANCELLED", appointment.getStatus());
        verify(notificationService).sendCancellationNotification(eq(appointmentId), anyString());
    }
    
    @Test(expected = SQLException.class)
    public void testCancelAppointment_AlreadyCancelled() throws Exception {
        // Arrange
        int appointmentId = 1;
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(appointmentId);
        appointment.setStatus("CANCELLED");
        
        when(appointmentService.getAppointmentById(appointmentId))
            .thenReturn(appointment);
        
        // Act
        appointmentService.cancelAppointment(appointmentId);
    }
}
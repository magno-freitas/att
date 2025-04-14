package main.java.vet.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CancellationPolicy {
    private static final int MINIMUM_HOURS_BEFORE = 2;
    private static final double LATE_CANCELLATION_FEE_PERCENTAGE = 0.3; // 30% fee

    public static boolean canCancel(LocalDateTime appointmentTime) {
        LocalDateTime now = LocalDateTime.now();
        long hoursUntilAppointment = ChronoUnit.HOURS.between(now, appointmentTime);
        return hoursUntilAppointment >= MINIMUM_HOURS_BEFORE;
    }

    public static double calculateCancellationFee(double appointmentPrice, LocalDateTime appointmentTime) {
        if (canCancel(appointmentTime)) {
            return 0.0;
        }
        return appointmentPrice * LATE_CANCELLATION_FEE_PERCENTAGE;
    }

    public static String getCancellationMessage(LocalDateTime appointmentTime) {
        if (canCancel(appointmentTime)) {
            return "Appointment can be cancelled without fee";
        }
        return String.format("Late cancellation will incur a %d%% fee of the appointment price", 
                           (int)(LATE_CANCELLATION_FEE_PERCENTAGE * 100));
    }
}
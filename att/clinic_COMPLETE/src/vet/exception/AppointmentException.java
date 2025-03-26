package vet.exception;

public class AppointmentException extends ApplicationException {
    public AppointmentException(String message) {
        super(message);
    }

    public AppointmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
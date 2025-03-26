package vet.exception;

public class AppointmentValidationException extends ValidationException {
    public AppointmentValidationException(String message) {
        super(message);
    }

    public AppointmentValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
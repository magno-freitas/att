package vet.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidationUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@(.+)$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^\\(?([0-9]{2})\\)?[-. ]?([0-9]{4,5})[-. ]?([0-9]{4})$"
    );
    
    private static final Pattern NAME_PATTERN = Pattern.compile(
        "^[\\p{L} .'-]+$"
    );

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }
        
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Email inválido");
        }
    }
    
    public static void validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone não pode ser vazio");
        }
        
        // Remove non-numeric characters for validation
        String numericPhone = phone.replaceAll("[^0-9]", "");
        Matcher matcher = PHONE_PATTERN.matcher(numericPhone);
        
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Telefone inválido. Use o formato: (XX)XXXXX-XXXX");
        }
    }
    
    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        
        if (name.length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        
        Matcher matcher = NAME_PATTERN.matcher(name);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Nome contém caracteres inválidos");
        }
    }
    
    public static void validatePrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }
    }
    
    public static void validateDateFormat(String dateStr) {
        try {
            DATE_FORMAT.setLenient(false);
            DATE_FORMAT.parse(dateStr);
        } catch (Exception e) {
            throw new IllegalArgumentException("Data inválida. Use o formato dd/mm/aaaa");
        }
    }
    
    public static void validateAppointmentTime(Timestamp time) {
        if (time == null) {
            throw new IllegalArgumentException("Horário não pode ser nulo");
        }
        
        // Check if appointment is in the past
        if (time.before(new Timestamp(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("Não é possível agendar para datas passadas");
        }
        
        // Check business hours (8:00 - 18:00)
        Date date = new Date(time.getTime());
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        int hour = Integer.parseInt(hourFormat.format(date));
        
        if (hour < 8 || hour >= 18) {
            throw new IllegalArgumentException("Horário deve estar entre 8:00 e 18:00");
        }
    }
    
    public static void validateTemperature(String temperature) {
        try {
            double temp = Double.parseDouble(temperature);
            if (temp < 35 || temp > 43) {
                throw new IllegalArgumentException("Temperatura fora da faixa normal (35-43°C)");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Temperatura inválida");
        }
    }
    
    public static void validateWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Peso deve ser maior que zero");
        }
        if (weight > 200) {
            throw new IllegalArgumentException("Peso excede o limite máximo");
        }
    }

    public static void validateNotes(String notes) {
        if (notes != null && notes.length() > 1000) {
            throw new IllegalArgumentException("Observações excedem o limite de 1000 caracteres");
        }
    }
}
-- Add new tables for the enhanced functionality

-- Medical Records table
CREATE TABLE IF NOT EXISTS medical_records (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pet_id INT NOT NULL,
    record_date DATE NOT NULL,
    weight DECIMAL(5,2),
    temperature VARCHAR(10),
    symptoms TEXT,
    diagnosis TEXT,
    treatment TEXT,
    prescriptions TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id)
);

-- Vaccine Records table
CREATE TABLE IF NOT EXISTS vaccine_records (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pet_id INT NOT NULL,
    vaccine_name VARCHAR(100) NOT NULL,
    application_date DATE NOT NULL,
    next_dose_date DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id)
);

-- Notification Log table
CREATE TABLE IF NOT EXISTS notification_log (
    id INT PRIMARY KEY AUTO_INCREMENT,
    reference_id INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SMS Log table
CREATE TABLE IF NOT EXISTS sms_log (
    id INT PRIMARY KEY AUTO_INCREMENT,
    phone_number VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add price column to appointments table
ALTER TABLE appointments 
ADD COLUMN price DECIMAL(10,2) DEFAULT 0.0;

-- Add status tracking for appointments
ALTER TABLE appointments 
ADD COLUMN status VARCHAR(20) DEFAULT 'agendado';

-- Add reorder level for vaccine stock
ALTER TABLE vaccine_stock 
ADD COLUMN reorder_level INT DEFAULT 10;

-- Add indexes for better performance
CREATE INDEX idx_appointments_start_time ON appointments(start_time);
CREATE INDEX idx_vaccine_records_pet_id ON vaccine_records(pet_id);
CREATE INDEX idx_medical_records_pet_id ON medical_records(pet_id);
CREATE INDEX idx_notification_log_sent_at ON notification_log(sent_at);
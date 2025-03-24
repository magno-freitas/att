-- Views for common queries

-- Daily schedule view
CREATE OR REPLACE VIEW vw_daily_schedule AS
SELECT 
    a.appointment_id,
    a.start_time,
    a.end_time,
    a.service,
    a.service_type,
    a.status,
    p.name as pet_name,
    c.name as client_name,
    c.phone as client_phone
FROM appointments a
JOIN pets p ON a.pet_id = p.id
JOIN clients c ON p.client_id = c.id
WHERE DATE(a.start_time) = CURDATE()
ORDER BY a.start_time;

-- Pet health summary view
CREATE OR REPLACE VIEW vw_pet_health_summary AS
SELECT 
    p.id as pet_id,
    p.name as pet_name,
    p.species,
    p.breed,
    c.name as owner_name,
    mr.record_date as last_visit_date,
    mr.weight as current_weight,
    mr.diagnosis as last_diagnosis,
    vr.vaccine_name as last_vaccine,
    vr.next_dose_date as next_vaccine_due
FROM pets p
LEFT JOIN clients c ON p.client_id = c.id
LEFT JOIN (
    SELECT pet_id, record_date, weight, diagnosis,
           ROW_NUMBER() OVER (PARTITION BY pet_id ORDER BY record_date DESC) as rn
    FROM medical_records
) mr ON mr.pet_id = p.id AND mr.rn = 1
LEFT JOIN (
    SELECT pet_id, vaccine_name, next_dose_date,
           ROW_NUMBER() OVER (PARTITION BY pet_id ORDER BY application_date DESC) as rn
    FROM vaccine_records
) vr ON vr.pet_id = p.id AND vr.rn = 1;

-- Revenue summary view
CREATE OR REPLACE VIEW vw_revenue_summary AS
SELECT 
    DATE(start_time) as service_date,
    service_type,
    COUNT(*) as total_appointments,
    SUM(price) as total_revenue,
    AVG(price) as avg_price,
    SUM(CASE WHEN status = 'cancelado' THEN 1 ELSE 0 END) as cancellations
FROM appointments
GROUP BY DATE(start_time), service_type;

-- Vaccine inventory status view
CREATE OR REPLACE VIEW vw_vaccine_inventory_status AS
SELECT 
    v.vaccine_name,
    v.quantity,
    v.reorder_level,
    v.expiry_date,
    v.manufacturer,
    v.batch_number,
    COUNT(vr.id) as doses_administered,
    MIN(vr.application_date) as first_use_date,
    MAX(vr.application_date) as last_use_date
FROM vaccine_stock v
LEFT JOIN vaccine_records vr ON v.vaccine_name = vr.vaccine_name
GROUP BY v.vaccine_name, v.quantity, v.reorder_level, v.expiry_date, v.manufacturer, v.batch_number;
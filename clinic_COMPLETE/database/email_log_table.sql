-- Email log table for tracking sent emails
CREATE TABLE IF NOT EXISTS email_log (
    id INT PRIMARY KEY AUTO_INCREMENT,
    to_email VARCHAR(100) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'sent',
    error_message TEXT
);

CREATE INDEX idx_email_log_sent_at ON email_log(sent_at);
CREATE INDEX idx_email_log_status ON email_log(status);
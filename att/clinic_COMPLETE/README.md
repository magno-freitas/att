# Veterinary Clinic Management System

The Veterinary Clinic Management System is a comprehensive solution for managing veterinary clinics. It provides functionality for:

- Appointment scheduling and management
- Client and pet record management  
- Medical records tracking
- Notification system for appointments
- Reporting and analytics
- Resource management
- Billing and payments

## Configuration

The system uses two main configuration files:

1. `application.properties` - Main application settings
2. `database.properties` - Database connection settings

## Getting Started

1. Ensure MySQL is installed and running
2. Create a database named `vet_clinic` 
3. Update database.properties with your credentials
4. Run Application.java to start the system

## Key Features

- Double-booking prevention
- Working hours enforcement 
- SMS/Email notifications
- Audit logging
- Transaction management
- Connection pooling
- Dependency injection

## Architecture 

The system follows a layered architecture:

- UI Layer (Main Menu, Forms)
- Service Layer (Business Logic)  
- Data Access Layer (Database)
- Common Utilities Layer

Key patterns used:
- Singleton pattern for ServiceFactory
- Factory pattern for Services
- Builder pattern for complex objects
- Strategy pattern for validations
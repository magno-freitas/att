package vet.ui;

import vet.*;
import vet.service.*;
import vet.util.ValidationUtils;
import vet.exception.ValidationException;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MenuHandlers {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final Scanner scanner = new Scanner(System.in);
    private static final ServiceFactory serviceFactory = ServiceFactory.getInstance();

    // Client Management
    public static void handleAddClient() {
        try {
            System.out.println("\n=== Cadastro de Cliente ===");
            
            System.out.print("Nome: ");
            String name = scanner.nextLine();
            ValidationUtils.validateName(name);
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            ValidationUtils.validateEmail(email);
            
            System.out.print("Telefone: ");
            String phone = scanner.nextLine();
            ValidationUtils.validatePhone(phone);
            
            System.out.print("Endereço: ");
            String address = scanner.nextLine();
            
            Client client = new Client();
            client.setName(name);
            client.setEmail(email);
            client.setPhone(phone);
            client.setAddress(address);
            
            serviceFactory.getClientService().addClient(client);
            System.out.println("Cliente cadastrado com sucesso!");
            
        } catch (ValidationException e) {
            System.out.println("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    // Pet Management
    public static void handleAddPet() {
        try {
            System.out.println("\n=== Cadastro de Pet ===");
            
            System.out.print("ID do Cliente: ");
            int clientId = Integer.parseInt(scanner.nextLine());
            
            // Validate client exists
            Client client = serviceFactory.getClientService().getClientById(clientId);
            if (client == null) {
                System.out.println("Cliente não encontrado!");
                return;
            }
            
            System.out.print("Nome do Pet: ");
            String name = scanner.nextLine();
            ValidationUtils.validateName(name);
            
            System.out.print("Espécie: ");
            String species = scanner.nextLine();
            
            System.out.print("Raça: ");
            String breed = scanner.nextLine();
            
            System.out.print("Data de Nascimento (dd/mm/aaaa): ");
            String birthDateStr = scanner.nextLine();
            ValidationUtils.validateDateFormat(birthDateStr);
            Date birthDate = new Date(DATE_FORMAT.parse(birthDateStr).getTime());
            
            Pet pet = new Pet();
            pet.setClientId(clientId);
            pet.setName(name);
            pet.setSpecies(species);
            pet.setBreed(breed);
            pet.setBirthDate(birthDate);
            
            serviceFactory.getPetService().addPet(pet);
            System.out.println("Pet cadastrado com sucesso!");
            
        } catch (ValidationException e) {
            System.out.println("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar pet: " + e.getMessage());
        }
    }

    // Appointment Management
    public static void handleScheduleAppointment() {
        try {
            System.out.println("\n=== Agendamento de Serviço ===");
            
            // Get pet ID
            System.out.print("ID do Pet: ");
            int petId = Integer.parseInt(scanner.nextLine());
            
            // Validate pet exists
            Pet pet = serviceFactory.getPetService().getPetById(petId);
            if (pet == null) {
                System.out.println("Pet não encontrado!");
                return;
            }
            
            // Select service type
            System.out.println("\nTipos de Serviço:");
            System.out.println("1. Banho");
            System.out.println("2. Tosa");
            System.out.println("3. Consulta");
            System.out.println("4. Vacina");
            System.out.print("Escolha o serviço: ");
            
            int serviceChoice = Integer.parseInt(scanner.nextLine());
            ServiceType serviceType;
            String service;
            
            switch (serviceChoice) {
                case 1:
                    serviceType = ServiceType.BANHO;
                    service = "Banho";
                    break;
                case 2:
                    serviceType = ServiceType.TOSA;
                    service = "Tosa";
                    break;
                case 3:
                    serviceType = ServiceType.CONSULTA;
                    service = "Consulta Veterinária";
                    break;
                case 4:
                    serviceType = ServiceType.VACINA;
                    service = "Vacinação";
                    if (!serviceFactory.getPetService().checkVaccinesUpToDate(petId)) {
                        System.out.println("ATENÇÃO: Vacinas do pet não estão em dia!");
                    }
                    break;
                default:
                    System.out.println("Opção inválida!");
                    return;
            }
            
            // Get date and time
            System.out.print("\nData (dd/mm/aaaa): ");
            String dateStr = scanner.nextLine();
            ValidationUtils.validateDateFormat(dateStr);
            
            System.out.print("Horário (HH:mm): ");
            String timeStr = scanner.nextLine();
            
            // Combine date and time
            java.util.Date dateTime = new java.util.Date(
                DATE_FORMAT.parse(dateStr).getTime() + 
                TIME_FORMAT.parse(timeStr).getTime() % (24*60*60*1000)
            );
            Timestamp startTime = new Timestamp(dateTime.getTime());
            
            // Validate business hours and availability
            ValidationUtils.validateAppointmentTime(startTime);
            
            // Create appointment
            Appointment appointment = new Appointment();
            appointment.setPetId(petId);
            appointment.setService(service);
            appointment.setServiceType(serviceType);
            appointment.setStartTime(startTime);
            appointment.setEndTime(new Timestamp(startTime.getTime() + 30*60*1000)); // 30 minutes duration
            appointment.setStatus("agendado");
            
            serviceFactory.getAppointmentService().scheduleAppointment(appointment);
            
            // Send confirmation
            Client client = serviceFactory.getClientService().getClientById(pet.getClientId());
            serviceFactory.getNotificationService().sendAppointmentConfirmation(appointment, client);
            
            System.out.println("Agendamento realizado com sucesso!");
            
        } catch (ValidationException e) {
            System.out.println("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao agendar serviço: " + e.getMessage());
        }
    }

    // Medical Records Management
    public static void handleAddMedicalRecord() {
        try {
            System.out.println("\n=== Novo Registro Médico ===");
            
            System.out.print("ID do Pet: ");
            int petId = Integer.parseInt(scanner.nextLine());
            
            // Validate pet exists
            Pet pet = serviceFactory.getPetService().getPetById(petId);
            if (pet == null) {
                System.out.println("Pet não encontrado!");
                return;
            }
            
            MedicalRecord record = new MedicalRecord();
            record.setPetId(petId);
            record.setRecordDate(new Date(System.currentTimeMillis()));
            
            System.out.print("Peso (kg): ");
            double weight = Double.parseDouble(scanner.nextLine());
            ValidationUtils.validateWeight(weight);
            record.setWeight(weight);
            
            System.out.print("Temperatura: ");
            String temperature = scanner.nextLine();
            ValidationUtils.validateTemperature(temperature);
            record.setTemperature(temperature);
            
            System.out.print("Sintomas: ");
            record.setSymptoms(scanner.nextLine());
            
            System.out.print("Diagnóstico: ");
            record.setDiagnosis(scanner.nextLine());
            
            System.out.print("Tratamento: ");
            record.setTreatment(scanner.nextLine());
            
            System.out.print("Prescrições: ");
            record.setPrescriptions(scanner.nextLine());
            
            System.out.print("Observações: ");
            String notes = scanner.nextLine();
            ValidationUtils.validateNotes(notes);
            record.setNotes(notes);
            
            serviceFactory.getMedicalRecordService().addMedicalRecord(record);
            System.out.println("Registro médico adicionado com sucesso!");
            
        } catch (ValidationException e) {
            System.out.println("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao adicionar registro médico: " + e.getMessage());
        }
    }

    // Reports
    public static void handleGenerateReports() {
        try {
            System.out.println("\n=== Relatórios ===");
            
            System.out.print("Data Inicial (dd/mm/aaaa): ");
            String startDateStr = scanner.nextLine();
            ValidationUtils.validateDateFormat(startDateStr);
            Date startDate = new Date(DATE_FORMAT.parse(startDateStr).getTime());
            
            System.out.print("Data Final (dd/mm/aaaa): ");
            String endDateStr = scanner.nextLine();
            ValidationUtils.validateDateFormat(endDateStr);
            Date endDate = new Date(DATE_FORMAT.parse(endDateStr).getTime());
            
            ReportingService reportingService = serviceFactory.getReportingService();
            
            // Appointments Report
            System.out.println("\nAgendamentos no Período:");
            List<Appointment> appointments = reportingService.getDailyAppointments(startDate);
            for (Appointment apt : appointments) {
                System.out.printf("%s - %s - %s - %s%n",
                    apt.getStartTime(), apt.getService(),
                    apt.getStatus(), apt.getNotes());
            }
            
            // Revenue Report
            System.out.println("\nFaturamento por Serviço:");
            Map<String, Double> revenue = reportingService.getRevenueByService(startDate, endDate);
            revenue.forEach((service, value) -> 
                System.out.printf("%s: R$ %.2f%n", service, value));
            
            // Cancellation Rate
            double cancelRate = reportingService.getCancellationRate(startDate, endDate);
            System.out.printf("\nTaxa de Cancelamento: %.1f%%%n", cancelRate);
            
            // Vaccine Inventory
            System.out.println("\nEstoque de Vacinas Baixo:");
            Map<String, Integer> lowStock = reportingService.getVaccineInventoryReport();
            lowStock.forEach((vaccine, quantity) ->
                System.out.printf("%s: %d unidades%n", vaccine, quantity));
            
        } catch (ValidationException e) {
            System.out.println("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao gerar relatórios: " + e.getMessage());
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int readInt(String prompt) throws NumberFormatException {
        return Integer.parseInt(readString(prompt));
    }

    private static double readDouble(String prompt) throws NumberFormatException {
        return Double.parseDouble(readString(prompt));
    }

    private static Date readDate(String prompt) throws ParseException {
        String dateStr = readString(prompt);
        ValidationUtils.validateDateFormat(dateStr);
        return new Date(DATE_FORMAT.parse(dateStr).getTime());
    }
}
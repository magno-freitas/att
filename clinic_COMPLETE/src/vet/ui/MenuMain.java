package vet.ui;

import vet.service.ServiceFactory;

public class MenuMain {
    private static final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    
    public static void main(String[] args) {
        try {
            // Start services
            serviceFactory.startServices();
            
            // Run main menu loop
            while (true) {
                MainMenu.showMainMenu();
                int option = MenuHandlers.readInt("Opção: ");
                
                switch (option) {
                    case 1: // Clients
                        handleClientMenu();
                        break;
                    case 2: // Pets
                        handlePetMenu();
                        break;
                    case 3: // Appointments
                        handleAppointmentMenu();
                        break;
                    case 4: // Medical Records
                        handleMedicalRecordMenu();
                        break;
                    case 5: // Vaccines
                        handleVaccineMenu();
                        break;
                    case 6: // Reports
                        handleReportMenu();
                        break;
                    case 7: // Settings
                        handleSettingsMenu();
                        break;
                    case 8: // Exit
                        System.out.println("Encerrando o sistema...");
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro fatal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cleanup
            serviceFactory.shutdownServices();
        }
    }
    
    private static void handleClientMenu() {
        while (true) {
            MainMenu.showClientMenu();
            try {
                int option = MenuHandlers.readInt("Opção: ");
                switch (option) {
                    case 1:
                        MenuHandlers.handleAddClient();
                        break;
                    case 2:
                        // Search client...
                        break;
                    case 3:
                        // Update client...
                        break;
                    case 4:
                        // List clients...
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
    
    private static void handlePetMenu() {
        while (true) {
            MainMenu.showPetMenu();
            try {
                int option = MenuHandlers.readInt("Opção: ");
                switch (option) {
                    case 1:
                        MenuHandlers.handleAddPet();
                        break;
                    case 2:
                        // Search pet...
                        break;
                    case 3:
                        // Update pet...
                        break;
                    case 4:
                        // List pets...
                        break;
                    case 5:
                        // Vaccine history...
                        break;
                    case 6:
                        // Medical history...
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
    
    private static void handleAppointmentMenu() {
        while (true) {
            MainMenu.showAppointmentMenu();
            try {
                int option = MenuHandlers.readInt("Opção: ");
                switch (option) {
                    case 1:
                        MenuHandlers.handleScheduleAppointment();
                        break;
                    case 2:
                        // Cancel appointment...
                        break;
                    case 3:
                        // Reschedule...
                        break;
                    case 4:
                        // List today's appointments...
                        break;
                    case 5:
                        // Search appointment...
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
    
    private static void handleMedicalRecordMenu() {
        while (true) {
            MainMenu.showMedicalRecordMenu();
            try {
                int option = MenuHandlers.readInt("Opção: ");
                switch (option) {
                    case 1:
                        MenuHandlers.handleAddMedicalRecord();
                        break;
                    case 2:
                        // Search history...
                        break;
                    case 3:
                        // Update record...
                        break;
                    case 4:
                        // Add prescription...
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
    
    private static void handleVaccineMenu() {
        while (true) {
            MainMenu.showVaccineMenu();
            try {
                int option = MenuHandlers.readInt("Opção: ");
                switch (option) {
                    case 1:
                        // Record vaccination...
                        break;
                    case 2:
                        // Check vaccine card...
                        break;
                    case 3:
                        // Manage inventory...
                        break;
                    case 4:
                        // Due vaccines...
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
    
    private static void handleReportMenu() {
        while (true) {
            MainMenu.showReportMenu();
            try {
                int option = MenuHandlers.readInt("Opção: ");
                switch (option) {
                    case 1:
                        MenuHandlers.handleGenerateReports();
                        break;
                    case 2:
                        // Revenue report...
                        break;
                    case 3:
                        // Cancellation rate...
                        break;
                    case 4:
                        // Vaccine inventory...
                        break;
                    case 5:
                        // Notification history...
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
    
    private static void handleSettingsMenu() {
        while (true) {
            MainMenu.showSettingsMenu();
            try {
                int option = MenuHandlers.readInt("Opção: ");
                switch (option) {
                    case 1:
                        // Manage prices...
                        break;
                    case 2:
                        // Business hours...
                        break;
                    case 3:
                        // Notification settings...
                        break;
                    case 4:
                        // User management...
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
}
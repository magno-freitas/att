package main.java.vet.ui;

import main.java.vet.MainMenu;
import main.java.vet.service.ServiceFactory;

public class MenuMain {
    private static final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    
    public static void main(String[] args) {
        try {
            serviceFactory.startServices();
            
            while (true) {
                MainMenu.showMainMenu();
                int option = MenuHandlers.readInt("Opção: ");
                
                switch (option) {
                    case 1: // Clientes e Pets
                        handleClientPetMenu();
                        break;
                    case 2: // Atendimentos
                        handleAttendanceMenu();
                        break;
                    case 3: // Prontuários
                        handleMedicalRecordMenu();
                        break;
                    case 4: // Vacinação
                        handleVaccinationMenu();
                        break;
                    case 5: // Exames
                        handleExamMenu();
                        break;
                    case 6: // Cirurgias
                        handleSurgeryMenu();
                        break;
                    case 7: // Financeiro
                        handleFinancialMenu();
                        break;
                    case 8: // Relatórios
                        handleReportMenu();
                        break;
                    case 9: // Configurações
                        handleSettingsMenu();
                        break;
                    case 0: // Sair
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
            serviceFactory.shutdownServices();
        }
    }

    private static void handleClientPetMenu() {
        while (true) {
            MainMenu.showClientPetMenu();
            try {
                int option = MenuHandlers.readInt("Opção: ");
                switch (option) {
                    case 1: // Cadastrar Cliente
                        MenuHandlers.handleAddClient();
                        break;
                    case 2: // Cadastrar Pet
                        MenuHandlers.handleAddPet();
                        break;
                    case 3: // Buscar Cliente/Pet
                        MenuHandlers.handleSearchClientPet();
                        break;
                    case 4: // Atualizar Dados
                        MenuHandlers.handleUpdateClientPet();
                        break;
                    case 5: // Histórico Completo
                        MenuHandlers.handleViewHistory();
                        break;
                    case 0: // Voltar
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void handleAttendanceMenu() {
        while (true) {
            MainMenu.showAttendanceMenu();
            try {
                int option = MenuHandlers.readInt("Opção: ");
                switch (option) {
                    case 1: // Novo Atendimento
                        MenuHandlers.handleNewAttendance();
                        break;
                    case 2: // Consultas do Dia
                        MenuHandlers.handleTodayAppointments();
                        break;
                    case 3: // Emergências
                        MenuHandlers.handleEmergency();
                        break;
                    case 4: // Internações
                        MenuHandlers.handleHospitalization();
                        break;
                    case 0: // Voltar
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
    
    private static void handleVaccinationMenu() {
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

    private static void handleExamMenu() {
        while (true) {
            MainMenu.showExamMenu();
            try {
                int option = MenuHandlers.readInt("Opção: ");
                switch (option) {
                    case 1: // Agendar Exame
                        MenuHandlers.handleScheduleExam();
                        break;
                    case 2: // Consultar Exames
                        MenuHandlers.handleConsultExams();
                        break;
                    case 3: // Resultados Pendentes
                        MenuHandlers.handlePendingResults();
                        break;
                    case 0: // Voltar
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void handleSurgeryMenu() {
        while (true) {
            MainMenu.showSurgeryMenu();
            try {
                int option = MenuHandlers.readInt("Opção: ");
                switch (option) {
                    case 1: // Agendar Cirurgia
                        MenuHandlers.handleScheduleSurgery();
                        break;
                    case 2: // Consultar Cirurgias
                        MenuHandlers.handleConsultSurgeries();
                        break;
                    case 3: // Cirurgias Pendentes
                        MenuHandlers.handlePendingSurgeries();
                        break;
                    case 0: // Voltar
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void handleFinancialMenu() {
        while (true) {
            MainMenu.showFinancialMenu();
            try {
                int option = MenuHandlers.readInt("Opção: ");
                switch (option) {
                    case 1: // Gerar Fatura
                        MenuHandlers.handleGenerateInvoice();
                        break;
                    case 2: // Consultar Faturas
                        MenuHandlers.handleConsultInvoices();
                        break;
                    case 3: // Pagamentos Pendentes
                        MenuHandlers.handlePendingPayments();
                        break;
                    case 0: // Voltar
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
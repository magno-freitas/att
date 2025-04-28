package vet;

import java.util.Scanner;
import java.util.logging.Logger;

import vet.exception.ValidationException;
import vet.model.Client;
import vet.service.ClientService;
import vet.service.ServiceFactory;
import vet.util.ValidationUtils;

public class MainMenu {
    private static final Logger LOGGER = Logger.getLogger(MainMenu.class.getName());
    private static Scanner scanner = new Scanner(System.in);
    private static ServiceFactory serviceFactory = ServiceFactory.getInstance();

    public static void showMainMenu() {
        System.out.println("\n=== Sistema Veterinário ===");
        System.out.println("1. Gestão de Clientes");
        System.out.println("2. Gestão de Pets");
        System.out.println("3. Agendamentos");
        System.out.println("4. Prontuário Médico");
        System.out.println("5. Vacinas");
        System.out.println("6. Relatórios");
        System.out.println("7. Configurações");
        System.out.println("8. Sair");
        System.out.print("Escolha uma opção: ");
    }

    public static void showClientMenu() {
        System.out.println("\n=== Gestão de Clientes ===");
        System.out.println("1. Cadastrar Novo Cliente");
        System.out.println("2. Buscar Cliente");
        System.out.println("3. Atualizar Cliente");
        System.out.println("4. Listar Todos Clientes");
        System.out.println("5. Voltar");
        System.out.print("Escolha uma opção: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        handleClientMenu(choice);
    }

    private static void handleClientMenu(int choice) {
        try {
            switch (choice) {
                case 1:
                    addClient();
                    break;
                case 2:
                    findClient();
                    break;
                case 3:
                    updateClient();
                    break;
                case 4:
                    listAllClients();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        } catch (Exception e) {
            LOGGER.severe("Error in client menu: " + e.getMessage());
            System.out.println("Ocorreu um erro: " + e.getMessage());
        }
    }
    
    private static void addClient() {
        try {
            System.out.println("\n=== Cadastrar Novo Cliente ===");
            
            System.out.print("Nome: ");
            String name = scanner.nextLine();
            
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
            
            ClientService clientService = serviceFactory.getClientService();
            clientService.addClient(client);
            
            System.out.println("Cliente cadastrado com sucesso!");
            
        } catch (ValidationException e) {
            System.err.println("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Error adding client: " + e.getMessage());
            System.err.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }
    
    private static void findClient() {
        System.out.println("\n=== Buscar Cliente ===");
        System.out.print("Digite o ID ou nome do cliente: ");
        String search = scanner.nextLine();
        
        try {
            ClientService clientService = serviceFactory.getClientService();
            
            if (search.matches("\\d+")) {
                // Search by ID
                int id = Integer.parseInt(search);
                Client client = clientService.getClientById(id);
                if (client != null) {
                    displayClient(client);
                } else {
                    System.out.println("Cliente não encontrado.");
                }
            } else {
                // Search by name
                var clients = clientService.findClientsByName(search);
                if (clients.isEmpty()) {
                    System.out.println("Nenhum cliente encontrado.");
                } else {
                    System.out.println("\nClientes encontrados:");
                    for (Client client : clients) {
                        displayClient(client);
                        System.out.println("------------------------");
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Error finding client: " + e.getMessage());
            System.err.println("Erro ao buscar cliente: " + e.getMessage());
        }
    }
    
    private static void displayClient(Client client) {
        System.out.println("ID: " + client.getId());
        System.out.println("Nome: " + client.getName());
        System.out.println("Email: " + client.getEmail());
        System.out.println("Telefone: " + client.getPhone());
        System.out.println("Endereço: " + client.getAddress());
    }
    
    private static void updateClient() {
        System.out.println("\n=== Atualizar Cliente ===");
        System.out.print("Digite o ID do cliente: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        try {
            ClientService clientService = serviceFactory.getClientService();
            Client client = clientService.getClientById(id);
            
            if (client == null) {
                System.out.println("Cliente não encontrado.");
                return;
            }
            
            System.out.println("\nDados atuais:");
            displayClient(client);
            
            System.out.println("\nDigite os novos dados (deixe em branco para manter o valor atual):");
            
            System.out.print("Nome [" + client.getName() + "]: ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                client.setName(name);
            }
            
            System.out.print("Email [" + client.getEmail() + "]: ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) {
                ValidationUtils.validateEmail(email);
                client.setEmail(email);
            }
            
            System.out.print("Telefone [" + client.getPhone() + "]: ");
            String phone = scanner.nextLine();
            if (!phone.isEmpty()) {
                ValidationUtils.validatePhone(phone);
                client.setPhone(phone);
            }
            
            System.out.print("Endereço [" + client.getAddress() + "]: ");
            String address = scanner.nextLine();
            if (!address.isEmpty()) {
                client.setAddress(address);
            }
            
            clientService.updateClient(client);
            System.out.println("Cliente atualizado com sucesso!");
            
        } catch (ValidationException e) {
            System.err.println("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Error updating client: " + e.getMessage());
            System.err.println("Erro ao atualizar cliente: " + e.getMessage());
        }
    }
    
    private static void listAllClients() {
        System.out.println("\n=== Lista de Clientes ===");
        
        try {
            ClientService clientService = serviceFactory.getClientService();
            var clients = clientService.getAllClients();
            
            if (clients.isEmpty()) {
                System.out.println("Nenhum cliente cadastrado.");
            } else {
                for (Client client : clients) {
                    displayClient(client);
                    System.out.println("------------------------");
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Error listing clients: " + e.getMessage());
            System.err.println("Erro ao listar clientes: " + e.getMessage());
        }
    }

    public static void showPetMenu() {
        System.out.println("\n=== Gestão de Pets ===");
        System.out.println("1. Cadastrar Novo Pet");
        System.out.println("2. Buscar Pet");
        System.out.println("3. Atualizar Pet");
        System.out.println("4. Listar Todos Pets");
        System.out.println("5. Histórico de Vacinas");
        System.out.println("6. Histórico Médico");
        System.out.println("7. Voltar");
        System.out.print("Escolha uma opção: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        handlePetMenu(choice);
    }
    
    private static void handlePetMenu(int choice) {
        // Implementation will be added later
        System.out.println("Funcionalidade em desenvolvimento.");
    }

    public static void showAppointmentMenu() {
        System.out.println("\n=== Agendamentos ===");
        System.out.println("1. Novo Agendamento");
        System.out.println("2. Cancelar Agendamento");
        System.out.println("3. Reagendar");
        System.out.println("4. Listar Agendamentos do Dia");
        System.out.println("5. Buscar Agendamento");
        System.out.println("6. Voltar");
        System.out.print("Escolha uma opção: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        handleAppointmentMenu(choice);
    }
    
    private static void handleAppointmentMenu(int choice) {
        // Implementation will be added later
        System.out.println("Funcionalidade em desenvolvimento.");
    }

    public static void showMedicalRecordMenu() {
        System.out.println("\n=== Prontuário Médico ===");
        System.out.println("1. Novo Registro Médico");
        System.out.println("2. Buscar Histórico");
        System.out.println("3. Atualizar Registro");
        System.out.println("4. Registrar Prescrição");
        System.out.println("5. Voltar");
        System.out.print("Escolha uma opção: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        handleMedicalRecordMenu(choice);
    }
    
    private static void handleMedicalRecordMenu(int choice) {
        // Implementation will be added later
        System.out.println("Funcionalidade em desenvolvimento.");
    }

    public static void showVaccineMenu() {
        System.out.println("\n=== Gestão de Vacinas ===");
        System.out.println("1. Registrar Vacinação");
        System.out.println("2. Verificar Carteira de Vacinas");
        System.out.println("3. Gerenciar Estoque");
        System.out.println("4. Vacinas Pendentes");
        System.out.println("5. Voltar");
        System.out.print("Escolha uma opção: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        handleVaccineMenu(choice);
    }
    
    private static void handleVaccineMenu(int choice) {
        // Implementation will be added later
        System.out.println("Funcionalidade em desenvolvimento.");
    }

    public static void showReportMenu() {
        System.out.println("\n=== Relatórios ===");
        System.out.println("1. Agendamentos por Período");
        System.out.println("2. Faturamento por Serviço");
        System.out.println("3. Taxa de Cancelamentos");
        System.out.println("4. Estoque de Vacinas");
        System.out.println("5. Histórico de Notificações");
        System.out.println("6. Voltar");
        System.out.print("Escolha uma opção: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        handleReportMenu(choice);
    }
    
    private static void handleReportMenu(int choice) {
        // Implementation will be added later
        System.out.println("Funcionalidade em desenvolvimento.");
    }

    public static void showSettingsMenu() {
        System.out.println("\n=== Configurações ===");
        System.out.println("1. Preços dos Serviços");
        System.out.println("2. Horário de Funcionamento");
        System.out.println("3. Configurar Notificações");
        System.out.println("4. Gerenciar Usuários");
        System.out.println("5. Voltar");
        System.out.print("Escolha uma opção: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        handleSettingsMenu(choice);
    }
    
    private static void handleSettingsMenu(int choice) {
        // Implementation will be added later
        System.out.println("Funcionalidade em desenvolvimento.");
    }
    
    public static void show() {
        showMainMenu();
    }
}
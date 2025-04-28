package vet;

import java.util.Scanner;
import java.util.logging.Logger;

import vet.config.ConfigurationManager;
import vet.dao.ConnectionPool;
import vet.service.ServiceFactory;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            // Initialize configuration
            ConfigurationManager.getInstance().initialize();
            
            // Initialize database connection pool
            ConnectionPool.initialize();
            
            // Initialize services
            ServiceFactory.getInstance().initialize();

            LOGGER.info("Application started successfully");
            
            boolean running = true;
            while (running) {
                MainMenu.showMainMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        MainMenu.showClientMenu();
                        break;
                    case 2:
                        MainMenu.showPetMenu();
                        break;
                    case 3:
                        MainMenu.showAppointmentMenu();
                        break;
                    case 4:
                        MainMenu.showMedicalRecordMenu();
                        break;
                    case 5:
                        MainMenu.showVaccineMenu();
                        break;
                    case 6:
                        MainMenu.showReportMenu();
                        break;
                    case 7:
                        MainMenu.showSettingsMenu();
                        break;
                    case 8:
                        running = false;
                        System.out.println("Saindo do sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Application failed to start: " + e.getMessage());
            e.printStackTrace();
        } finally {
            shutdown();
            scanner.close();
        }
    }
    
    private static void shutdown() {
        try {
            ServiceFactory.getInstance().shutdown();
            ConnectionPool.shutdown();
            LOGGER.info("Application shutdown completed");
        } catch (Exception e) {
            LOGGER.severe("Error during shutdown: " + e.getMessage());
        }
    }
}
package main.java.vet;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
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
        scanner.close();
    }
}
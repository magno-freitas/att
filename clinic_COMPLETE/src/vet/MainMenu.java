package vet;

public class MainMenu {
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
    }

    public static void showMedicalRecordMenu() {
        System.out.println("\n=== Prontuário Médico ===");
        System.out.println("1. Novo Registro Médico");
        System.out.println("2. Buscar Histórico");
        System.out.println("3. Atualizar Registro");
        System.out.println("4. Registrar Prescrição");
        System.out.println("5. Voltar");
        System.out.print("Escolha uma opção: ");
    }

    public static void showVaccineMenu() {
        System.out.println("\n=== Gestão de Vacinas ===");
        System.out.println("1. Registrar Vacinação");
        System.out.println("2. Verificar Carteira de Vacinas");
        System.out.println("3. Gerenciar Estoque");
        System.out.println("4. Vacinas Pendentes");
        System.out.println("5. Voltar");
        System.out.print("Escolha uma opção: ");
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
    }

    public static void showSettingsMenu() {
        System.out.println("\n=== Configurações ===");
        System.out.println("1. Preços dos Serviços");
        System.out.println("2. Horário de Funcionamento");
        System.out.println("3. Configurar Notificações");
        System.out.println("4. Gerenciar Usuários");
        System.out.println("5. Voltar");
        System.out.print("Escolha uma opção: ");
    }

    public static void show() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }
}
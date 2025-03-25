public class ex7 {
    public static void main(String[] args) {
        Contribuinte[] contribuintes = new Contribuinte[5];

        // Criando 5 contribuintes com dados diferentes
        contribuintes[0] = new Contribuinte("João da Silva", "123.456.789-00", "PR", 40000);
        contribuintes[1] = new Contribuinte("Maria Santos", "987.654.321-00", "SC", 8000);
        contribuintes[2] = new Contribuinte("Pedro Oliveira", "456.789.123-00", "RS", 25000);
        contribuintes[3] = new Contribuinte("Ana Souza", "789.123.456-00", "PR", 60000);
        contribuintes[4] = new Contribuinte("Carlos Lima", "321.654.987-00", "SC", 3000);

        // Encontrar quem paga mais imposto
        Contribuinte maisImposto = contribuintes[0];
        for (int i = 1; i < contribuintes.length; i++) {
            if (contribuintes[i].getImpostoAPagar() > maisImposto.getImpostoAPagar()) {
                maisImposto = contribuintes[i];
            }
        }

        // Encontrar quem paga menos imposto
        Contribuinte menosImposto = contribuintes[0];
        for (int i = 1; i < contribuintes.length; i++) {
            if (contribuintes[i].getImpostoAPagar() < menosImposto.getImpostoAPagar()) {
                menosImposto = contribuintes[i];
            }
        }

        // Calcular total de impostos
        double totalImpostos = 0;
        for (Contribuinte c : contribuintes) {
            totalImpostos += c.getImpostoAPagar();
        }

        // Exibir resultados
        System.out.println("=== Relatório de Impostos ===");
        System.out.println("\nQuem mais paga imposto:");
        System.out.println(maisImposto);
        
        System.out.println("\nQuem menos paga imposto:");
        System.out.println(menosImposto);
        
        System.out.printf("\nTotal de impostos pagos: R$%.2f\n", totalImpostos);
    }
}
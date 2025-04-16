public class Main {
    public static void main(String[] args) {
        try {
            Conta conta1=new Conta("João", 1000.0);
            Conta conta2=new Conta("Maria", 500.0);

            System.out.println("Estado inicial:");
            System.out.println("Conta 1: " + conta1);
            System.out.println("Conta 2: " + conta2);

            conta1.sacar(200.0);
            conta2.depositar(150.0);
            conta1.transferir(conta2, 300.0);


            System.out.println("Estado final:");
            System.out.println("Conta 1: " + conta1);
            System.out.println("Conta 2: " + conta2);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
            
    }
}

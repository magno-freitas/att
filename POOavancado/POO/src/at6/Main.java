

public class Main {
    public static void main(String[] args) {
        
        Endereco endereco=new Endereco("Rua Vahldieck", "Apto 101", 123);
        Pessoa pessoa = new Pessoa("Magno", endereco);
        System.out.println(pessoa);
        pessoa.getEndereco().setComplemento("apto 202");
        System.out.println("\nApos alteracao: ");
        System.out.println(pessoa);

    }
}

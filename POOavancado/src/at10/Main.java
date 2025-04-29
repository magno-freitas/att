package src.at10;

public class Main {
        public static void main(String[] args) {
            
            Setor setor=new Setor();

            setor.adicionarFuncionario(new Funcionario("Magno", "234", 0, 10000000));
            setor.adicionarFuncionario(new Funcionario("Magno1", "2347", 2, 10000000));
            setor.adicionarFuncionario(new Funcionario("Magno2", "2346", 4, 10000000));
            setor.adicionarFuncionario(new Funcionario("Magno3", "2345", 1, 10000000));

            System.out.println("Total da folha de pagamento do setor: "+setor.totalFolhaDePagamentoSetor());
            System.out.println("Total da folha de pagamento do departamento 0: "+setor.totalFolhaDePagamentoDept(0));
            System.out.println("Funcionario com maior salario: "+setor.funcionarioMaiorSalario().getNome());
            Funcionario funcionario = setor.acharFuncionario("Magno", "234");
            if(funcionario != null) {
                System.out.println("Funcionario encontrado: " + funcionario.getNome());
            } else {
                System.out.println("Funcionario não encontrado.");
            }
        }
}

package src.at10;
import java.util.ArrayList;
import java.util.List;
public class Setor {
    private List<Funcionario> listaFuncionarios;

    public Setor() {
        listaFuncionarios = new ArrayList<>();
    }
    public void adicionarFuncionario(Funcionario funcionario) {
        listaFuncionarios.add(funcionario);
    }
    public double  totalFolhaDePagamentoSetor(){
        double total=0;
        for(Funcionario f : listaFuncionarios){
            total+=f.getSalario();

        }
        return total;
    }
    public double totalFolhaDePagamentoDept(int departamento){
        double total=0;
        for(Funcionario f: listaFuncionarios){
            if(f.getDepartamento() == departamento){
                total+=f.getSalario();
            }
        }
        return total;
    }
    public Funcionario funcionarioMaiorSalario(){
        if(listaFuncionarios.isEmpty())return null;
        Funcionario maior=listaFuncionarios.get(0);
        for(Funcionario f : listaFuncionarios){
            if(f.getSalario()>maior.getSalario()){
                maior=f;
            }
        }
        return maior;
    }
    public Funcionario acharFuncionario(String nome, String matricula ){
        for(Funcionario f:listaFuncionarios){
            if(f.getNome().equals(nome)&&f.getMatricula().equals(matricula)){
                return f;
            }
        }
        return null;
    }
}

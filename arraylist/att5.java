import java.util.ArrayList;
import java.util.Scanner;

public class att5 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        ArrayList<String> lista = new ArrayList<>();

        System.out.println("Digite a tarefa: ");
        String tarefa = sc.nextLine();
        lista.add(tarefa);
        while (true){
            System.out.println("Digite a tarefa: ");
            String tarefa2 = sc.nextLine();
            if (lista.contains(tarefa2)){
                System.out.println("Tarefa já existe.");
            } else {
                lista.add(tarefa2);
            }
            System.out.println("Deseja adicionar mais tarefas? (s/n)");
            String resposta = sc.nextLine();
            if (resposta.equalsIgnoreCase("n")){
                break;
            }
        } 
        System.out.println("Tarefas: ");
        for (String tarefa3 : lista) {
            System.out.println(tarefa3);
        }
    }
}
// Crie um gerenciador de tarefas
// Faça métodos para:
// Adicionar tarefas, caso a tarefa já exista, não insira
// Remover tarefas.
// Exibir quantas tarefas ainda restam.
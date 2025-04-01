import java.util.ArrayList;
import java.util.Scanner;

public class att6 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        ArrayList<String> lista = new ArrayList<>();

        System.out.println("Digite 5 nomes: ");
        for(int i=0;i<5;i++){
            String nome=sc.nextLine();
            lista.add(nome);
        }
        System.out.println("Lista original: ");
        for(String nome : lista){
            System.out.println(nome)  ;

        }
        ArrayList<String> listaSem =new ArrayList<>();
        for(String nome : lista){
            if(!listaSem.contains(nome)){
                listaSem.add(nome);
            }
        }
        System.out.println("Lista sem duplicatas: ");
        for(String nome : listaSem){
            System.out.println(nome);
        }
        
    }
}
// Crie uma lista de nomes
// Faça métodos para:
// Remover todos os nomes duplicados.
// Exibir a lista sem duplicatas

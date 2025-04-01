import java.util.ArrayList;
import java.util.Scanner;

public class att4 {
    public static void main(String[] args) {
        
        Scanner sc=new Scanner(System.in);
        ArrayList<Integer> lista=new ArrayList<>();
        System.out.println("Digite 5 números: ");
        for(int i=0;i<5;i++){
            int n=sc.nextInt();
            lista.add(n);
        }
        System.out.println("Digite um número para verificar se está na lista: ");
        int numero=sc.nextInt();
        boolean encontrado = false;
        for(int i=0;i<lista.size();i++){
            if(lista.get(i)==numero){
                System.out.println("Numero encontrado no indice: "+i);
                encontrado = true;
                break;
            } else {
                System.out.println("Numero não encontrado na lista.");
                break;
            }
        }
        sc.close();
    }
}

// Faça um algoritmo que descubra se uma Array List possui o número que o usuário entrou.
// Imprima também o seu índice
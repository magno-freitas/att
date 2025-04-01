import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class att2 {
    public static void main(String[] args) {
        Scanner sc =new Scanner(System.in);
        ArrayList<String> lista = new ArrayList<>();
        for(int i=0;i<5;i++){
            System.out.println("Digite um elemento: ");
            String elemento =sc.nextLine();
            lista.add(elemento);

        }
        Collections.reverse(lista);
        System.out.println("Lista inverdida: ");
        for (String elemento : lista){ 
            System.out.println(elemento);
        }
    }
}
//Crie uma Array List e imprima os últimos elementos por primeiro

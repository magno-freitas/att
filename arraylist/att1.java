import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class att1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        ArrayList<String> lista=new ArrayList<>();
        for(int i=0;i<5;i++){
            System.out.println("Digite um elemento: ");
            String elemento=scanner.nextLine();
            lista.add(elemento);

        }
        lista.sort(null);
        System.out.println("Lista ordenada: ");
        for (String elemento : lista) {
            System.out.println(elemento);
        }
        scanner.close();
    }
}
//Coloque 5 elementos em uma Array List e ordene-os

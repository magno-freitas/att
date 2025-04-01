import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class att7 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        ArrayList<Character> lista = new ArrayList<>();


        System.out.println("DIgite uma palavra: ");
        String palavra = sc.nextLine();

        for(int i=0; i<palavra.length();i++){
            char l=palavra.charAt(i);
            lista.add(l);

        }
        System.out.println("Lista original: ");
        for(char l : lista){
            System.out.print(l);
        }
        System.out.println();
        ArrayList<Character> listaInvertida = new ArrayList<>(lista);
        Collections.reverse(listaInvertida);
        System.out.println("Lista invertida: ");
        for(char l : listaInvertida){
            System.out.print(l);
        }
        System.out.println();
        if(lista.equals(listaInvertida)){
            System.out.println("A palavra é um palíndromo.");
        } else {
            System.out.println("A palavra não é um palíndromo.");
        }
    }
}
// Faça um algoritmo que verifica palíndromos
// O usuário insere uma palavra.
// Crie uma ArrayList<Character> com os caracteres da palavra.
// Verifique se a palavra é um palíndromo
// Dica: use Collections.reverse() 
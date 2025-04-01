import java.util.ArrayList;
import java.util.Scanner;

import org.hibernate.query.criteria.internal.expression.function.AggregationFunction.MAX;


public class att3 {
    public static void main(String[] args) {
        ArrayList<Double> lista=new ArrayList<>();
        Scanner sc=new Scanner(System.in);
      
        double menor=Double.MAX_VALUE;
        double maior=Double.MIN_VALUE;
        double soma=0;
       
        while(true){
            System.out.println("Digite um numero: ");
            double n=sc.nextDouble();
            if(n!=0){
                lista.add(n);
                menor=Math.min(menor, n);
                maior=Math.max(maior, n);
                soma+=n;
            }
            if(n==0){
                break;
            }
        }
        System.out.println("Menor numero: "+menor);
        System.out.println("Maior numero: "+maior);
        System.out.println("Média: "+(soma/(lista.size())));
        sc.close();
    }
}
//3)
//Crie uma Array List
// O usuário vai colocando valores decimais até ele colocar o número 0.
// Calcular:
// a) Qual o menor número
// b) Qual o maior número
// c) Calcular a média
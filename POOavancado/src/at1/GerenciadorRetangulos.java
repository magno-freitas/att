package src.at1;

// Exercícios Avançados de POO
// Para todos os exercícios dessa lista:
// Fazer método construtor, atributos privados, getters e setters, método toString, tratamento de
// exceção e Array List quando necessário.
// 1)
// Faça uma classe chamada Retangulo, com os atributos altura e largura.
// Faça um método para descobrir a área e outro para descobrir o perímetro.
// Crie 5 objetos de Retangulo.
// Crie uma lista em outra classe para armazenar todos os objetos de Retangulo
// Faça um método que encontre o Retangulo com a maior área e outro que encontre o Retangulo
// com o maior perímetro
// Imprima o toString desses Retangulos
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
public class GerenciadorRetangulos {
    private List<Retangulo> retangulos = new ArrayList<>();
    public GerenciadorRetangulos() {
        this.retangulos = new ArrayList<>();
    }
    public void addRetangulo(Retangulo retangulo){
        this.retangulos.add(retangulo);
    }
    public Retangulo maiorArea(){
        if (retangulos.isEmpty()) {
            return null;
            
        }
        return retangulos.stream()
            .max((r1, r2) -> Double.compare(r1.maiorArea(), r2.maiorArea()))
            .orElse(null);
    }
    public Retangulo maiorPerimetro(){
        if(retangulos.isEmpty()){
            return null;
        }
        return retangulos.stream()
            .max((r1, r2) -> Double.compare(r1.maiorPerimetro(), r2.maiorPerimetro()))
            .orElse(null);

    }
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        GerenciadorRetangulos gerenciar = new GerenciadorRetangulos();
        for(int i=0;i<5;i++){
            System.out.println("\nCriando retangulo " + (i+1) + ": ");
            Retangulo retangulo=new Retangulo();
            gerenciar.addRetangulo(retangulo);
            
        }
        System.out.println("\nRetangulo com maior area: " + gerenciar.maiorArea().toString());
        System.out.println("\nRetangulo com maior perimetro: " + gerenciar.maiorPerimetro().toString());
        sc.close();
    }
    

}

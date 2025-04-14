package at1;
import java.util.Scanner;

public class Retangulo {
    
    private double altura;
    private double largura;

    public Retangulo(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite a altura do retangulo: ");
        this.altura = sc.nextDouble();
        System.out.println("Digite a largura do retangulo: ");
        this.largura = sc.nextDouble();
    }
    public Retangulo(double altura, double largura){
         this.altura = altura;
            this.largura = largura;
        
    }
    public double getAltura() {
        return altura;
    }
    public void setAltura(double altura) {
        this.altura = altura;
    }
    public double getLargura() {
        return largura;
    }
    public void setLargura(double largura) {
        this.largura = largura;
    }
    
    public double area(){
        return this.altura * this.largura;
    }
    public double perimetro(){
        return 2 * (this.altura + this.largura);
    }
    public String toString(){
        return "Altura: "+ this.altura + " Largura: " + this.largura + " Area: " + this.area() + " Perimetro: " + this.perimetro();
    }  
    public Double maiorArea(){
        return this.area();
    }
    
    public Double maiorPerimetro(){
        return this.perimetro();
    }
    

public static void main(String[] args) {
    Retangulo[] retangulos = new Retangulo[5];
    for(int i=0;i<5;i++){
        retangulos[i]=new Retangulo();
        System.out.println("Retangulo " + (i+1) + ": " + retangulos[i].toString());

    }

}


}

// Faça uma classe chamada Retangulo, com os atributos altura e largura.
// Faça um método para descobrir a área e outro para descobrir o perímetro.
// Crie 5 objetos de Retangulo.
// Crie uma lista em outra classe para armazenar todos os objetos de Retangulo
// Faça um método que encontre o Retangulo com a maior área e outro que encontre o Retangulo
// com o maior perímetro
// Imprima o toString desses Retangulos
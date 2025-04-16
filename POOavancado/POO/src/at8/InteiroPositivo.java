package at8;

public class InteiroPositivo {
    private int x;
    public InteiroPositivo(int x) {
        setValor(x);
    }
    public void setValor(int x) {
        if (x > 0) {
            this.x = x;
        }
    }
    public int getValor() {
        return x;
    }
    public int fatorial() {
        int fatorial = 1;
        for (int i = x; i > 0; i--) {
            fatorial *= i;
        }
        return fatorial;
    }
    public int multiplicar(InteiroPositivo outro) {
        return this.x * outro.getValor();
    }
    public String divisores() {
        StringBuilder divisores = new StringBuilder();
        int cont = 0;
        for (int i = 1; i <= x; i++) {
            if (x % i == 0) {
                divisores.append(i).append(" ");
                cont++;
            }
        }
        return "Divisores: " + divisores.toString() + "\nQuantidade de divisores: " + cont;
    }
    public int fibonacci() {
        int a = 1;
        int b = 1;
        for (int i = 3; i <= x; i++) {
            int c = a + b;
            a = b;
            b = c;
        }
        return b;
    }
    @Override
    public String toString() {
        return "InteiroPositivo{" +
                "x=" + x +
                ", fatorial=" + fatorial() +
                ", divisores=" + divisores() +
                ", fibonacci=" + fibonacci() +
                '}';
    }
    public static void main(String[] args) {
        InteiroPositivo n1 = new InteiroPositivo(12);
        InteiroPositivo n2 = new InteiroPositivo(5);
        System.out.println("Valor: " + n1.getValor());
        System.out.println("Valor: " + n2.getValor());
        System.out.println("Multiplicação: " + n1.multiplicar(n2));
        System.out.println("Fatorial de: " + n1.getValor()+ ": " + n1.fatorial());
        System.out.println("Divisores de : " + n1.getValor() + ": " + n1.divisores());
        System.out.println("Fibonacci de: " + n2.getValor() + ": " + n2.fibonacci());
    }
}
// Exercícios Avançados de POO
// Para todos os exercícios dessa lista:
// Fazer método construtor, atributos privados, getters e setters, método toString, tratamento de
// exceção e Array List quando necessário.

// 8)
// Um matemático está necessitando de várias funções relacionadas a um número inteiro
// positivo.
// Suponha a definição de uma classe Inteiro Positivo que apresenta o seguinte atributo: um
// número X.
// Implemente os seguintes métodos:
// a) um método setValor, que realiza a consistência necessária para garantir que X seja um inteiro
// positivo
// b) um método para retornar o número X multiplicado por outro objeto de InteiroPositivo
// c) um método para calcular o fatorial de X
// Fatorial (X) = X * (X-1) * (X-2) * (X-3) * … * 2 * 1
// d) um método para identificar os divisores inteiros de X e a quantidade de divisores.Exemplo:
// para o número 12, os divisores são 1, 2, 3, 4, 6, 12 e a quantidade de divisores é 6
// e) um método para calcular a série de Fibonacci formada por X elemento
// Fibonacci = 1, 1, 2, 3, 5, 8, 13, …
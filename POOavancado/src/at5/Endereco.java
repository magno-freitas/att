package src.at5;




public class Endereco {

    private String logradouro;
    private String complemento;
    private int numero;

    public Endereco(String logradouro, String complemento, int numero){
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.numero = numero;


    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    @Override
    public String toString() {
        return "Endereco{" +
                "logradouro='" + logradouro + '\'' +
                ", complemento='" + complemento + '\'' +
                ", numero=" + numero +
                '}';
    }
}
// Exercícios Avançados de POO
// Para todos os exercícios dessa lista:
// Fazer método construtor, atributos privados, getters e setters, método toString, tratamento de
// exceção e Array List quando necessário.

// 6)
// O objetivo deste exercício é implementar a relação entre uma pessoa e seu endereço.
// Classe Pessoa:
// Crie os atributos de nome e Endereco, em que Endereco é uma classe
// Classe Endereco:
// Crie três atributos privados: um do tipo String para o logradouro, outro do tipo String para o
// complemento e um do tipo int para o número
// Crie objetos de Pessoa
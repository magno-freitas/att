package src.at5;

import java.time.LocalDate;

public class Pessoa {
    private String nome;
    private Endereco endereco;

    public Pessoa(String nome, Endereco endereco){
        this.nome = nome;
        this.endereco = endereco;

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "nome='" + nome + '\'' +
                ", endereco=" + endereco +
                '}';
    }

    public LocalDate getDataNascimento() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDataNascimento'");
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
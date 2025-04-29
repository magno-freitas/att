package src.at9;

import src.at9.Familia;
import src.at9.Pessoa;

public class Main {
    public static void main(String[] args) {
        Familia familia=new Familia();
        familia.adicionarPessoas(new Pessoa("Magno Valadares", 30));
        familia.adicionarPessoas(new Pessoa("Carlos", 25));
        familia.adicionarPessoas(new Pessoa("John Snow", 40));
        System.out.println(familia.pessoaMaisVelha().getNome());
        System.out.println(familia.pessoaMaisVelha().getDataNascimento());

    }
    }


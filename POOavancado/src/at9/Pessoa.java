package src.at9;

import java.time.LocalDate;
import src.at9.Pessoa;

public class Pessoa {
        private String nome;
        private LocalDate dataNascimento;

        public Pessoa(String nome, int idade) {
            this.nome = nome;
            // Assuming dataNascimento is calculated based on idade
            this.dataNascimento = LocalDate.now().minusYears(idade);
        }

        public LocalDate getDataNascimento(){
            return dataNascimento;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public void setDataNascimento(LocalDate dataNascimento) {
            this.dataNascimento = dataNascimento;
        }
        
}

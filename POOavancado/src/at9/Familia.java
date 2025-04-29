package src.at9;
import java.util.ArrayList;
import java.util.List;

import src.at5.Pessoa;

import java.time.LocalDate;
public class Familia {
        private List<Pessoa> listaPessoas;

        public Familia(){
            this.listaPessoas=new ArrayList<>();
        }
        public Pessoa adicionarPessoas(Pessoa pessoa){
            listaPessoas.add(pessoa);
            return null;
        }

}

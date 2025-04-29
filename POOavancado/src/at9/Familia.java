package src.at9;
import java.util.ArrayList;
import java.util.List;

import src.at9.Pessoa;

import java.time.LocalDate;
public class Familia {
        private List<Pessoa> listaPessoas;

        public Familia(){
            this.listaPessoas=new ArrayList<>();
        }
        public void adicionarPessoas(Pessoa pessoa){
            listaPessoas.add(pessoa);
        }
        public Pessoa pessoaMaisVelha(){
            if(listaPessoas.isEmpty()){
                return null;
            }
            Pessoa maisVelha=listaPessoas.get(0);
            for(Pessoa p : listaPessoas){
                if(p.getDataNascimento().isBefore(maisVelha.getDataNascimento())){
                    maisVelha=p;
                }
            }
            return maisVelha;
        }

        public List<Pessoa> getListaPessoas() {
            return listaPessoas;
        }
        public void setListaPessoas(List<Pessoa> listaPessoas) {
            this.listaPessoas = listaPessoas;
        }
        

}

package src.at11;

import java.util.ArrayList;
import java.util.List;

public class Continente {
    private List<Pais> listaPaises;

    public Continente(){
        listaPaises=new ArrayList<>();
    }
    public void adicionarPaises(Pais pais){
        if(!listaPaises.contains(pais)){
            listaPaises.add(pais);
        
        }
    }
    public Pais maiorPais(){
        if(listaPaises.isEmpty()) return null;
        Pais maior=listaPaises.get(0);
        for(Pais pais:listaPaises){
            if(pais.getDimensao()>maior.getDimensao()){
                maior=pais;
            }
        }
        return maior;
    }
    public List<Pais> paises(){
        return listaPaises;
    }


}

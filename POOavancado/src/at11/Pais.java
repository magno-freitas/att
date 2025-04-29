package src.at11;

import java.util.ArrayList;
import java.util.List;

public class Pais { 
    private String nome;
    private double dimensao;
    private List<Pais> paisesDeFronteira;

    public Pais(String nome,double dimensao){
        this.nome=nome;
        this.dimensao=dimensao;
        this.paisesDeFronteira = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getDimensao() {
        return dimensao;
    }

 

    public List<Pais> getPaisesDeFronteira() {
        return paisesDeFronteira;
    }

    public void adicionarPaisesDeFronteira(Pais pais) {
        if(!paisesDeFronteira.contains(pais)&&!pais.equals(this)){
            paisesDeFronteira.add(pais);
        }
    }
    public boolean fazFronteira(Pais pais){
        return paisesDeFronteira.contains(pais);
    }
    public List<Pais> fronteiraIguais(Pais outro){
        List<Pais> iguais=new ArrayList<>();
        for (Pais pais : paisesDeFronteira) {
            if (pais.fazFronteira(outro)) {
                iguais.add(pais);
            }
        }
        return iguais;
    }
    
}

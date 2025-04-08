package at2;
import java.util.ArrayList;
import java.util.List;
public class flor {
    private String nome;
    private double preco;
    private String cliente;
    private boolean presente;
    public flor(String nome, double preco, String cliente, boolean presente) {
        this.nome = nome;
        this.preco = preco;
        this.cliente = cliente;
        this.presente = presente;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public double getPreco() {
        return preco;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }
    public String getCliente() {
        return cliente;
    }
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
    public boolean isPresente() {
        return presente;
    }
    public void setPresente(boolean presente) {
        this.presente = presente;
    }
    @Override
    public String toString() {
        return "Flor{" +
                "nome='" + nome + '\'' +
                ", preco=" + preco +
                ", cliente='" + cliente + '\'' +
                ", presente=" + presente +
                '}';
    }
  
    public static flor florMaisCara(List<flor> flores) {
      if(flores==null||flores.isEmpty()){
          return null;
      }
        return flores.stream()
                .max((f1, f2) -> Double.compare(f1.getPreco(), f2.getPreco()))
                .get();
    }
    
    public double getPrecoTotal() {
        if (isPresente()) {
            return getPreco() * 1.2;
        } else {
            return getPreco();
        }
    }

    public static void main(String[] args) {
        try {
            List<flor> flores = new ArrayList<>();
            flores.add(new flor("Rosa", 10.0, "João", true));
            flores.add(new flor("Margarida", 5.0, "Maria", false));
            flores.add(new flor("Girassol", 8.0, "Pedro", true));
            flores.add(new flor("Tulipa", 7.0, "Ana", false));
            flores.add(new flor("Lírio", 12.0, "Carlos", true));
        
        double receitaPresente = flores.stream()
                .filter(flor::isPresente)
                .mapToDouble(f -> f.getPrecoTotal())
                .sum();
        double receitaNormal = flores.stream()
                .filter(f -> !f.isPresente())
                .mapToDouble(flor::getPrecoTotal)
                .sum();
        
            for (flor f : flores) {
                if (f.isPresente()) {
                    receitaPresente += f.getPrecoTotal();
                } else {
                    receitaNormal += f.getPrecoTotal();
                }
            }
        flor maisCara = florMaisCara(flores);
        System.out.println("A flor mais cara é: " + maisCara.getNome() + " com o preço de: " + maisCara.getPreco());
        System.out.println("Receita para presentes: " + receitaPresente);
        System.out.println("Receita normal: " + receitaNormal);
        System.out.println("Receita total: " + (receitaPresente + receitaNormal));
       
        
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }}  // Exercícios Avançados de POO
// Para todos os exercícios dessa lista:
// Fazer método construtor, atributos privados, getters e setters, método toString, tratamento de
// exceção e Array List quando necessário.

// 2)
// Em um sistema de uma floricultura deve se guardar o nome da flor, o preço, o nome do cliente
// que comprou a flor e um boolean que determina se a flor é para presente ou não
// Descubra:
// a) Qual a flor mais cara?
// b) Se a loja teve uma receita maior vendendo flores para presente ou não

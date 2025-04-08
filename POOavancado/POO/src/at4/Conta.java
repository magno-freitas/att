package at4;
import java.util.ArrayList;
import java.util.List;

public class Conta {
    private String nome;
    private String descricao;
    private double preco;
    private double volume;
    private double peso;
    private int quantidadeEstoque;

    private List<Conta> lista=new ArrayList<>();

    public Conta(String nome, String descricao, double preco, double volume, double peso, int quantidadeEstoque) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.volume = volume;
        this.peso = peso;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public List<Conta> getLista() {
        return lista;
    }

    public void setLista(List<Conta> lista) {
        this.lista = lista;
    }
    public void adicionarProduto(Conta produto) {
        lista.add(produto);
    }
    public void venderProduto(String nome, int quantidade){
        for(Conta produto: lista){
            if( produto.getNome().equals(nome)){

                if(produto.getQuantidadeEstoque() >= quantidade) {
                    produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
                    System.out.println("Produto vendido: " + produto.getNome() + ", quantidade: " + quantidade);
                } else {
                    System.out.println("Quantidade em estoque insuficiente.");
                }
            }
        }
    }
    public void adiconarEstoque(String nome, int quantidade){
        for(Conta conta: lista){
            if(conta.getNome().equals(nome)){
                conta.setQuantidadeEstoque(conta.getQuantidadeEstoque()+ quantidade);
            }
        }
    }
    public void Promocao(String nome, double porcentagem){
        for(Conta conta : lista){
            if(conta.getNome().equals(nome)){
                conta.setPreco(conta.getPreco()-(conta.getPreco()*porcentagem/100));
                System.out.println("Produto em promocao: " + conta.getNome() + ", novo preco " + conta.getPreco());
            }
        }
    }
    public Conta caro(){
        Conta caro = lista.get(0);
        for(Conta conta: lista){
            if(conta.getPreco()>caro.getPreco()){
                caro = conta;
            }
        }
        return caro;    
    }
    public Conta caroMetro(){
        Conta caro = lista.get(0);
        for(Conta conta:lista){
            if((conta.getPreco()/conta.getVolume())>caro.getPreco()/caro.getVolume()){
                caro=conta;
            }
        }
        return caro;
    }
    public Conta denso(){
        Conta denso=lista.get(0);
        for(Conta conta:lista){
            if(conta.getPreco()/conta.getVolume()>denso.getPreco()/denso.getVolume()){
                denso=conta;
            }
            
        }
        return denso;
    }
    public Conta ocupaEspaco(){
        Conta ocupa=lista.get(0);
        for(Conta conta:lista){
            if(conta.getVolume()>ocupa.getVolume()){
                ocupa=conta;
            }
        }
        return ocupa;
    }
    @Override
    public String toString(){
        return "Conta(" + "nome=" + nome + ", descricao=" + descricao + ", preco=" + preco + ", volume=" + volume + 
        ", peso=" + peso + ", quantidadeEstoque=" + quantidadeEstoque + ")";

    }
    public static void main(String[] args) {
        Conta conta1 = new Conta("Cimento", "Cimento Portland", 30.0, 0.5, 1.0, 100);
        Conta conta2 = new Conta("Areia", "Areia fina", 20.0, 0.3, 0.5, 200);
        Conta conta3 = new Conta("Tijolo", "Tijolo cerâmico", 50.0, 0.4, 2.0, 150);

        conta1.adicionarProduto(conta1);
        conta1.adicionarProduto(conta2);
        conta1.adicionarProduto(conta3);

        System.out.println("Produto mais caro: " + conta1.caro());
        System.out.println("Produto mais caro por metro cúbico: " + conta1.caroMetro());
        System.out.println("Produto mais denso: " + conta1.denso());
        System.out.println("Produto que mais ocupa espaço no estoque: " + conta1.ocupaEspaco());
        conta1.adiconarEstoque("Cimento", 50);
    }
} // Exercícios Avançados de POO
// Para todos os exercícios dessa lista:
// Fazer método construtor, atributos privados, getters e setters, método toString, tratamento de
// exceção e Array List quando necessário.

// 4)
// Crie um sistema para uma loja de materiais de construção
// Os objetos terão os atributos:
// Nome, descrição, preço, volume (tamanho), peso e quantidade em estoque
// Crie métodos para:
// Adicionar mais unidades da peça ao estoque
// Vender um produto, podendo ser vendido até o mesmo número de peças que tem no estoque
// Colocar o produto em promoção, o parâmetro do método deve ser a porcentagem do desconto
// Descubra:
// a) Qual o material mais caro
// b) Qual o material mais caro por metro cúbico
// c) Qual o material mais denso
// d) Qual o material que mais ocupa espaço no estoque
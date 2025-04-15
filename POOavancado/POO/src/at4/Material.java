public class Material {
    private String nome;
    private String descricao;
    private double preco;
    private double volume;
    private double peso;
    private int quantidadeEstoque;

    public Material(String nome, String descricao, double preco, double volume, double peso, int quantidadeEstoque) {
        validarDados(nome, preco, volume, peso, quantidadeEstoque);
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.volume = volume;
        this.peso = peso;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    private void validarDados(String nome, double preco, double volume, double peso, int quantidadeEstoque) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        if (preco <= 0) throw new IllegalArgumentException("Preço deve ser maior que zero");
        if (volume <= 0) throw new IllegalArgumentException("Volume deve ser maior que zero");
        if (peso <= 0) throw new IllegalArgumentException("Peso deve ser maior que zero");
        if (quantidadeEstoque < 0) throw new IllegalArgumentException("Quantidade em estoque não pode ser negativa");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
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
        if (preco <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        this.preco = preco;
    }

    public double getVolume() {
        return volume;
    }

    public double getPeso() {
        return peso;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        if (quantidadeEstoque < 0) {
            throw new IllegalArgumentException("Quantidade em estoque não pode ser negativa");
        }
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public double getPrecoMetroCubico() {
        return preco / volume;
    }

    public double getDensidade() {
        return peso / volume;
    }

    public double getVolumeTotal() {
        return volume * quantidadeEstoque;
    }

    @Override
    public String toString() {
        return String.format("Material{nome='%s', descrição='%s', preço=%.2f, volume=%.2f, peso=%.2f, estoque=%d}",
                nome, descricao, preco, volume, peso, quantidadeEstoque);
    }
}
package src.at3;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LojaConstrucao {
    private List<Material> materiais = new ArrayList<>();

    public void adicionarMaterial(Material material) {
        if (material == null) throw new IllegalArgumentException("Material não pode ser nulo");
        materiais.add(material);
    }

    public void venderProduto(String nome, int quantidade) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        
        Material material = buscarPorNome(nome);
        if (material == null) throw new IllegalArgumentException("Produto não encontrado");
        if (material.getQuantidadeEstoque() < quantidade) {
            throw new IllegalStateException("Quantidade insuficiente em estoque");
        }
        
        material.setQuantidadeEstoque(material.getQuantidadeEstoque() - quantidade);
    }

    public void adicionarEstoque(String nome, int quantidade) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        
        Material material = buscarPorNome(nome);
        if (material == null) throw new IllegalArgumentException("Material não encontrado");
        material.setQuantidadeEstoque(material.getQuantidadeEstoque() + quantidade);
    }

    public void aplicarPromocao(String nome, double percentualDesconto) {
        if (percentualDesconto <= 0 || percentualDesconto >= 100) {
            throw new IllegalArgumentException("Percentual de desconto deve estar entre 0 e 100");
        }
        
        Material material = buscarPorNome(nome);
        if (material == null) throw new IllegalArgumentException("Material não encontrado");
        
        double novoPreco = material.getPreco() * (1 - percentualDesconto/100);
        material.setPreco(novoPreco);
    }

    public Material getMaterialMaisCaro() {
        return materiais.stream()
                .max(Comparator.comparing(Material::getPreco))
                .orElse(null);
    }

    public Material getMaterialMaisCaroPorMetroCubico() {
        return materiais.stream()
                .max(Comparator.comparing(Material::getPrecoMetroCubico))
                .orElse(null);
    }

    public Material getMaterialMaisDenso() {
        return materiais.stream()
                .max(Comparator.comparing(Material::getDensidade))
                .orElse(null);
    }

    public Material getMaterialMaiorVolume() {
        return materiais.stream()
                .max(Comparator.comparing(Material::getVolumeTotal))
                .orElse(null);
    }

    private Material buscarPorNome(String nome) {
        return materiais.stream()
                .filter(m -> m.getNome().equals(nome))
                .findFirst()
                .orElse(null);
    }

    public List<Material> getMateriais() {
        return new ArrayList<>(materiais);
    }
}
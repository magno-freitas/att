package src.at3;

public class Main {
    public static void main(String[] args) {
        try {
            LojaConstrucao loja = new LojaConstrucao();

            // Criando materiais com volumes diferentes para melhor demonstração
            Material tijolo = new Material("Tijolo", "Tijolo cerâmico", 50.0, 0.4, 2.0, 150);
            Material cimento = new Material("Cimento", "Cimento Portland", 30.0, 0.5, 1.0, 100);
            Material areia = new Material("Areia", "Areia fina", 20.0, 0.8, 0.5, 200);

            loja.adicionarMaterial(tijolo);
            loja.adicionarMaterial(cimento);
            loja.adicionarMaterial(areia);

            System.out.println("\nAnálise inicial dos materiais:");
            System.out.println("Material mais caro: " + loja.getMaterialMaisCaro());
            System.out.println("Material mais caro por m³: " + loja.getMaterialMaisCaroPorMetroCubico());
            System.out.println("Material mais denso: " + loja.getMaterialMaisDenso());
            System.out.println("Material que ocupa mais espaço: " + loja.getMaterialMaiorVolume());

            System.out.println("\nTestando operações:");
            System.out.println("Vendendo 10 tijolos...");
            loja.venderProduto("Tijolo", 10);
            
            System.out.println("Adicionando 50 sacos de cimento...");
            loja.adicionarEstoque("Cimento", 50);
            
            System.out.println("Aplicando 10% de desconto na areia...");
            loja.aplicarPromocao("Areia", 10);

            System.out.println("\nAnálise após operações:");
            System.out.println("Material mais caro: " + loja.getMaterialMaisCaro());
            System.out.println("Material que ocupa mais espaço: " + loja.getMaterialMaiorVolume());

        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Erro de operação: " + e.getMessage());
        }
    }
}
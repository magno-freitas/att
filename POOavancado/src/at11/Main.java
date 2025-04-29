package src.at11;

public class Main {
    public static void main(String[] args) {
        Pais brasil = new Pais("Brasil", 8515767);
        Pais argentina = new Pais("Argentina", 2780400);
        Pais uruguai = new Pais("Uruguai", 176215);

        brasil.adicionarPaisesDeFronteira(argentina);
        brasil.adicionarPaisesDeFronteira(uruguai);
        argentina.adicionarPaisesDeFronteira(brasil);
        argentina.adicionarPaisesDeFronteira(uruguai);
        uruguai.adicionarPaisesDeFronteira(brasil);
        uruguai.adicionarPaisesDeFronteira(argentina);

        Continente americaSul = new Continente();
        americaSul.adicionarPaises(brasil);
        americaSul.adicionarPaises(argentina);
        americaSul.adicionarPaises(uruguai);

        System.out.println("Maior país: " + americaSul.maiorPais().getNome());
        System.out.println("Brasil faz fronteira com Argentina? " + brasil.fazFronteira(argentina));
        System.out.println("Fronteiras iguais entre Brasil e Argentina: ");
        for (Pais p : brasil.fronteiraIguais(argentina)) {
            System.out.println(p.getNome());
        }
    }
}
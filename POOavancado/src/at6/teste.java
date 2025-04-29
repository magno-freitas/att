package src.at6;
class TestaVereador {
 
    public static void main(String[] args) {
        Vereador vereador1 = new Vereador("Fulano", "PDT", 10, 5);
        Vereador vereador2 = new Vereador("Ciclano", "PSDB", 20, 15);
        Vereador vereador3 = new Vereador("Beltrano", "PT", 0, 0);
        Vereador vereador4 = new Vereador("NovoVereador", "NOVO", 30, 25);

        Vereador[] vereadores = {vereador1, vereador2, vereador3, vereador4};

        for (Vereador vereador : vereadores) {
            System.out.println(vereador.toString());
        }
    
    }}
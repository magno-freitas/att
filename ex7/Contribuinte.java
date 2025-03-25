public class Contribuinte {
    private String nome;
    private String cpf;
    private String uf;
    private double rendaAnual;
    private double impostoAPagar;

    public Contribuinte(String nome, String cpf, String uf, double rendaAnual) {
        this.nome = nome;
        this.cpf = cpf;
        this.uf = uf;
        this.rendaAnual = rendaAnual;
        this.calcularImposto();
    }

    public void setRendaAnual(double rendaAnual) {
        if(rendaAnual <= 0) {
            throw new IllegalArgumentException("Renda anual Invalida");
        }
        this.rendaAnual = rendaAnual;
    }

    private void calcularImposto() {
        double aliquota;
        
        if (rendaAnual <= 4000) {
            aliquota = 0;
        } else if (rendaAnual <= 9000) {
            aliquota = 0.058;
        } else if (rendaAnual <= 25000) {
            aliquota = 0.15;
        } else   if (rendaAnual <= 35000) {
            aliquota = 0.275;
        } else {
            aliquota = 0.30;
        }
        
        this.impostoAPagar = rendaAnual * aliquota;
    }

    public String getNome() {
        return nome;
    }

    public double getImpostoAPagar() {
        return impostoAPagar;
    }

    @Override
    public String toString() {
        return String.format("Nome: %s, CPF: %s, UF: %s, Renda Anual: R$%.2f, Imposto: R$%.2f",
                nome, cpf, uf, rendaAnual, impostoAPagar);
    }

    
}

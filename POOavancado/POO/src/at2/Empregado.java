// Exercícios Avançados de POO
// Para todos os exercícios dessa lista:
// Fazer método construtor, atributos privados, getters e setters, método toString, tratamento de
// exceção e Array List quando necessário.

// 2)
// Em um sistema de uma floricultura deve se guardar o nome da flor, o preço, o nome do cliente
// que comprou a flor e um boolean que determina se a flor é para presente ou não
// Descubra:
// a) Qual a flor mais cara?
// b) Se a loja teve uma receita maior vendendo flores para presente ou não
// 3)
// Crie uma classe Empregado que terá como atributos:
// Identificação
// Nome
// Sobrenome
// Salário (mensal)
// Crie métodos para:
// Saber o salário anual do empregado
// Saber o nome completo do empregado
// Modificar o salário, o parâmetro do método deve ser o percentual de aumento
package at2;

public class Empregado {
    private String identificacao;
    private String nome;
    private String sobrenome;
    private double salario;

    public Empregado(String identificacao, String nome, String sobrenome, double salario) {
        this.identificacao = identificacao;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.salario = salario;
    }
    public double getSalarioAnual(){
        return this.salario * 12;
    }
    public String getNomeCompleto(){
        return this.nome + " " + this.sobrenome;
    }
    public void setModSalario(double percent){
        this.salario += this.salario*(percent/100);
    }
    public String getIdentificacao() {
        return identificacao;
    }
    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getSobrenome() {
        return sobrenome;
    }
    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }
    public double getSalario() {
        return salario;
    }
    public void setSalario(double salario) {
        this.salario = salario;
    }
    @Override
    public String toString() {
        return "Empregado(" + "Identificacao: " + identificacao + ", Nome: " + nome + ", Sobrenome: " + sobrenome + ", Salario: " + salario + ")";
    
    }
    public static void main(String[] args) {
        Empregado empregado = new Empregado("123", "Joao", "Silva", 1000);
        System.out.println(empregado.toString());
        empregado.setModSalario(10);
        System.out.println(empregado.toString());
        System.out.println("Salário Anual: " + empregado.getSalarioAnual());

        Empregado novoEmpregado = new Empregado("456", "Maria", "Oliveira", 1500);
        System.out.println(novoEmpregado.toString());
        System.out.println("Salário Anual: " + novoEmpregado.getSalarioAnual());
        novoEmpregado.setModSalario(5);
        System.out.println(novoEmpregado.toString());
        System.out.println("Salário Anual: " + novoEmpregado.getSalarioAnual());

        empregado.setModSalario(10);
        System.out.println(empregado.toString());
        System.out.println("Salário Anual: " + empregado.getSalarioAnual());
        System.out.println("Nome Completo: " + empregado.getNomeCompleto());
        System.out.println("Identificação: " + empregado.getIdentificacao());
        System.out.println("Nome Completo: " + novoEmpregado.getNomeCompleto());
        System.out.println("Identificação: " + novoEmpregado.getIdentificacao());
        System.out.println("Salário Anual: " + empregado.getSalarioAnual());
        System.out.println("Salário Anual: " + novoEmpregado.getSalarioAnual());
    }
}

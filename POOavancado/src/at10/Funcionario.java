package src.at10;

public class Funcionario {
        private String nome;
        private String matricula;
        private int departamento;
        private double salario;

        public Funcionario(String nome, String matricula, int departamento, double salario){
            this.nome=nome;
            this.matricula=matricula;
            this.departamento=departamento;
            this.salario=salario;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getMatricula() {
            return matricula;
        }

        public void setMatricula(String matricula) {
            this.matricula = matricula;
        }

        public int getDepartamento() {
            return departamento;
        }

        public void setDepartamento(int departamento) {
            this.departamento = departamento;
        }

        public double getSalario() {
            return salario;
        }

        public void setSalario(double salario) {
            this.salario = salario;
        }
        
}

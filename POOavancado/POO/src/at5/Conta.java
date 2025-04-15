public class Conta {
    private String titular;
    private double saldo;

    public Conta(String titular, double saldo) {
        if (titular == null || titular.trim().isEmpty()) {
            throw new IllegalArgumentException("Titular não pode ser nulo ou vazio");
        }
        if (saldo < 0) {
            throw new IllegalArgumentException("Saldo inicial não pode ser negativo");
        }
        this.titular = titular;
        this.saldo = saldo;
    }
    
    public void sacar(double quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (quantidade > saldo) {
            throw new IllegalStateException("Saldo insuficiente para saque");
        }
        saldo -= quantidade;
    }
    
    public void depositar(double quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        saldo += quantidade;
    }
    
    public void transferir(Conta contaDestino, double quantidade) {
        if (contaDestino == null) {
            throw new IllegalArgumentException("Conta destino não pode ser nula");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (quantidade > saldo) {
            throw new IllegalStateException("Saldo insuficiente para transferência");
        }
        this.sacar(quantidade);
        contaDestino.depositar(quantidade);
    }
    
    public String getTitular() {
        return titular;
    }
    
    public double getSaldo() {
        return saldo;
    }
    
    public void setTitular(String titular) {
        if (titular == null || titular.trim().isEmpty()) {
            throw new IllegalArgumentException("Titular não pode ser nulo ou vazio");
        }
        this.titular = titular;
    }
    
    @Override
    public String toString() {
        return String.format("Conta{titular='%s', saldo=%.2f}", titular, saldo);
    }
}
public class Computador {
    private String nSerie;
    private String modeloProcessador;
    private double clockProcessador;
    private boolean overclock;
   
    private double armazenamento;
    private double memoria; 
    private double consumoEnergia;
    private double potenciaFonte;
    
    public Computador(String nSerie, String modeloProcessador, double clockProcessador, double armazenamento, double memoria, 
    double consumoEnergia, double potenciaFonte) {
this.nSerie = nSerie;
this.modeloProcessador = modeloProcessador;
this.clockProcessador = clockProcessador;
// Define overclock baseado no modelo do processador
this.overclock = modeloProcessador.endsWith("K") || 
       modeloProcessador.endsWith("KF") || 
       modeloProcessador.endsWith("X");
this.armazenamento = armazenamento;
this.memoria = memoria;
this.consumoEnergia = consumoEnergia;
this.potenciaFonte = potenciaFonte;
}

    public String getnSerie() {
        return nSerie;
    }

    public void setnSerie(String nSerie) {
        this.nSerie = nSerie;
    }

    public String getModeloProcessador() {
        return modeloProcessador;
    }

    public void setModeloProcessador(String modeloProcessador) {
        this.modeloProcessador = modeloProcessador;
    }

    public double getClockProcessador() {
        return clockProcessador;
    }

    public void setClockProcessador(double clockProcessador) {
        this.clockProcessador = clockProcessador;
    }

    public boolean isOverclock() {
        return overclock;
    }

    public void setOverclock(boolean overclock) {
        this.overclock = overclock;
    }

    public double getArmazenamento() {
        return armazenamento;
    }

    public void setArmazenamento(double armazenamento) {
        this.armazenamento = armazenamento;
    }

    public double getMemoria() {
        return memoria;
    }

    public void setMemoria(double memoria) {
        this.memoria = memoria;
    }

    public double getConsumoEnergia() {
        return consumoEnergia;
    }

    public void setConsumoEnergia(double consumoEnergia) {
        this.consumoEnergia = consumoEnergia;
    }

    public double getPotenciaFonte() {
        return potenciaFonte;
    }

    public void setPotenciaFonte(double potenciaFonte) {
        this.potenciaFonte = potenciaFonte;
    }
    public String fazerOverclock(int incrementoMHz) {
        if (!overclock) return "Este processador não permite overclock.";
        double incrementoGHz = incrementoMHz / 1000.0;
        double maxClock = clockProcessador * 1.10;
        if (clockProcessador + incrementoGHz > maxClock) return "Limite de 10% do clock original excedido.";
        double novoConsumo = consumoEnergia + ((incrementoMHz / 100.0) * 75);
        if (novoConsumo > potenciaFonte * 0.95) return "Consumo excede 95% da potência da fonte.";
        clockProcessador += incrementoGHz;
        consumoEnergia = novoConsumo;
        return "Overclock realizado com sucesso!";
    }
    @Override
    public String toString() {
        return "Computador{" +
                "nSerie='" + nSerie + '\'' +
                ", modeloProcessador='" + modeloProcessador + '\'' +
                ", clockProcessador=" + clockProcessador +
                ", overclock=" + overclock +
                ", armazenamento=" + armazenamento +
                ", memoria=" + memoria +
                ", consumoEnergia=" + consumoEnergia +
                ", potenciaFonte=" + potenciaFonte +
                '}';
    }

}
// Exercícios Avançados de POO
// Para todos os exercícios dessa lista:
// Fazer método construtor, atributos privados, getters e setters, método toString, tratamento de
// exceção e Array List quando necessário.


// 9)
// Implemente um programa para ler o cadastro de um laboratório de informática.
// No laboratório existem N computadores.
// Cada computador possui as seguintes informações:
// - número de série
// - modelo do processador
// - clock do processador em GHz
// - um boolean informando se pode ser feito overclock no processador
// - armazenamento
// - quantidade de memória
// - consumo de energia
// - potência da fonte
// O boolean referente ao overclock não deve ser passado como parâmetro do construtor, mas
// atribuido true no construtor caso o modelo do processador termine em 'K' ou 'KF' ou 'X' e false
// caso não termine.
// Faça:
// a) Um método que faça overclock no processador.
// O método deve ter como parâmetro o incremento em MHz que será feito de overclock.
// Os processadores só aceitam overclock até 10% do clock original.
// A cada 100 MHz que o clock sobe, o consumo do computador aumenta em 75W.
// O consumo do computador nunca deve ultrapassar 95% da potência da fonte.
// Note que o processador somente aceitará overclock caso a variável booleana de overclock for
// true.
// Caso não seja possível fazer o overclock, por qualquer uma das limitações, informe por que
// não foi possível.
// b) Um método que informe o armazenamento total desse laboratório
// c) O laboratório está pensando em implementar mais 3 salas iguais a essa no prédio.
// Eles querem saber antes qual o impacto do total de computadores nos gastos de energia
// elétrica.
// Informe qual a potência mínima em Watts que o circuito elétrico deve ter para acomodar todas
// as salas.
// A potência mínima é calculada a partir do consumo total e adicionado 10% por questões de
// segurança.
// d) Assumindo que os computadores vão funcionar por uma média de 12 horas por dia, informe
// também o gasto de energia elétrica que a empresa terá com essas 3 salas.
// Considere o preço da energia elétrica 71 centavos por KW/h.
// Considere esses computadores.
// Computador 1:
//  - Número de série: "SN001"
//  - Modelo do processador: "Intel Core i7-13700KF"
//  - CPU Clock: 5.4 GHz
//  - Permite overclock: True
//  - Armazenamento: 512 GB
//  - Memória RAM: 16 GB
//  - Consumo de energia: 500W
//  - Potência da fonte: 850W
// Computador 2:
//  - Número de série: "SN002"
//  - Modelo do processador: "AMD Ryzen 9 5950X"
//  - CPU Clock: 4.9 GHz
//  - Permite overclock: True
//  - Armazenamento: 1 TB
//  - Memória RAM: 32 GB
//  - Consumo de energia: 500W
//  - Potência da fonte: 1000W
// Computador 3:
//  - Número de série: "SN003"
//  - Modelo do processador: "Intel Core i5-9400F"
//  - CPU Clock: 4.1 GHz
//  - Permite overclock: False
//  - Armazenamento: 256 GB
//  - Memória RAM: 8 GB
//  - Consumo de energia: 400W
//  - Potência da fonte: 600W
// Computador 4:
//  - Número de série: "SN004"
//  - Modelo do processador: "AMD Ryzen 7 3700X"
//  - CPU Clock: 4.4 GHz
//  - Permite overclock: True
//  - Armazenamento: 512 GB
//  - Memória RAM: 16 GB
//  - Consumo de energia: 450W
//  - Potência da fonte: 550W
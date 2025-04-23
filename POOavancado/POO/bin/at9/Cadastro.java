package POO.bin.at9;

import java.util.ArrayList;
public class Cadastro { // Declaração da classe Cadastro
    private ArrayList<Computador> computadores = new ArrayList<>(); // Lista para armazenar objetos Computador
    private int salas = 4;
    private int horasPorDia = 12; // Horas de uso por dia (variável não declarada)
    private double precoKWh = 0.71; // Preço do kWh (variável não declarada)
    public void adicionarComputador(Computador computador) { // Método para adicionar um computador à lista
        computadores.add(computador); // Adiciona o computador à lista
    }
    public int armazenamentoTotal(){ // Método para calcular o armazenamento total
        int total = 0; // Inicializa o total em 0
        for (Computador computador : computadores) { // Percorre todos os computadores da lista
            total += computador.getArmazenamento(); // Soma o armazenamento de cada computador
        }
        return total; // Retorna o total calculado
    }
    public int memoriaRamTotal(){ // Método para calcular a memória RAM total
        int total = 0; // Inicializa o total em 0
        for (Computador computador : computadores) { // Percorre todos os computadores da lista
            total += computador.getMemoria(); // Soma a memória RAM de cada computador
        }
        return total; // Retorna o total calculado
    }
    public int consumoTotal(){ // Método para calcular o consumo total de energia
        int total = 0; // Inicializa o total em 0
        for (Computador computador : computadores) { // Percorre todos os computadores da lista
            total += computador.getConsumoEnergia(); // Soma o consumo de energia de cada computador
        }
        return total; // Retorna o total calculado
    }
    public double potenciaMinimaCircuitoEletrico(){ // Método para calcular a potência mínima do circuito elétrico
        double total=consumoTotal()*salas; // Soma o consumo total com o número de salas (variável não declarada)
        return total*1.1; // Adiciona 10% de segurança e retorna o valor
    }
    public double gastoEnergiaMensal() {
        double consumoTotalKW = (consumoTotal() * salas) / 1000.0; // Converte W para kW
        double gastoDia = consumoTotalKW * horasPorDia * precoKWh; // Calcula gasto diário correto
        return gastoDia * 30; // Retorna o gasto mensal (30 dias)
    }
    public ArrayList<Computador> getComputadores() { // Getter para a lista de computadores
        return computadores; // Retorna a lista de computadores
    }

  
}

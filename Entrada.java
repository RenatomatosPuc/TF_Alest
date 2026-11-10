package aeroporto;

import java.util.ArrayList;
import java.util.List;

// Passo 1: recebe aeronaves e mantém a fila de entrada limitada.
public class Entrada {
    private final Fila fila = new Fila();
    private final List<String> identificacoesRecebidas = new ArrayList<>();
    private final int capacidade;
    private int totalChegadas;
    private int totalAceitas;
    private int totalRecusadas;

    public Entrada(int capacidade) {
        if (capacidade <= 0) {
            throw new IllegalArgumentException("A capacidade da fila deve ser positiva.");
        }
        this.capacidade = capacidade;
    }

    // Registra a chegada e retorna true quando a aeronave entra na fila.
    public boolean receber(Aeronave aeronave) {
        if (aeronave.getEstado() != Aeronave.EstadoAeronave.FILA_ENTRADA) {
            throw new IllegalArgumentException("Apenas aeronaves recém-chegadas podem entrar na fila.");
        }
        for (String identificacao : identificacoesRecebidas) {
            if (identificacao.equals(aeronave.getIdentificacao())) {
                throw new IllegalArgumentException("Identificação de aeronave duplicada.");
            }
        }
        identificacoesRecebidas.add(aeronave.getIdentificacao());
        totalChegadas++;
        if (fila.tamanho() >= capacidade) {
            aeronave.recusar();
            totalRecusadas++;
            return false;
        }
        fila.enfileirar(aeronave);
        totalAceitas++;
        return true;
    }

    Aeronave retirarProxima() {
        return fila.desenfileirar();
    }

    boolean estaVazia() {
        return fila.estaVazia();
    }

    public int getTamanhoFila() {
        return fila.tamanho();
    }

    public int getCapacidade() {
        return capacidade;
    }

    public int getTotalChegadas() {
        return totalChegadas;
    }

    public int getTotalAceitas() {
        return totalAceitas;
    }

    public int getTotalRecusadas() {
        return totalRecusadas;
    }
}

package TF_Alest;

// Etapa 1: recebe as aeronaves e mantém a fila de entrada, que tem tamanho máximo.
public class Entrada {
    private final Fila fila = new Fila();
    private final int capacidade;
    private int totalAceitas;
    private int totalRecusadas;

    public Entrada(int capacidade) {
        this.capacidade = capacidade;
    }

    // Se a fila estiver cheia, a aeronave é recusada. Senão, entra na fila.
    public void receber(Aeronave aeronave) {
        if (fila.tamanho() >= capacidade) {
            aeronave.recusar();
            totalRecusadas++;
        } else {
            fila.enfileirar(aeronave);
            totalAceitas++;
        }
    }

    public Aeronave retirarProxima() {
        return fila.desenfileirar();
    }

    public boolean estaVazia() {
        return fila.estaVazia();
    }

    public int getTamanhoFila() {
        return fila.tamanho();
    }

    public int getTotalChegadas() {
        return totalAceitas + totalRecusadas;
    }

    public int getTotalAceitas() {
        return totalAceitas;
    }

    public int getTotalRecusadas() {
        return totalRecusadas;
    }
}

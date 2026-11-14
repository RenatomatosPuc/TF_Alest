package TF_Alest;

// ocupa um controlador ou uma pista.
public class Atendimento {
    private final Aeronave aeronave;
    private int tempoRestante;

    // Cria um novo atendimento para a aeronave com a duração especificada.
    Atendimento(Aeronave aeronave, int duracao) {
        this.aeronave = aeronave;
        this.tempoRestante = duracao;
    }

    public Aeronave getAeronave() {
        return aeronave;
    }

    public boolean terminou() {
        return tempoRestante == 0;
    }

    public void avancarUmaRodada() {
        tempoRestante--;
    }
}

package TF_Alest;

// Representa uma aeronave ocupando um controlador ou uma pista
// durante um certo número de rodadas.
public class Atendimento {
    private final Aeronave aeronave;
    private int tempoRestante;

    public Atendimento(Aeronave aeronave, int duracao) {
        this.aeronave = aeronave;
        this.tempoRestante = duracao;
    }

    public Aeronave getAeronave() {
        return this.aeronave;
    }

    public boolean terminou() {
        return this.tempoRestante == 0;
    }

    public void avancarUmaRodada() {
        this.tempoRestante--;
    }
}

package aeroporto;

// ocupa um controlador ou uma pista.
class Atendimento {
    private final Aeronave aeronave;
    private int tempoRestante;

    // Cria um novo atendimento para a aeronave com a duração especificada.
    Atendimento(Aeronave aeronave, int duracao) {
        this.aeronave = aeronave;
        this.tempoRestante = duracao;
    }

    Aeronave getAeronave() {
        return aeronave;
    }

    boolean terminou() {
        return tempoRestante == 0;
    }

    void avancarUmaRodada() {
        tempoRestante--;
    }
}

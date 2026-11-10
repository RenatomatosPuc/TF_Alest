package aeroporto;

// Medidas do sistema durante uma rodada da simulação.
public class RegistroRodada {
    private final int rodada;
    private final int filaEntrada;
    private final int filaAlta;
    private final int filaMedia;
    private final int filaBaixa;
    private final int controladoresOcupados;
    private final int pistasOcupadas;

    RegistroRodada(int rodada, int filaEntrada, int filaAlta, int filaMedia,
            int filaBaixa, int controladoresOcupados, int pistasOcupadas) {
        this.rodada = rodada;
        this.filaEntrada = filaEntrada;
        this.filaAlta = filaAlta;
        this.filaMedia = filaMedia;
        this.filaBaixa = filaBaixa;
        this.controladoresOcupados = controladoresOcupados;
        this.pistasOcupadas = pistasOcupadas;
    }

    public int getRodada() {
        return rodada;
    }

    public int getFilaEntrada() {
        return filaEntrada;
    }

    public int getFilaAlta() {
        return filaAlta;
    }

    public int getFilaMedia() {
        return filaMedia;
    }

    public int getFilaBaixa() {
        return filaBaixa;
    }

    public int getControladoresOcupados() {
        return controladoresOcupados;
    }

    public int getPistasOcupadas() {
        return pistasOcupadas;
    }
}

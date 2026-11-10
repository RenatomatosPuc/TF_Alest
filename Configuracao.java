package aeroporto;

// Valores tipados e validados.
public class Configuracao {
    private final int rodadas;
    private final double probabilidadeChegada;
    private final int capacidadeFilaEntrada;
    private final int quantidadeControladores;
    private final int duracaoClassificacao;
    private final int quantidadePistas;
    private final int duracaoPistaAlta;
    private final int duracaoPistaMedia;
    private final int duracaoPistaBaixa;
    private final double probabilidadePrioridadeAlta;
    private final double probabilidadePrioridadeMedia;
    private final double probabilidadePrioridadeBaixa;
    private final long semente;

    Configuracao(
            int rodadas,
            double probabilidadeChegada,
            int capacidadeFilaEntrada,
            int quantidadeControladores,
            int duracaoClassificacao,
            int quantidadePistas,
            int duracaoPistaAlta,
            int duracaoPistaMedia,
            int duracaoPistaBaixa,
            double probabilidadePrioridadeAlta,
            double probabilidadePrioridadeMedia,
            double probabilidadePrioridadeBaixa,
            long semente) {
        this.rodadas = rodadas;
        this.probabilidadeChegada = probabilidadeChegada;
        this.capacidadeFilaEntrada = capacidadeFilaEntrada;
        this.quantidadeControladores = quantidadeControladores;
        this.duracaoClassificacao = duracaoClassificacao;
        this.quantidadePistas = quantidadePistas;
        this.duracaoPistaAlta = duracaoPistaAlta;
        this.duracaoPistaMedia = duracaoPistaMedia;
        this.duracaoPistaBaixa = duracaoPistaBaixa;
        this.probabilidadePrioridadeAlta = probabilidadePrioridadeAlta;
        this.probabilidadePrioridadeMedia = probabilidadePrioridadeMedia;
        this.probabilidadePrioridadeBaixa = probabilidadePrioridadeBaixa;
        this.semente = semente;
    }

    public int getRodadas() {
        return rodadas;
    }

    public double getProbabilidadeChegada() {
        return probabilidadeChegada;
    }

    public int getCapacidadeFilaEntrada() {
        return capacidadeFilaEntrada;
    }

    public int getQuantidadeControladores() {
        return quantidadeControladores;
    }

    public int getDuracaoClassificacao() {
        return duracaoClassificacao;
    }

    public int getQuantidadePistas() {
        return quantidadePistas;
    }

    public int getDuracaoPistaAlta() {
        return duracaoPistaAlta;
    }

    public int getDuracaoPistaMedia() {
        return duracaoPistaMedia;
    }

    public int getDuracaoPistaBaixa() {
        return duracaoPistaBaixa;
    }

    public double getProbabilidadePrioridadeAlta() {
        return probabilidadePrioridadeAlta;
    }

    public double getProbabilidadePrioridadeMedia() {
        return probabilidadePrioridadeMedia;
    }

    public double getProbabilidadePrioridadeBaixa() {
        return probabilidadePrioridadeBaixa;
    }

    public long getSemente() {
        return semente;
    }
}

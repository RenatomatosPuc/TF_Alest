package TF_Alest;

public class Aeronave {
    // Em que etapa a aeronave está.
    public enum EstadoAeronave {
        FILA_ENTRADA,
        CLASSIFICANDO,
        FILA_PRIORIDADE,
        EM_PROCESSAMENTO,
        CONCLUIDA,
        RECUSADA
    }

    private final String identificacao;
    private final int rodadaChegada;
    private EstadoAeronave estado;
    private Prioridade prioridade;

    // -1 significa "ainda não aconteceu"
    private int inicioClassificacao = -1;
    private int fimClassificacao = -1;
    private int inicioProcessamento = -1;
    private int fimProcessamento = -1;

    public Aeronave(String identificacao, int rodadaChegada) {
        this.identificacao = identificacao;
        this.rodadaChegada = rodadaChegada;
        this.estado = EstadoAeronave.FILA_ENTRADA;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public int getRodadaChegada() {
        return rodadaChegada;
    }

    public EstadoAeronave getEstado() {
        return this.estado;
    }

    public Prioridade getPrioridade() {
        return this.prioridade;
    }

    public int getInicioClassificacao() {
        return this.inicioClassificacao;
    }

    public int getFimClassificacao() {
        return this.fimClassificacao;
    }

    public int getInicioProcessamento() {
        return this.inicioProcessamento;
    }

    public int getFimProcessamento() {
        return this.fimProcessamento;
    }

    // Cada método abaixo registra o tempo e muda o estado da aeronave.

    public void recusar() {
        this.estado = EstadoAeronave.RECUSADA;
    }

    public void iniciarClassificacao(int tempo) {
        this.inicioClassificacao = tempo;
        this.estado = EstadoAeronave.CLASSIFICANDO;
    }

    public void concluirClassificacao(int tempo, Prioridade prioridade) {
        this.fimClassificacao = tempo;
        this.prioridade = prioridade;
        this.estado = EstadoAeronave.FILA_PRIORIDADE;
    }

    public void iniciarProcessamento(int tempo) {
        this.inicioProcessamento = tempo;
        this.estado = EstadoAeronave.EM_PROCESSAMENTO;
    }

    public void concluirProcessamento(int tempo) {
        this.fimProcessamento = tempo;
        this.estado = EstadoAeronave.CONCLUIDA;
    }

    // Espera = tempo na fila de entrada + tempo na fila de prioridade.
    // Retorna -1 se a aeronave ainda não chegou à pista.
    public int getTempoEsperaTotal() {
        if (this.inicioProcessamento < 0) {
            return -1;
        }
        int esperaEntrada = inicioClassificacao - rodadaChegada;
        int esperaPrioridade = inicioProcessamento - fimClassificacao;
        return esperaEntrada + esperaPrioridade;
    }

    // Tempo desde a chegada até terminar a pista. Retorna -1 se não concluiu.
    public int getTempoNoSistema() {
        if (this.estado != EstadoAeronave.CONCLUIDA) {
            return -1;
        }
        return this.fimProcessamento - rodadaChegada;
    }
}

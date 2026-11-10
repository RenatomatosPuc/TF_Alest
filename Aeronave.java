package aeroporto;

public class Aeronave {
    // Situação atual da aeronave na simulação.
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
    private int inicioClassificacao = -1;
    private int fimClassificacao = -1;
    private int inicioProcessamento = -1;
    private int fimProcessamento = -1;

    // Cria uma nova aeronave com a identificação e a rodada de chegada
    // especificadas.
    public Aeronave(String identificacao, int rodadaChegada) {
        String id = identificacao.trim();
        if (id.isEmpty()) {
            throw new IllegalArgumentException("A identificação da aeronave não pode ser vazia.");
        }
        if (rodadaChegada < 0) {
            throw new IllegalArgumentException("A rodada de chegada não pode ser negativa.");
        }
        this.identificacao = id;
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
        return estado;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public int getInicioClassificacao() {
        return inicioClassificacao;
    }

    public int getFimClassificacao() {
        return fimClassificacao;
    }

    public int getInicioProcessamento() {
        return inicioProcessamento;
    }

    public int getFimProcessamento() {
        return fimProcessamento;
    }

    void recusar() {
        exigirEstado(EstadoAeronave.FILA_ENTRADA);
        estado = EstadoAeronave.RECUSADA;
    }

    void iniciarClassificacao(int tempo) {
        exigirEstado(EstadoAeronave.FILA_ENTRADA);
        validarTempo(tempo);
        if (tempo < rodadaChegada) {
            throw new IllegalArgumentException("A classificação não pode começar antes da chegada.");
        }
        inicioClassificacao = tempo;
        estado = EstadoAeronave.CLASSIFICANDO;
    }

    void concluirClassificacao(int tempo, Prioridade prioridade) {
        exigirEstado(EstadoAeronave.CLASSIFICANDO);
        validarTempo(tempo);
        if (tempo <= inicioClassificacao) {
            throw new IllegalArgumentException("A classificação deve consumir ao menos uma unidade de tempo.");
        }
        if (prioridade == null) {
            throw new IllegalArgumentException("A prioridade não pode ser vazia.");
        }
        this.prioridade = prioridade;
        fimClassificacao = tempo;
        estado = EstadoAeronave.FILA_PRIORIDADE;
    }

    void iniciarProcessamento(int tempo) {
        exigirEstado(EstadoAeronave.FILA_PRIORIDADE);
        validarTempo(tempo);
        if (tempo < fimClassificacao) {
            throw new IllegalArgumentException("O processamento não pode começar antes da classificação terminar.");
        }
        inicioProcessamento = tempo;
        estado = EstadoAeronave.EM_PROCESSAMENTO;
    }

    void concluirProcessamento(int tempo) {
        exigirEstado(EstadoAeronave.EM_PROCESSAMENTO);
        validarTempo(tempo);
        if (tempo <= inicioProcessamento) {
            throw new IllegalArgumentException("O processamento deve consumir ao menos uma unidade de tempo.");
        }
        fimProcessamento = tempo;
        estado = EstadoAeronave.CONCLUIDA;
    }

    // Soma os intervalos de espera. retorna -1 se ainda não começou.
    public int getTempoEsperaTotal() {
        if (inicioProcessamento < 0) {
            return -1;
        }
        return (inicioClassificacao - rodadaChegada) + (inicioProcessamento - fimClassificacao);
    }

    // Retorna o tempo no sistema para aeronaves concluídas. caso contrário retorna
    // -1.
    public int getTempoNoSistema() {
        if (estado != EstadoAeronave.CONCLUIDA) {
            return -1;
        }
        return fimProcessamento - rodadaChegada;
    }

    // Verifica se o estado da aeronave é o esperado.
    private void exigirEstado(EstadoAeronave esperado) {
        if (estado != esperado) {
            throw new IllegalStateException("Transição inválida para " + identificacao + ": estado atual " + estado);
        }
    }

    private static void validarTempo(int tempo) {
        if (tempo < 0) {
            throw new IllegalArgumentException("O tempo não pode ser negativo.");
        }
    }
}

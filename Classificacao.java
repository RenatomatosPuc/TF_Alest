package aeroporto;

import java.util.Random;

// Segunda etapa: analisa aeronaves e as encaminha às filas de prioridade.
public class Classificacao {
    private final Entrada entrada;
    private final Configuracao configuracao;
    private final Random aleatorio;
    private final Fila filaAlta = new Fila();
    private final Fila filaMedia = new Fila();
    private final Fila filaBaixa = new Fila();
    private final Atendimento[] controladores;

    public Classificacao(Entrada entrada, Configuracao configuracao, Random aleatorio) {
        this.entrada = entrada;
        this.configuracao = configuracao;
        this.aleatorio = aleatorio;
        this.controladores = new Atendimento[configuracao.getQuantidadeControladores()];
    }

    // Finaliza classificações que consumiram sua duração e coloca os aviões na
    // fila.
    void concluirAtendimentos(int tempo) {
        for (int i = 0; i < controladores.length; i++) {
            Atendimento atendimento = controladores[i];
            if (atendimento != null && atendimento.terminou()) {
                Aeronave aeronave = atendimento.getAeronave();
                Prioridade prioridade = sortearPrioridade();
                aeronave.concluirClassificacao(tempo, prioridade);
                filaDaPrioridade(prioridade).enfileirar(aeronave);
                controladores[i] = null;
            }
        }
    }

    // Ocupa controladores livres com as próximas aeronaves da fila de entrada.
    void iniciarAtendimentos(int tempo) {
        for (int i = 0; i < controladores.length && !entrada.estaVazia(); i++) {
            if (controladores[i] == null) {
                Aeronave aeronave = entrada.retirarProxima();
                aeronave.iniciarClassificacao(tempo);
                controladores[i] = new Atendimento(
                        aeronave, configuracao.getDuracaoClassificacao());
            }
        }
    }

    void avancarUmaRodada() {
        for (Atendimento atendimento : controladores) {
            if (atendimento != null) {
                atendimento.avancarUmaRodada();
            }
        }
    }

    Aeronave retirarProximaPrioridade() {
        if (!filaAlta.estaVazia()) {
            return filaAlta.desenfileirar();
        }
        if (!filaMedia.estaVazia()) {
            return filaMedia.desenfileirar();
        }
        if (!filaBaixa.estaVazia()) {
            return filaBaixa.desenfileirar();
        }
        return null;
    }

    public int getTamanhoFila(Prioridade prioridade) {
        return filaDaPrioridade(prioridade).tamanho();
    }

    public int getControladoresOcupados() {
        int ocupados = 0;
        for (Atendimento atendimento : controladores) {
            if (atendimento != null) {
                ocupados++;
            }
        }
        return ocupados;
    }

    private Prioridade sortearPrioridade() {
        double sorteio = aleatorio.nextDouble();
        double limiteAlta = configuracao.getProbabilidadePrioridadeAlta();
        double limiteMedia = limiteAlta + configuracao.getProbabilidadePrioridadeMedia();
        if (sorteio < limiteAlta) {
            return Prioridade.ALTA;
        }
        if (sorteio < limiteMedia) {
            return Prioridade.MEDIA;
        }
        return Prioridade.BAIXA;
    }

    private Fila filaDaPrioridade(Prioridade prioridade) {
        switch (prioridade) {
            case ALTA:
                return filaAlta;
            case MEDIA:
                return filaMedia;
            case BAIXA:
                return filaBaixa;
            default:
                throw new IllegalArgumentException("Prioridade não reconhecida: " + prioridade);
        }
    }
}

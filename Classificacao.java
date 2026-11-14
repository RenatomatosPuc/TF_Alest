package TF_Alest;

import java.util.Random;

// Etapa 2: os controladores analisam cada aeronave, sorteiam a prioridade
// e colocam a aeronave na fila da prioridade correspondente.
public class Classificacao {
    private final Entrada entrada;
    private final Configuracao config;
    private final Random aleatorio;
    private final Fila filaAlta = new Fila();
    private final Fila filaMedia = new Fila();
    private final Fila filaBaixa = new Fila();

    // Um controlador por posição. null = controlador livre.
    private final Atendimento[] controladores;

    public Classificacao(Entrada entrada, Configuracao config, Random aleatorio) {
        this.entrada = entrada;
        this.config = config;
        this.aleatorio = aleatorio;
        this.controladores = new Atendimento[config.quantidadeControladores];
    }

    // Quem terminou de analisar recebe uma prioridade, vai para a fila e libera o controlador.
    public void concluirAtendimentos(int tempo) {
        for (int i = 0; i < controladores.length; i++) {
            if (controladores[i] != null && controladores[i].terminou()) {
                Aeronave aeronave = controladores[i].getAeronave();
                Prioridade prioridade = sortearPrioridade();
                aeronave.concluirClassificacao(tempo, prioridade);
                filaDaPrioridade(prioridade).enfileirar(aeronave);
                controladores[i] = null;
            }
        }
    }

    // Controladores livres pegam a próxima aeronave da fila de entrada.
    public void iniciarAtendimentos(int tempo) {
        for (int i = 0; i < controladores.length; i++) {
            if (controladores[i] == null && !entrada.estaVazia()) {
                Aeronave aeronave = entrada.retirarProxima();
                aeronave.iniciarClassificacao(tempo);
                controladores[i] = new Atendimento(aeronave, config.duracaoClassificacao);
            }
        }
    }

    public void avancarUmaRodada() {
        for (int i = 0; i < controladores.length; i++) {
            if (controladores[i] != null) {
                controladores[i].avancarUmaRodada();
            }
        }
    }

    // Devolve a próxima aeronave a ser atendida na pista: primeiro as de prioridade ALTA,
    // depois MEDIA, depois BAIXA. Retorna null se as três filas estiverem vazias.
    public Aeronave retirarProximaPrioridade() {
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
        for (int i = 0; i < controladores.length; i++) {
            if (controladores[i] != null) {
                ocupados++;
            }
        }
        return ocupados;
    }

    // Sorteia um número entre 0 e 1 e vê em qual faixa ele cai.
    // Ex.: 0.05 / 0.20 / 0.75 -> [0, 0.05) ALTA, [0.05, 0.25) MEDIA, resto BAIXA.
    private Prioridade sortearPrioridade() {
        double sorteio = aleatorio.nextDouble();
        double limiteAlta = config.probabilidadePrioridadeAlta;
        double limiteMedia = limiteAlta + config.probabilidadePrioridadeMedia;
        if (sorteio < limiteAlta) {
            return Prioridade.ALTA;
        }
        if (sorteio < limiteMedia) {
            return Prioridade.MEDIA;
        }
        return Prioridade.BAIXA;
    }

    private Fila filaDaPrioridade(Prioridade prioridade) {
        if (prioridade == Prioridade.ALTA) {
            return filaAlta;
        }
        if (prioridade == Prioridade.MEDIA) {
            return filaMedia;
        }
        return filaBaixa;
    }
}

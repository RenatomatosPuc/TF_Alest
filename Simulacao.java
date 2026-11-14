package TF_Alest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Controla a simulação: gera chegadas e faz as três etapas funcionarem rodada a rodada.
public class Simulacao {
    private final Configuracao config;
    private final Random aleatorio;
    private final Entrada entrada;
    private final Classificacao classificacao;
    private final Processamento processamento;
    private final List<Aeronave> aeronaves = new ArrayList<>();
    private final List<RegistroRodada> registros = new ArrayList<>();

    public Simulacao(Configuracao config) {
        this.config = config;
        this.aleatorio = new Random(config.semente);
        this.entrada = new Entrada(config.capacidadeFilaEntrada);
        this.classificacao = new Classificacao(entrada, config, aleatorio);
        this.processamento = new Processamento(classificacao, config);
    }

    public ResultadoSimulacao executar() {
        for (int rodada = 1; rodada <= config.rodadas; rodada++) {
            int tempo = rodada - 1;

            // 1) Pode chegar uma aeronave nova
            gerarChegada(tempo);

            // 2) Quem terminou a etapa sai dela
            classificacao.concluirAtendimentos(tempo);
            processamento.concluirAtendimentos(tempo);

            // 3) Quem estava esperando entra na etapa seguinte
            processamento.iniciarAtendimentos(tempo);
            classificacao.iniciarAtendimentos(tempo);

            // 4) Anota como o sistema ficou nesta rodada
            registros.add(new RegistroRodada(
                    rodada,
                    entrada.getTamanhoFila(),
                    classificacao.getTamanhoFila(Prioridade.ALTA),
                    classificacao.getTamanhoFila(Prioridade.MEDIA),
                    classificacao.getTamanhoFila(Prioridade.BAIXA),
                    classificacao.getControladoresOcupados(),
                    processamento.getPistasOcupadas()));

            // 5) Passa o tempo
            classificacao.avancarUmaRodada();
            processamento.avancarUmaRodada();
        }

        // Fecha o que terminou exatamente no fim da última rodada.
        classificacao.concluirAtendimentos(config.rodadas);
        processamento.concluirAtendimentos(config.rodadas);

        return new ResultadoSimulacao(config, entrada, aeronaves, registros);
    }

    // Em cada rodada, chega uma aeronave com a probabilidade definida na configuração.
    private void gerarChegada(int tempo) {
        if (aleatorio.nextDouble() < config.probabilidadeChegada) {
            String identificacao = String.format("VOO-%03d", aeronaves.size() + 1);
            Aeronave aeronave = new Aeronave(identificacao, tempo);
            aeronaves.add(aeronave);
            entrada.receber(aeronave);
        }
    }
}

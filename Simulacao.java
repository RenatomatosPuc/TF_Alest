package aeroporto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Coordena a geração de chegadas e as três etapas do sistema.
public class Simulacao {
    private final Configuracao configuracao;
    private final Random aleatorio;
    private final Entrada entrada;
    private final Classificacao classificacao;
    private final Processamento processamento;
    private final List<Aeronave> aeronaves = new ArrayList<>();
    private final List<RegistroRodada> registros = new ArrayList<>();
    public Simulacao(Configuracao configuracao) {
        this.configuracao = configuracao;
        this.aleatorio = new Random(configuracao.getSemente());
        this.entrada = new Entrada(configuracao.getCapacidadeFilaEntrada());
        this.classificacao = new Classificacao(entrada, configuracao, aleatorio);
        this.processamento = new Processamento(classificacao, configuracao);
    }

    // Executa todas as rodadas e devolve os dados coletados.
    public ResultadoSimulacao executar() {
        for (int rodada = 1; rodada <= configuracao.getRodadas(); rodada++) {
            int tempoAtual = rodada - 1;
            gerarChegada(tempoAtual);

            classificacao.concluirAtendimentos(tempoAtual);
            processamento.concluirAtendimentos(tempoAtual);

            processamento.iniciarAtendimentos(tempoAtual);
            classificacao.iniciarAtendimentos(tempoAtual);

            registros.add(new RegistroRodada(
                    rodada,
                    entrada.getTamanhoFila(),
                    classificacao.getTamanhoFila(Prioridade.ALTA),
                    classificacao.getTamanhoFila(Prioridade.MEDIA),
                    classificacao.getTamanhoFila(Prioridade.BAIXA),
                    classificacao.getControladoresOcupados(),
                    processamento.getPistasOcupadas()));

            classificacao.avancarUmaRodada();
            processamento.avancarUmaRodada();

        }

        // Fecha as operações que terminaram exatamente no limite da simulação.
        int tempoFinal = configuracao.getRodadas();
        classificacao.concluirAtendimentos(tempoFinal);
        processamento.concluirAtendimentos(tempoFinal);

        return new ResultadoSimulacao(
                configuracao, entrada.getTotalChegadas(), entrada.getTotalAceitas(),
                entrada.getTotalRecusadas(), aeronaves, registros);
    }

    private void gerarChegada(int tempoAtual) {
        if (aleatorio.nextDouble() < configuracao.getProbabilidadeChegada()) {
            String identificacao = String.format("VOO-%03d", aeronaves.size() + 1);
            Aeronave aeronave = new Aeronave(identificacao, tempoAtual);
            aeronaves.add(aeronave);
            entrada.receber(aeronave);
        }
    }

}

package TF_Alest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// Guarda o que aconteceu na simulação, mostra o resumo na tela e grava os arquivos CSV.
public class ResultadoSimulacao {
    private final Configuracao config;
    private final Entrada entrada;
    private final List<Aeronave> aeronaves;
    private final List<RegistroRodada> registros;

    public ResultadoSimulacao(Configuracao config, Entrada entrada,
            List<Aeronave> aeronaves, List<RegistroRodada> registros) {
        this.config = config;
        this.entrada = entrada;
        this.aeronaves = aeronaves;
        this.registros = registros;
    }

    // ---------- Resumo na tela ----------

    public void imprimirResumo() {
        System.out.println("Semente: " + config.semente);
        System.out.println("Chegadas: " + entrada.getTotalChegadas());
        System.out.println("Aceitas: " + entrada.getTotalAceitas());
        System.out.println("Recusadas/desviadas: " + entrada.getTotalRecusadas());
        System.out.println("Operações concluídas: " + contarConcluidas());
        System.out.println("Aeronaves ainda no sistema: " + contarPendentes());
        System.out.printf("Ocupação média dos controladores: %.2f%%%n", ocupacaoControladores());
        System.out.printf("Ocupação média das pistas: %.2f%%%n", ocupacaoPistas());

        System.out.println("Métricas por prioridade (voos concluídos):");
        Prioridade[] todas = Prioridade.values();
        for (int i = 0; i < todas.length; i++) {
            imprimirResumoPrioridade(todas[i]);
        }

        imprimirPendentes();
    }

    private void imprimirResumoPrioridade(Prioridade prioridade) {
        int quantidade = 0;
        int somaEspera = 0;
        int esperaMaxima = 0;
        int somaTempoNoSistema = 0;

        for (Aeronave aeronave : aeronaves) {
            if (aeronave.getEstado() == Aeronave.EstadoAeronave.CONCLUIDA
                    && aeronave.getPrioridade() == prioridade) {
                int espera = aeronave.getTempoEsperaTotal();
                quantidade++;
                somaEspera += espera;
                somaTempoNoSistema += aeronave.getTempoNoSistema();
                if (espera > esperaMaxima) {
                    esperaMaxima = espera;
                }
            }
        }

        if (quantidade == 0) {
            System.out.println("  " + prioridade + ": nenhum voo concluído");
        } else {
            double esperaMedia = (double) somaEspera / quantidade;
            double tempoMedio = (double) somaTempoNoSistema / quantidade;
            System.out.printf("  %s: %d voos, espera média %.2f rodadas, espera máxima %d, "
                    + "tempo médio no sistema %.2f rodadas%n",
                    prioridade, quantidade, esperaMedia, esperaMaxima, tempoMedio);
        }
    }

    private void imprimirPendentes() {
        if (contarPendentes() == 0) {
            return;
        }
        System.out.println("Pendências ao fim da simulação:");
        for (Aeronave aeronave : aeronaves) {
            if (estaPendente(aeronave)) {
                String prioridade = "sem classificação";
                if (aeronave.getPrioridade() != null) {
                    prioridade = aeronave.getPrioridade().toString();
                }
                System.out.println("  " + aeronave.getIdentificacao() + " - "
                        + aeronave.getEstado() + " - " + prioridade);
            }
        }
    }

    // ---------- Contagens e médias ----------

    // Pendente = ainda está no sistema (nem concluiu, nem foi recusada).
    private boolean estaPendente(Aeronave aeronave) {
        return aeronave.getEstado() != Aeronave.EstadoAeronave.CONCLUIDA
                && aeronave.getEstado() != Aeronave.EstadoAeronave.RECUSADA;
    }

    private int contarConcluidas() {
        int total = 0;
        for (Aeronave aeronave : aeronaves) {
            if (aeronave.getEstado() == Aeronave.EstadoAeronave.CONCLUIDA) {
                total++;
            }
        }
        return total;
    }

    private int contarPendentes() {
        int total = 0;
        for (Aeronave aeronave : aeronaves) {
            if (estaPendente(aeronave)) {
                total++;
            }
        }
        return total;
    }

    // Ocupação = (soma dos ocupados em todas as rodadas) / (total possível) * 100
    private double ocupacaoControladores() {
        int soma = 0;
        for (RegistroRodada registro : registros) {
            soma += registro.controladoresOcupados;
        }
        int totalPossivel = config.quantidadeControladores * registros.size();
        return 100.0 * soma / totalPossivel;
    }

    private double ocupacaoPistas() {
        int soma = 0;
        for (RegistroRodada registro : registros) {
            soma += registro.pistasOcupadas;
        }
        int totalPossivel = config.quantidadePistas * registros.size();
        return 100.0 * soma / totalPossivel;
    }

    // ---------- Arquivos CSV ----------

    public void salvarCsv(String pasta) throws IOException {
        new File(pasta).mkdirs();
        gravar(pasta + "/rodadas.csv", montarCsvRodadas());
        gravar(pasta + "/aeronaves.csv", montarCsvAeronaves());
    }

    private void gravar(String caminho, String texto) throws IOException {
        PrintWriter escritor = new PrintWriter(new FileWriter(caminho));
        escritor.print(texto);
        escritor.close();
    }

    private String montarCsvRodadas() {
        String csv = "rodada,fila_entrada,fila_alta,fila_media,fila_baixa,"
                + "controladores_ocupados,pistas_ocupadas\n";
        for (RegistroRodada r : registros) {
            csv += r.rodada + "," + r.filaEntrada + "," + r.filaAlta + ","
                    + r.filaMedia + "," + r.filaBaixa + ","
                    + r.controladoresOcupados + "," + r.pistasOcupadas + "\n";
        }
        return csv;
    }

    private String montarCsvAeronaves() {
        String csv = "identificacao,estado,rodada_chegada,prioridade,inicio_classificacao,"
                + "fim_classificacao,inicio_processamento,fim_processamento,"
                + "espera_total,tempo_no_sistema\n";
        for (Aeronave a : aeronaves) {
            String prioridade = "";
            if (a.getPrioridade() != null) {
                prioridade = a.getPrioridade().toString();
            }
            csv += a.getIdentificacao() + "," + a.getEstado() + "," + a.getRodadaChegada() + ","
                    + prioridade + ","
                    + tempoCsv(a.getInicioClassificacao()) + ","
                    + tempoCsv(a.getFimClassificacao()) + ","
                    + tempoCsv(a.getInicioProcessamento()) + ","
                    + tempoCsv(a.getFimProcessamento()) + ","
                    + tempoCsv(a.getTempoEsperaTotal()) + ","
                    + tempoCsv(a.getTempoNoSistema()) + "\n";
        }
        return csv;
    }

    // Tempo negativo (-1) quer dizer "não aconteceu": deixa a célula vazia.
    private String tempoCsv(int tempo) {
        if (tempo < 0) {
            return "";
        }
        return Integer.toString(tempo);
    }
}

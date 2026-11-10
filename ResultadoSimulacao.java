package aeroporto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// Guarda os dados da simulação, mostra o resumo e grava os CSVs.
public class ResultadoSimulacao {
    private final Configuracao configuracao;
    private final int totalChegadas;
    private final int totalAceitas;
    private final int totalRecusadas;
    private final List<Aeronave> aeronaves;
    private final List<RegistroRodada> registros;

    ResultadoSimulacao(Configuracao configuracao, int totalChegadas, int totalAceitas,
            int totalRecusadas, List<Aeronave> aeronaves, List<RegistroRodada> registros) {
        this.configuracao = configuracao;
        this.totalChegadas = totalChegadas;
        this.totalAceitas = totalAceitas;
        this.totalRecusadas = totalRecusadas;
        this.aeronaves = aeronaves;
        this.registros = registros;
    }

    public List<Aeronave> getAeronaves() {
        return aeronaves;
    }

    public List<RegistroRodada> getRegistros() {
        return registros;
    }

    public int getTotalChegadas() {
        return totalChegadas;
    }

    public int getTotalAceitas() {
        return totalAceitas;
    }

    public int getTotalRecusadas() {
        return totalRecusadas;
    }

    public int getTotalConcluidas() {
        int total = 0;
        for (Aeronave aeronave : aeronaves) {
            if (aeronave.getEstado() == Aeronave.EstadoAeronave.CONCLUIDA) {
                total++;
            }
        }
        return total;
    }

    public int getTotalPendentes() {
        int total = 0;
        for (Aeronave aeronave : aeronaves) {
            Aeronave.EstadoAeronave estado = aeronave.getEstado();
            if (estado != Aeronave.EstadoAeronave.CONCLUIDA
                    && estado != Aeronave.EstadoAeronave.RECUSADA) {
                total++;
            }
        }
        return total;
    }

    public void imprimirResumo() {
        System.out.println("Semente: " + configuracao.getSemente());
        System.out.println("Chegadas: " + totalChegadas);
        System.out.println("Aceitas: " + totalAceitas);
        System.out.println("Recusadas/desviadas: " + totalRecusadas);
        System.out.println("Operações concluídas: " + getTotalConcluidas());
        System.out.println("Aeronaves ainda no sistema: " + getTotalPendentes());
        System.out.printf("Ocupação média dos controladores: %.2f%%%n", ocupacaoControladores());
        System.out.printf("Ocupação média das pistas: %.2f%%%n", ocupacaoPistas());
        System.out.println("Métricas por prioridade (voos concluídos):");
        for (Prioridade prioridade : Prioridade.values()) {
            imprimirResumoPrioridade(prioridade);
        }
        imprimirPendentes();
    }

    public void salvarCsv(Path diretorio) throws IOException {
        Files.createDirectories(diretorio);
        Files.writeString(diretorio.resolve("rodadas.csv"), csvRodadas(), StandardCharsets.UTF_8);
        Files.writeString(diretorio.resolve("aeronaves.csv"), csvAeronaves(), StandardCharsets.UTF_8);
    }

    private void imprimirResumoPrioridade(Prioridade prioridade) {
        int quantidade = 0;
        int somaEspera = 0;
        int esperaMaxima = 0;
        int somaTempoNoSistema = 0;

        for (Aeronave aeronave : aeronaves) {
            if (aeronave.getEstado() == Aeronave.EstadoAeronave.CONCLUIDA
                    && aeronave.getPrioridade() == prioridade) {
                quantidade++;
                somaEspera += aeronave.getTempoEsperaTotal();
                if (aeronave.getTempoEsperaTotal() > esperaMaxima) {
                    esperaMaxima = aeronave.getTempoEsperaTotal();
                }
                somaTempoNoSistema += aeronave.getTempoNoSistema();
            }
        }

        if (quantidade == 0) {
            System.out.println("  " + prioridade + ": nenhum voo concluído");
        } else {
            double esperaMedia = (double) somaEspera / quantidade;
            double tempoMedioNoSistema = (double) somaTempoNoSistema / quantidade;
            System.out.printf("  %s: %d voos, espera média %.2f rodadas, espera máxima %d, "
                    + "tempo médio no sistema %.2f rodadas%n",
                    prioridade, quantidade, esperaMedia, esperaMaxima, tempoMedioNoSistema);
        }
    }

    private void imprimirPendentes() {
        if (getTotalPendentes() == 0) {
            return;
        }

        System.out.println("Pendências ao fim da simulação:");
        for (Aeronave aeronave : aeronaves) {
            Aeronave.EstadoAeronave estado = aeronave.getEstado();
            if (estado != Aeronave.EstadoAeronave.CONCLUIDA
                    && estado != Aeronave.EstadoAeronave.RECUSADA) {
                String prioridade = "sem classificação";
                if (aeronave.getPrioridade() != null) {
                    prioridade = aeronave.getPrioridade().toString();
                }
                System.out.println("  " + aeronave.getIdentificacao() + " - " + estado + " - " + prioridade);
            }
        }
    }

    private double ocupacaoControladores() {
        int ocupados = 0;
        for (RegistroRodada registro : registros) {
            ocupados += registro.getControladoresOcupados();
        }
        int total = configuracao.getQuantidadeControladores() * registros.size();
        if (total == 0) {
            return 0.0;
        }
        return 100.0 * ocupados / total;
    }

    private double ocupacaoPistas() {
        int ocupadas = 0;
        for (RegistroRodada registro : registros) {
            ocupadas += registro.getPistasOcupadas();
        }
        int total = configuracao.getQuantidadePistas() * registros.size();
        if (total == 0) {
            return 0.0;
        }
        return 100.0 * ocupadas / total;
    }

    private String csvRodadas() {
        StringBuilder csv = new StringBuilder(
                "rodada,fila_entrada,fila_alta,fila_media,fila_baixa,controladores_ocupados,pistas_ocupadas\n");
        for (RegistroRodada registro : registros) {
            csv.append(registro.getRodada()).append(',')
                    .append(registro.getFilaEntrada()).append(',')
                    .append(registro.getFilaAlta()).append(',')
                    .append(registro.getFilaMedia()).append(',')
                    .append(registro.getFilaBaixa()).append(',')
                    .append(registro.getControladoresOcupados()).append(',')
                    .append(registro.getPistasOcupadas()).append('\n');
        }
        return csv.toString();
    }

    private String csvAeronaves() {
        StringBuilder csv = new StringBuilder(
                "identificacao,estado,rodada_chegada,prioridade,inicio_classificacao,fim_classificacao,"
                        + "inicio_processamento,fim_processamento,espera_total,tempo_no_sistema\n");
        for (Aeronave aeronave : aeronaves) {
            csv.append(aeronave.getIdentificacao()).append(',')
                    .append(aeronave.getEstado()).append(',')
                    .append(aeronave.getRodadaChegada()).append(',');
            if (aeronave.getPrioridade() != null) {
                csv.append(aeronave.getPrioridade());
            }
            csv.append(',').append(tempoCsv(aeronave.getInicioClassificacao()))
                    .append(',').append(tempoCsv(aeronave.getFimClassificacao()))
                    .append(',').append(tempoCsv(aeronave.getInicioProcessamento()))
                    .append(',').append(tempoCsv(aeronave.getFimProcessamento()))
                    .append(',').append(tempoCsv(aeronave.getTempoEsperaTotal()))
                    .append(',').append(tempoCsv(aeronave.getTempoNoSistema()))
                    .append('\n');
        }
        return csv.toString();
    }

    private String tempoCsv(int tempo) {
        if (tempo < 0) {
            return "";
        }
        return Integer.toString(tempo);
    }
}

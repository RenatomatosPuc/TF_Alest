package aeroporto;

// Passo 3: atende as aeronaves usando pistas limitadas e sem interrupção.
public class Processamento {
    private final Classificacao classificacao;
    private final Configuracao configuracao;
    private final Atendimento[] pistas;

    public Processamento(Classificacao classificacao, Configuracao configuracao) {
        this.classificacao = classificacao;
        this.configuracao = configuracao;
        this.pistas = new Atendimento[configuracao.getQuantidadePistas()];
    }

    /** Finaliza operações que já consumiram todo o tempo de pista. */
    void concluirAtendimentos(int tempo) {
        for (int i = 0; i < pistas.length; i++) {
            Atendimento atendimento = pistas[i];
            if (atendimento != null && atendimento.terminou()) {
                atendimento.getAeronave().concluirProcessamento(tempo);
                pistas[i] = null;
            }
        }
    }

    // Ocupa cada pista livre com a aeronave de maior prioridade disponível.
    void iniciarAtendimentos(int tempo) {
        for (int i = 0; i < pistas.length; i++) {
            if (pistas[i] == null) {
                Aeronave aeronave = classificacao.retirarProximaPrioridade();
                if (aeronave == null) {
                    return;
                }
                aeronave.iniciarProcessamento(tempo);
                pistas[i] = new Atendimento(aeronave, duracaoDaPista(aeronave.getPrioridade()));
            }
        }
    }

    void avancarUmaRodada() {
        for (Atendimento atendimento : pistas) {
            if (atendimento != null) {
                atendimento.avancarUmaRodada();
            }
        }
    }

    public int getPistasOcupadas() {
        int ocupadas = 0;
        for (Atendimento atendimento : pistas) {
            if (atendimento != null) {
                ocupadas++;
            }
        }
        return ocupadas;
    }

    private int duracaoDaPista(Prioridade prioridade) {
        switch (prioridade) {
            case ALTA:
                return configuracao.getDuracaoPistaAlta();
            case MEDIA:
                return configuracao.getDuracaoPistaMedia();
            case BAIXA:
                return configuracao.getDuracaoPistaBaixa();
            default:
                throw new IllegalStateException("Prioridade não reconhecida: " + prioridade);
        }
    }
}

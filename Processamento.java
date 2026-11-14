package TF_Alest;

// Etapa 3: as aeronaves usam a pista. Cada pista atende uma aeronave por vez, sem interrupção.
public class Processamento {
    private final Classificacao classificacao;
    private final Configuracao config;

    // Uma pista por posição. null = pista livre.
    private final Atendimento[] pistas;

    public Processamento(Classificacao classificacao, Configuracao config) {
        this.classificacao = classificacao;
        this.config = config;
        this.pistas = new Atendimento[config.quantidadePistas];
    }

    // Libera as pistas cujas operações terminaram.
    public void concluirAtendimentos(int tempo) {
        for (int i = 0; i < pistas.length; i++) {
            if (pistas[i] != null && pistas[i].terminou()) {
                pistas[i].getAeronave().concluirProcessamento(tempo);
                pistas[i] = null;
            }
        }
    }

    // Cada pista livre recebe a aeronave de maior prioridade que estiver esperando.
    public void iniciarAtendimentos(int tempo) {
        for (int i = 0; i < pistas.length; i++) {
            if (pistas[i] == null) {
                Aeronave aeronave = classificacao.retirarProximaPrioridade();
                if (aeronave == null) {
                    return; // ninguém esperando
                }
                aeronave.iniciarProcessamento(tempo);
                int duracao = duracaoDaPista(aeronave.getPrioridade());
                pistas[i] = new Atendimento(aeronave, duracao);
            }
        }
    }

    public void avancarUmaRodada() {
        for (int i = 0; i < pistas.length; i++) {
            if (pistas[i] != null) {
                pistas[i].avancarUmaRodada();
            }
        }
    }

    public int getPistasOcupadas() {
        int ocupadas = 0;
        for (int i = 0; i < pistas.length; i++) {
            if (pistas[i] != null) {
                ocupadas++;
            }
        }
        return ocupadas;
    }

    private int duracaoDaPista(Prioridade prioridade) {
        if (prioridade == Prioridade.ALTA) {
            return config.duracaoPistaAlta;
        }
        if (prioridade == Prioridade.MEDIA) {
            return config.duracaoPistaMedia;
        }
        return config.duracaoPistaBaixa;
    }
}

package TF_Alest;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

// Lê o arquivo config.properties e guarda os valores da simulação.
// Os atributos são "final" e públicos: depois de lidos, ninguém consegue alterá-los,
// então não precisamos de getters.
public class Configuracao {
    public final int rodadas;
    public final double probabilidadeChegada;
    public final int capacidadeFilaEntrada;
    public final int quantidadeControladores;
    public final int duracaoClassificacao;
    public final int quantidadePistas;
    public final int duracaoPistaAlta;
    public final int duracaoPistaMedia;
    public final int duracaoPistaBaixa;
    public final double probabilidadePrioridadeAlta;
    public final double probabilidadePrioridadeMedia;
    public final double probabilidadePrioridadeBaixa;
    public final long semente;

    public Configuracao(String caminhoArquivo) throws IOException {
        Properties p = new Properties();
        FileReader leitor = new FileReader(caminhoArquivo);
        p.load(leitor);
        leitor.close();

        rodadas = lerInt(p, "simulation.rounds");
        probabilidadeChegada = lerDouble(p, "arrival.probability");
        capacidadeFilaEntrada = lerInt(p, "queue.entry.capacity");
        quantidadeControladores = lerInt(p, "controller.count");
        duracaoClassificacao = lerInt(p, "controller.classification.duration");
        quantidadePistas = lerInt(p, "runway.count");
        duracaoPistaAlta = lerInt(p, "runway.duration.high");
        duracaoPistaMedia = lerInt(p, "runway.duration.medium");
        duracaoPistaBaixa = lerInt(p, "runway.duration.low");
        probabilidadePrioridadeAlta = lerDouble(p, "priority.probability.high");
        probabilidadePrioridadeMedia = lerDouble(p, "priority.probability.medium");
        probabilidadePrioridadeBaixa = lerDouble(p, "priority.probability.low");

        // "auto" significa: sortear uma semente nova a cada execução.
        String textoSemente = lerTexto(p, "random.seed");
        if (textoSemente.equals("auto")) {
            semente = new Random().nextLong();
        } else {
            semente = Long.parseLong(textoSemente);
        }

        double soma = probabilidadePrioridadeAlta + probabilidadePrioridadeMedia
                + probabilidadePrioridadeBaixa;
        if (Math.abs(soma - 1.0) > 0.001) {
            throw new IllegalArgumentException("As probabilidades das prioridades devem somar 1.");
        }
    }

    private static String lerTexto(Properties p, String chave) {
        String valor = p.getProperty(chave);
        if (valor == null) {
            throw new IllegalArgumentException("Falta a propriedade " + chave);
        }
        return valor.trim();
    }

    // Quantidades e durações precisam ser maiores que zero.
    // (Uma operação com duração 0 começaria e terminaria no mesmo instante,
    // o que o enunciado proíbe.)
    private static int lerInt(Properties p, String chave) {
        int valor = Integer.parseInt(lerTexto(p, chave));
        if (valor <= 0) {
            throw new IllegalArgumentException(chave + " deve ser maior que zero.");
        }
        return valor;
    }

    // Todas as probabilidades precisam estar entre 0 e 1.
    private static double lerDouble(Properties p, String chave) {
        double valor = Double.parseDouble(lerTexto(p, chave));
        if (valor < 0.0 || valor > 1.0) {
            throw new IllegalArgumentException(chave + " deve estar entre 0 e 1.");
        }
        return valor;
    }
}

package aeroporto;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Random;

/** Lê os parâmetros do arquivo config.properties. */
public class CarregadorConfiguracao {
    public static Configuracao carregar() throws IOException {
        return carregar(Path.of("config.properties"));
    }

    public static Configuracao carregar(Path caminho) throws IOException {
        Properties propriedades = new Properties();
        try (BufferedReader leitor = Files.newBufferedReader(caminho, StandardCharsets.UTF_8)) {
            propriedades.load(leitor);
        }

        int rodadas = inteiroPositivo(propriedades, "simulation.rounds");
        double probabilidadeChegada = probabilidade(propriedades, "arrival.probability");
        int capacidadeFila = inteiroPositivo(propriedades, "queue.entry.capacity");
        int quantidadeControladores = inteiroPositivo(propriedades, "controller.count");
        int duracaoClassificacao = inteiroPositivo(propriedades, "controller.classification.duration");
        int quantidadePistas = inteiroPositivo(propriedades, "runway.count");
        int duracaoPistaAlta = inteiroPositivo(propriedades, "runway.duration.high");
        int duracaoPistaMedia = inteiroPositivo(propriedades, "runway.duration.medium");
        int duracaoPistaBaixa = inteiroPositivo(propriedades, "runway.duration.low");
        double probabilidadeAlta = probabilidade(propriedades, "priority.probability.high");
        double probabilidadeMedia = probabilidade(propriedades, "priority.probability.medium");
        double probabilidadeBaixa = probabilidade(propriedades, "priority.probability.low");
        long semente = lerSemente(propriedades);

        if (Math.abs(probabilidadeAlta + probabilidadeMedia + probabilidadeBaixa - 1.0) > 0.001) {
            throw new IllegalArgumentException("As probabilidades das prioridades devem somar 1.");
        }

        return new Configuracao(
                rodadas, probabilidadeChegada, capacidadeFila,
                quantidadeControladores, duracaoClassificacao, quantidadePistas,
                duracaoPistaAlta, duracaoPistaMedia, duracaoPistaBaixa,
                probabilidadeAlta, probabilidadeMedia, probabilidadeBaixa, semente);
    }

    private static String valor(Properties propriedades, String chave) {
        String valor = propriedades.getProperty(chave);
        if (valor == null) {
            throw new IllegalArgumentException("Falta a propriedade " + chave + ".");
        }
        return valor.trim();
    }

    private static int inteiroPositivo(Properties propriedades, String chave) {
        int numero = Integer.parseInt(valor(propriedades, chave));
        if (numero <= 0) {
            throw new IllegalArgumentException(chave + " deve ser maior que zero.");
        }
        return numero;
    }

    private static double probabilidade(Properties propriedades, String chave) {
        double numero = Double.parseDouble(valor(propriedades, chave));
        if (Double.isNaN(numero) || numero < 0.0 || numero > 1.0) {
            throw new IllegalArgumentException(chave + " deve estar entre 0 e 1.");
        }
        return numero;
    }

    private static long lerSemente(Properties propriedades) {
        String valor = valor(propriedades, "random.seed");
        if (valor.equals("auto")) {
            return new Random().nextLong();
        }
        return Long.parseLong(valor);
    }
}

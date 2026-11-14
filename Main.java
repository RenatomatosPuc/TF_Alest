package TF_Alest;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Configuracao config = new Configuracao("config.properties");
        Simulacao simulacao = new Simulacao(config);
        ResultadoSimulacao resultado = simulacao.executar();

        resultado.imprimirResumo();
        resultado.salvarCsv("resultados");
        System.out.println("Arquivos CSV salvos na pasta resultados.");
    }
}

package aeroporto;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        Configuracao configuracao = CarregadorConfiguracao.carregar();
        ResultadoSimulacao resultado = new Simulacao(configuracao).executar();
        resultado.imprimirResumo();
        resultado.salvarCsv(Path.of("resultados"));
        System.out.println("Arquivos CSV salvos na pasta resultados.");
    }
}

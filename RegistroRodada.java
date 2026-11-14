package TF_Alest;

// "Foto" do sistema em uma rodada: tamanho das filas e quantos estão ocupados.
// Os atributos são final e públicos: depois de criado, o registro não muda.
public class RegistroRodada {
    public final int rodada;
    public final int filaEntrada;
    public final int filaAlta;
    public final int filaMedia;
    public final int filaBaixa;
    public final int controladoresOcupados;
    public final int pistasOcupadas;

    public RegistroRodada(int rodada, int filaEntrada, int filaAlta, int filaMedia,
            int filaBaixa, int controladoresOcupados, int pistasOcupadas) {
        this.rodada = rodada;
        this.filaEntrada = filaEntrada;
        this.filaAlta = filaAlta;
        this.filaMedia = filaMedia;
        this.filaBaixa = filaBaixa;
        this.controladoresOcupados = controladoresOcupados;
        this.pistasOcupadas = pistasOcupadas;
    }
}

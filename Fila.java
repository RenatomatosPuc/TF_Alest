package aeroporto;

import java.util.NoSuchElementException;

// Fila FIFO de aeronaves com nós encadeados.
public class Fila {
    private No primeiro;
    private No ultimo;
    private int tamanho;

    public void enfileirar(Aeronave aeronave) {
        No novo = new No(aeronave);
        if (estaVazia()) {
            primeiro = novo;
        } else {
            ultimo.proximo = novo;
        }
        ultimo = novo;
        tamanho++;
    }

    public Aeronave desenfileirar() {
        if (estaVazia()) {
            throw new NoSuchElementException("A fila está vazia.");
        }

        Aeronave aeronave = primeiro.aeronave;
        primeiro = primeiro.proximo;
        tamanho--;
        if (primeiro == null) {
            ultimo = null;
        }
        return aeronave;
    }

    public int tamanho() {
        return tamanho;
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    private static class No {
        private Aeronave aeronave;
        private No proximo;

        private No(Aeronave aeronave) {
            this.aeronave = aeronave;
        }
    }
}

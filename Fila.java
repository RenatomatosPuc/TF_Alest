package TF_Alest;

import java.util.NoSuchElementException;

// Fila (FIFO) de aeronaves feita com lista encadeada.
public class Fila {
    private Node primeiro;
    private Node ultimo;
    private int tamanho;

    // Coloca a aeronave no fim da fila.
    public void enfileirar(Aeronave aeronave) {
        Node novo = new No(aeronave);
        if (estaVazia()) {
            primeiro = novo;
        } else {
            ultimo.proximo = novo;
        }
        ultimo = novo;
        tamanho++;
    }

    // Tira e devolve a aeronave do começo da fila.
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

    // Cada nó guarda uma aeronave e aponta para o próximo.
    private static class Node {
        Aeronave aeronave;
        Node proximo;

        Node(Aeronave aeronave) {
            this.aeronave = aeronave;
        }
    }
}

public class Aresta {
    private final Vertice origem;
    private final Vertice destino;
    private final double peso;

    public Aresta(Vertice origem, Vertice destino, double peso) {
        this.origem = origem;
        this.destino = destino;
        this.peso = peso;
    }

    public boolean liga(Vertice origem, Vertice destino, boolean dirigido) {
        if (this.origem == origem && this.destino == destino) {
            return true;
        }

        return !dirigido && this.origem == destino && this.destino == origem;
    }

    public Vertice getOrigem() {
        return origem;
    }

    public Vertice getDestino() {
        return destino;
    }

    public double getPeso() {
        return peso;
    }
}

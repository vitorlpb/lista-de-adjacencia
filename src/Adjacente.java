public class Adjacente {
    private final Vertice vertice;
    private final double peso;

    public Adjacente(Vertice vertice, double peso) {
        this.vertice = vertice;
        this.peso = peso;
    }

    public Vertice getVertice() {
        return vertice;
    }

    public double getPeso() {
        return peso;
    }
}

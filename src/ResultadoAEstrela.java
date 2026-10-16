import java.util.List;
import java.util.Map;

public class ResultadoAEstrela {

    private final List<Aresta> caminho;
    private final double custoTotal;
    private final Map<Vertice, Double> heuristica;
    private final boolean encontrado;

    public ResultadoAEstrela(
            List<Aresta> caminho,
            double custoTotal,
            Map<Vertice, Double> heuristica,
            boolean encontrado) {
        this.caminho = caminho;
        this.custoTotal = custoTotal;
        this.heuristica = heuristica;
        this.encontrado = encontrado;
    }

    public List<Aresta> getCaminho() {
        return caminho;
    }

    public double getCustoTotal() {
        return custoTotal;
    }

    public Map<Vertice, Double> getHeuristica() {
        return heuristica;
    }

    public boolean isEncontrado() {
        return encontrado;
    }
}

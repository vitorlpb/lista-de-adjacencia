import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class AEstrela {

    private static final double RAIO_TERRA_KM = 6371.0;

    // Item da fila de prioridade: vértice e seu f(n) = g(n) + h(n) no momento da inserção.
    private record No(Vertice vertice, double f) {
    }

    public ResultadoAEstrela buscar(Grafo grafo, Vertice origem, Vertice destino) {

        // h(n): distância em linha reta (Haversine) de cada vértice até o destino
        Map<Vertice, Double> heuristica = new HashMap<>();

        for (Vertice vertice : grafo.getVertices()) {
            heuristica.put(vertice, haversine(vertice, destino));
        }

        Map<Vertice, Double> custo = new HashMap<>();          // g(n)
        Map<Vertice, Aresta> predecessor = new HashMap<>();
        Set<Vertice> fechados = new HashSet<>();
        PriorityQueue<No> abertos = new PriorityQueue<>((a, b) -> Double.compare(a.f(), b.f()));

        custo.put(origem, 0.0);
        abertos.add(new No(origem, heuristica.get(origem)));

        boolean encontrado = false;

        while (!abertos.isEmpty()) {

            Vertice atual = abertos.poll().vertice();

            // entrada antiga na fila, o vértice já foi expandido
            if (fechados.contains(atual)) {
                continue;
            }

            fechados.add(atual);

            if (atual == destino) {
                encontrado = true;
                break;
            }

            for (Adjacente adjacente : grafo.getAdjacentes(atual)) {

                Vertice vizinho = adjacente.getVertice();
                double novoCusto = custo.get(atual) + adjacente.getPeso();

                if (novoCusto < custo.getOrDefault(vizinho, Double.POSITIVE_INFINITY)) {
                    custo.put(vizinho, novoCusto);
                    predecessor.put(vizinho, new Aresta(atual, vizinho, adjacente.getPeso()));
                    // reabre o vértice caso a heurística não seja consistente
                    fechados.remove(vizinho);
                    abertos.add(new No(vizinho, novoCusto + heuristica.get(vizinho)));
                }
            }
        }

        List<Aresta> caminho = new ArrayList<>();

        if (encontrado) {
            Vertice atual = destino;

            while (atual != origem) {
                Aresta aresta = predecessor.get(atual);
                caminho.add(0, aresta);
                atual = aresta.getOrigem();
            }
        }

        double custoTotal = encontrado ? custo.get(destino) : 0;

        return new ResultadoAEstrela(caminho, custoTotal, heuristica, encontrado);
    }

    public static double haversine(Vertice a, Vertice b) {

        double lat1 = Math.toRadians(a.getLatitude());
        double lat2 = Math.toRadians(b.getLatitude());
        double deltaLat = lat2 - lat1;
        double deltaLon = Math.toRadians(b.getLongitude() - a.getLongitude());

        double termo = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        return 2 * RAIO_TERRA_KM * Math.asin(Math.sqrt(termo));
    }
}

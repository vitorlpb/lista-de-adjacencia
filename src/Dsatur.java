import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dsatur {

    // Retorna a cor (0, 1, 2, ...) de cada vértice.
    public Map<Vertice, Integer> colorir(Grafo grafo) {

        List<Vertice> vertices = grafo.getVertices();
        vertices.sort((a, b) -> a.getRotulo().compareTo(b.getRotulo()));

        // Na coloração, A -> B também torna A vizinho de B,
        // então os vizinhos são montados nos dois sentidos.
        Map<Vertice, Set<Vertice>> vizinhos = new HashMap<>();

        for (Vertice vertice : vertices) {
            vizinhos.put(vertice, new HashSet<>());
        }

        for (Vertice vertice : vertices) {
            for (Adjacente adjacente : grafo.getAdjacentes(vertice)) {

                if (adjacente.getVertice() == vertice) {
                    throw new IllegalArgumentException(
                            "Não é possível colorir: o vértice " + vertice.getRotulo()
                                    + " tem um laço.");
                }

                vizinhos.get(vertice).add(adjacente.getVertice());
                vizinhos.get(adjacente.getVertice()).add(vertice);
            }
        }

        Map<Vertice, Integer> cores = new HashMap<>();

        while (cores.size() < vertices.size()) {

            // escolhe o vértice sem cor com maior saturação; empate: maior grau
            Vertice escolhido = null;
            int maiorSaturacao = -1;
            int maiorGrau = -1;

            for (Vertice vertice : vertices) {

                if (cores.containsKey(vertice)) {
                    continue;
                }

                int saturacao = coresVizinhas(vertice, vizinhos, cores).size();
                int grau = vizinhos.get(vertice).size();

                if (saturacao > maiorSaturacao
                        || (saturacao == maiorSaturacao && grau > maiorGrau)) {
                    escolhido = vertice;
                    maiorSaturacao = saturacao;
                    maiorGrau = grau;
                }
            }

            // menor cor que nenhum vizinho usa
            Set<Integer> usadas = coresVizinhas(escolhido, vizinhos, cores);
            int cor = 0;

            while (usadas.contains(cor)) {
                cor++;
            }

            cores.put(escolhido, cor);
        }

        return cores;
    }

    private Set<Integer> coresVizinhas(
            Vertice vertice,
            Map<Vertice, Set<Vertice>> vizinhos,
            Map<Vertice, Integer> cores) {

        Set<Integer> usadas = new HashSet<>();

        for (Vertice vizinho : vizinhos.get(vertice)) {
            if (cores.containsKey(vizinho)) {
                usadas.add(cores.get(vizinho));
            }
        }

        return usadas;
    }
}

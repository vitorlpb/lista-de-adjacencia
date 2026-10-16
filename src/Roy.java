import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Roy {

    public List<List<Vertice>> calcularComponentes(Grafo grafo) {

        List<Vertice> vertices = grafo.getVertices();
        int n = vertices.size();

        Map<Vertice, Integer> indice = new HashMap<>();

        for (int i = 0; i < n; i++) {
            indice.put(vertices.get(i), i);
        }

        boolean[][] alcanca = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            alcanca[i][i] = true;

            for (Adjacente adjacente : grafo.getAdjacentes(vertices.get(i))) {
                int j = indice.get(adjacente.getVertice());
                alcanca[i][j] = true;
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {

                if (!alcanca[i][k]) {
                    continue;
                }

                for (int j = 0; j < n; j++) {
                    if (alcanca[k][j]) {
                        alcanca[i][j] = true;
                    }
                }
            }
        }

        boolean[] agrupado = new boolean[n];
        List<List<Vertice>> componentes = new ArrayList<>();

        for (int i = 0; i < n; i++) {

            if (agrupado[i]) {
                continue;
            }

            List<Vertice> componente = new ArrayList<>();

            for (int j = 0; j < n; j++) {

                boolean mesmoComponente = grafo.isDirigido()
                        ? (alcanca[i][j] && alcanca[j][i])
                        : alcanca[i][j];

                if (mesmoComponente) {
                    componente.add(vertices.get(j));
                    agrupado[j] = true;
                }
            }

            componentes.add(componente);
        }

        return componentes;
    }
}

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Prim {

    public ResultadoPrim calculaPrim(Grafo grafo, Vertice origem) {

        if (grafo.isDirigido()) {
            throw new IllegalArgumentException(
                    "Prim não se aplica, para grafos dirigidos");
        }

        Set<Vertice> naArvore = new HashSet<>();
        List<Aresta> arestasAGM = new ArrayList<>();
        double custoTotal = 0;

        naArvore.add(origem); // vai registar best vertices

        while (naArvore.size() < grafo.getVertices().size()) {

            Aresta menorAresta = null;

            // percorre a lista de adjacência dos vértices já na árvore
            for (Vertice vertice : naArvore) {

                for (Adjacente adjacente : grafo.getAdjacentes(vertice)) {

                    if (naArvore.contains(adjacente.getVertice())) {
                        continue;
                    }

                    if (menorAresta == null || adjacente.getPeso() < menorAresta.getPeso()) {
                        menorAresta = new Aresta(vertice, adjacente.getVertice(), adjacente.getPeso());
                    }
                }
            }

            if (menorAresta == null) {
                break;
            }

            naArvore.add(menorAresta.getDestino());
            arestasAGM.add(menorAresta);
            custoTotal += menorAresta.getPeso();
        }

        return new ResultadoPrim(arestasAGM, custoTotal);
    }
}

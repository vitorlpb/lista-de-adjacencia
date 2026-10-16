import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class BuscaProfundidade {

    public ResultadoBusca buscar(Grafo grafo, Vertice origem, Vertice destino) {

        // Cada item da pilha guarda a aresta pela qual o vértice foi alcançado
        // (null para a origem). O vértice só é visitado ao sair da pilha, então
        // o predecessor registrado é o do caminho realmente percorrido.
        Stack<Aresta> pilha = new Stack<>();
        Set<Vertice> visitados = new HashSet<>();
        Map<Vertice, Aresta> predecessor = new HashMap<>();
        List<Aresta> arvore = new ArrayList<>();

        pilha.push(new Aresta(null, origem, 0));

        boolean encontrado = false;

        while (!pilha.isEmpty()) {

            Aresta chegada = pilha.pop();
            Vertice atual = chegada.getDestino();

            if (visitados.contains(atual)) {
                continue;
            }

            visitados.add(atual);

            if (chegada.getOrigem() != null) {
                predecessor.put(atual, chegada);
                arvore.add(chegada);
            }

            if (atual == destino) {
                encontrado = true;
                break;
            }

            List<Adjacente> adjacentes = grafo.getAdjacentes(atual);

            // empilha de trás para frente para explorar os vizinhos na ordem da lista
            for (int i = adjacentes.size() - 1; i >= 0; i--) {

                Adjacente adjacente = adjacentes.get(i);
                Vertice vizinho = adjacente.getVertice();

                if (!visitados.contains(vizinho)) {
                    pilha.push(new Aresta(atual, vizinho, adjacente.getPeso()));
                }
            }
        }

        List<Aresta> caminho = reconstruirCaminho(predecessor, origem, destino, encontrado);

        return new ResultadoBusca(arvore, caminho, encontrado);
    }

    private List<Aresta> reconstruirCaminho(
            Map<Vertice, Aresta> predecessor,
            Vertice origem,
            Vertice destino,
            boolean encontrado) {

        List<Aresta> caminho = new ArrayList<>();

        if (!encontrado) {
            return caminho;
        }

        Vertice atual = destino;

        while (!atual.equals(origem)) {
            Aresta aresta = predecessor.get(atual);
            caminho.add(0, aresta);
            atual = aresta.getOrigem();
        }

        return caminho;
    }
}

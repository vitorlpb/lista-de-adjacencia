import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grafo {

    private final boolean dirigido;

    private final Map<String, Vertice> vertices;
    private final Map<Vertice, List<Adjacente>> adjacencias;

    public Boolean validaVertice(Vertice vertice, boolean remove) {
        // ação false = adiciona | true = remove
        boolean rotulo = vertices.containsKey(vertice.getRotulo());

        if (rotulo && !remove) {
            throw new IllegalArgumentException(
                    "Já existe um vértice com esse rótulo.");

        } else if (!rotulo && remove) {
            throw new IllegalArgumentException(
                    "Escolha um vértice existente");
        }

        return true;
    }

    public Grafo(boolean dirigido) {
        this.dirigido = dirigido;
        this.vertices = new HashMap<>();
        this.adjacencias = new HashMap<>();
    }

    public void adicionarVertice(Vertice vertice) {
        validaVertice(vertice, false);
        vertices.put(vertice.getRotulo(), vertice);
        adjacencias.put(vertice, new ArrayList<>());
    }

    public void removerVertices(Vertice vertice) {
        validaVertice(vertice, true);

        vertices.remove(vertice.getRotulo());
        adjacencias.remove(vertice);

        for (List<Adjacente> lista : adjacencias.values()) {
            lista.removeIf(adjacente -> adjacente.getVertice() == vertice);
        }

    }

    public void adicionarAresta(Vertice origem, Vertice destino, double peso) {
        validaVertice(origem, true);
        validaVertice(destino, true);

        if (possuiAresta(origem, destino)) {
            throw new IllegalArgumentException(
                    "Já existe uma aresta entre " + origem.getRotulo()
                            + " e " + destino.getRotulo() + ".");
        }

        adjacencias.get(origem).add(new Adjacente(destino, peso));

        if (!dirigido && origem != destino) {
            adjacencias.get(destino).add(new Adjacente(origem, peso));
        }
    }

    public void removerAresta(Vertice origem, Vertice destino) {
        validaVertice(origem, true);
        validaVertice(destino, true);

        boolean removeu = adjacencias.get(origem)
                .removeIf(adjacente -> adjacente.getVertice() == destino);

        if (!removeu) {
            throw new IllegalArgumentException(
                    "A aresta informada não existe no grafo.");
        }

        if (!dirigido) {
            adjacencias.get(destino)
                    .removeIf(adjacente -> adjacente.getVertice() == origem);
        }
    }

    public boolean possuiAresta(Vertice origem, Vertice destino) {
        for (Adjacente adjacente : adjacencias.get(origem)) {
            if (adjacente.getVertice() == destino) {
                return true;
            }
        }

        return false;
    }

    public List<Vertice> getVertices() {
        return new ArrayList<>(vertices.values());
    }

    public Vertice getVertice(String rotulo) {
        return vertices.get(rotulo);
    }

    public boolean possuiVertice(String rotulo) {
        return vertices.containsKey(rotulo);
    }

    public List<Adjacente> getAdjacentes(Vertice vertice) {
        return new ArrayList<>(adjacencias.get(vertice));
    }

    // Monta as arestas a partir da lista de adjacência.
    // Em grafo não dirigido, cada aresta aparece uma única vez.
    public List<Aresta> getArestas() {
        List<Aresta> arestas = new ArrayList<>();
        Set<Vertice> processados = new HashSet<>();

        for (Map.Entry<Vertice, List<Adjacente>> entrada : adjacencias.entrySet()) {

            Vertice origem = entrada.getKey();

            for (Adjacente adjacente : entrada.getValue()) {

                if (!dirigido && processados.contains(adjacente.getVertice())) {
                    continue;
                }

                arestas.add(new Aresta(origem, adjacente.getVertice(), adjacente.getPeso()));
            }

            processados.add(origem);
        }

        return arestas;
    }

    public boolean isDirigido() {
        return dirigido;
    }
}

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainCanvas extends JPanel {

    private Grafo grafo;

    private boolean modoAdicionarVertice;
    private boolean modoAdicionarAresta;
    private boolean modoRemoverVertice;
    private boolean modoRemoverAresta;

    private Vertice origemAresta;

    private List<Aresta> destacadas = new ArrayList<>();

    public MainCanvas(Grafo grafo) {
        this.grafo = grafo;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tratarClique(e);
            }
        });
    }

    public void setGrafo(Grafo grafo) {
        this.grafo = grafo;
        origemAresta = null;
        destacadas.clear();
        repaint();
    }

    public void ativarAdicionarVertice() {
        desativarModos();
        modoAdicionarVertice = true;
    }

    public void ativarAdicionarAresta() {
        desativarModos();
        modoAdicionarAresta = true;
    }

    public void ativarRemoverVertice() {
        desativarModos();
        modoRemoverVertice = true;
    }

    public void ativarRemoverAresta() {
        desativarModos();
        modoRemoverAresta = true;
    }

    public void executarPrim() {

        desativarModos();
        limparDestaque();

        Vertice inicio = escolherVertice("Vértice inicial para o Prim:");

        if (inicio == null) {
            return;
        }

        try {
            ResultadoPrim resultado = new Prim().calculaPrim(grafo, inicio);

            destacar(resultado.getArestas());

            JOptionPane.showMessageDialog(
                    this,
                    "Custo total da AGM: " + formatarPeso(resultado.getCustoTotal()));

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    public void executarBuscaProfundidade() {

        desativarModos();
        limparDestaque();

        Vertice origem = escolherVertice("Vértice de origem:");

        if (origem == null) {
            return;
        }

        Vertice destino = escolherVertice("Vértice de destino:");

        if (destino == null) {
            return;
        }

        ResultadoBusca resultado = new BuscaProfundidade().buscar(grafo, origem, destino);

        destacar(resultado.getArvore());

        if (!resultado.isEncontrado()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não existe caminho de " + origem.getRotulo()
                            + " até " + destino.getRotulo() + ".");
            return;
        }

        StringBuilder texto = new StringBuilder(origem.getRotulo());

        for (Aresta aresta : resultado.getCaminho()) {
            texto.append(" -> ").append(aresta.getDestino().getRotulo());
        }

        JOptionPane.showMessageDialog(this, "Caminho encontrado: " + texto);
    }

    public void executarRoy() {

        desativarModos();
        limparDestaque();

        List<List<Vertice>> componentes = new Roy().calcularComponentes(grafo);

        String titulo = grafo.isDirigido()
                ? "Componentes fortemente conexas:"
                : "Componentes conexas:";

        StringBuilder texto = new StringBuilder(titulo).append("\n");

        for (List<Vertice> componente : componentes) {

            texto.append("{ ");

            for (Vertice vertice : componente) {
                texto.append(vertice.getRotulo()).append(" ");
            }

            texto.append("}\n");
        }

        JOptionPane.showMessageDialog(this, texto.toString());
    }

    private void destacar(List<Aresta> arestas) {

        destacadas = new ArrayList<>(arestas);

        repaint();
    }

    private void limparDestaque() {
        destacadas.clear();
        repaint();
    }

    private boolean estaDestacada(Aresta aresta) {
        for (Aresta destacada : destacadas) {
            if (destacada.liga(aresta.getOrigem(), aresta.getDestino(), grafo.isDirigido())) {
                return true;
            }
        }

        return false;
    }

    private Vertice escolherVertice(String mensagem) {

        List<Vertice> lista = grafo.getVertices();

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O grafo não tem vértices.");
            return null;
        }

        String[] rotulos = new String[lista.size()];

        for (int i = 0; i < lista.size(); i++) {
            rotulos[i] = lista.get(i).getRotulo();
        }

        String escolhido = (String) JOptionPane.showInputDialog(
                this,
                mensagem,
                "Selecionar vértice",
                JOptionPane.QUESTION_MESSAGE,
                null,
                rotulos,
                rotulos[0]);

        if (escolhido == null) {
            return null;
        }

        return grafo.getVertice(escolhido);
    }

    private void desativarModos() {
        modoAdicionarVertice = false;
        modoAdicionarAresta = false;
        modoRemoverVertice = false;
        modoRemoverAresta = false;
        origemAresta = null;
    }

    private void tratarClique(MouseEvent e) {

        if (modoAdicionarVertice) {
            adicionarVertice(e.getX(), e.getY());
            return;
        }

        if (modoAdicionarAresta) {
            selecionarAresta(e.getX(), e.getY());
            return;
        }

        if (modoRemoverVertice) {
            removerVertice(e.getX(), e.getY());
            return;
        }

        if (modoRemoverAresta) {
            removerAresta(e.getX(), e.getY());
        }
    }

    private void adicionarVertice(int x, int y) {

        String rotulo = JOptionPane.showInputDialog(
                this,
                "Rótulo do vértice:");

        if (rotulo == null || rotulo.isBlank()) {
            return;
        }

        rotulo = rotulo.trim();

        if (grafo.possuiVertice(rotulo)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Já existe um vértice com o rótulo " + rotulo + ".");

            return;
        }

        Vertice vertice = new Vertice(rotulo, x, y);

        grafo.adicionarVertice(vertice);

        repaint();
    }

    private void selecionarAresta(int x, int y) {

        Vertice vertice = encontrarVertice(x, y);

        if (vertice == null) {
            return;
        }

        if (origemAresta == null) {
            origemAresta = vertice;
            repaint();
            return;
        }

        String entrada = JOptionPane.showInputDialog(this,"Peso do vértice:");
        
        if (entrada == null || entrada.isBlank()) {
            entrada = "1";
        }
        double peso = Double.parseDouble(entrada);

        Vertice destino = vertice;

        try {
            grafo.adicionarAresta(origemAresta, destino, peso);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

        origemAresta = null;

        repaint();
    }

    private Vertice encontrarVertice(int x, int y) {

        for (Vertice vertice : grafo.getVertices()) {

            int dx = x - vertice.getX();
            int dy = y - vertice.getY();

            if (dx * dx + dy * dy <= 15 * 15) {
                return vertice;
            }
        }

        return null;
    }

    private void removerVertice(int x, int y) {

        Vertice vertice = encontrarVertice(x, y);

        if (vertice == null) {
            return;
        }

        grafo.removerVertices(vertice);

        repaint();
    }

    private void removerAresta(int x, int y) {

        Aresta aresta = encontrarAresta(x, y);

        if (aresta == null) {
            return;
        }

        grafo.removerAresta(aresta.getOrigem(), aresta.getDestino());

        repaint();
    }

    private Aresta encontrarAresta(int x, int y) {

        Aresta maisProxima = null;
        double menorDistancia = 8;

        for (Aresta aresta : grafo.getArestas()) {

            double distancia = distanciaAoSegmento(
                    x, y,
                    aresta.getOrigem().getX(), aresta.getOrigem().getY(),
                    aresta.getDestino().getX(), aresta.getDestino().getY());

            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                maisProxima = aresta;
            }
        }

        return maisProxima;
    }

    private double distanciaAoSegmento(
            int px, int py, int ax, int ay, int bx, int by) {

        double dx = bx - ax;
        double dy = by - ay;
        double comprimentoQuadrado = dx * dx + dy * dy;

        double t = 0;

        if (comprimentoQuadrado > 0) {
            t = ((px - ax) * dx + (py - ay) * dy) / comprimentoQuadrado;
            t = Math.max(0, Math.min(1, t));
        }

        double projX = ax + t * dx;
        double projY = ay + t * dy;

        double distX = px - projX;
        double distY = py - projY;

        return Math.sqrt(distX * distX + distY * distY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        desenharArestas(g);
        desenharVertices(g);
    }

    private void desenharArestas(Graphics g) {
        for (Aresta aresta : grafo.getArestas()) {

            g.setColor(estaDestacada(aresta)
                    ? Color.RED
                    : Color.BLACK);

            if (grafo.isDirigido()) {
                desenharSeta(g, aresta);
            } else {
                desenharLinha(g, aresta);
            }

            desenharPeso(g, aresta);
        }

        g.setColor(Color.BLACK);
    }

    private void desenharPeso(Graphics g, Aresta aresta) {

        Vertice origem = aresta.getOrigem();
        Vertice destino = aresta.getDestino();

        int meioX = (origem.getX() + destino.getX()) / 2;
        int meioY = (origem.getY() + destino.getY()) / 2;

        g.drawString(formatarPeso(aresta.getPeso()), meioX, meioY - 5);
    }

    private String formatarPeso(double peso) {

        if (peso == Math.floor(peso)) {
            return String.valueOf((long) peso);
        }

        return String.valueOf(peso);
    }

    private void desenharLinha(Graphics g, Aresta aresta) {

        Vertice origem = aresta.getOrigem();
        Vertice destino = aresta.getDestino();

        g.drawLine(
                origem.getX(),
                origem.getY(),
                destino.getX(),
                destino.getY());
    }

    private void desenharSeta(Graphics g, Aresta aresta) {

        Vertice origem = aresta.getOrigem();
        Vertice destino = aresta.getDestino();
        

        int x1 = origem.getX();
        int y1 = origem.getY();

        int x2 = destino.getX();
        int y2 = destino.getY();

        double angulo = Math.atan2(y2 - y1, x2 - x1);

        int raioVertice = 15;

        int xPonta = (int) (x2 - raioVertice * Math.cos(angulo));
        int yPonta = (int) (y2 - raioVertice * Math.sin(angulo));

        g.drawLine(x1, y1, xPonta, yPonta);

        int tamanho = 10;

        int x3 = (int) (xPonta - tamanho * Math.cos(angulo - Math.PI / 6));
        int y3 = (int) (yPonta - tamanho * Math.sin(angulo - Math.PI / 6));

        int x4 = (int) (xPonta - tamanho * Math.cos(angulo + Math.PI / 6));
        int y4 = (int) (yPonta - tamanho * Math.sin(angulo + Math.PI / 6));

        g.drawLine(xPonta, yPonta, x3, y3);
        g.drawLine(xPonta, yPonta, x4, y4);
    }

    private void desenharVertices(Graphics g) {

        for (Vertice vertice : grafo.getVertices()) {

            int x = vertice.getX();
            int y = vertice.getY();

            g.fillOval(x - 15, y - 15, 30, 30);

            g.drawString(
                    vertice.getRotulo(),
                    x - 5,
                    y - 20);
        }
    }
}
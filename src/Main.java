import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {

        Grafo grafo = criarGrafo();

        MainCanvas canvas = new MainCanvas(grafo);

        JFrame frame = new JFrame("Editor de Grafos");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        frame.setJMenuBar(criarMenu(frame, canvas));

        frame.setContentPane(canvas);
        frame.setVisible(true);
    }

    private static Grafo criarGrafo() {

        int resposta = JOptionPane.showConfirmDialog(
                null,
                "O grafo é dirigido?",
                "Novo grafo",
                JOptionPane.YES_NO_OPTION);

        return new Grafo(resposta == JOptionPane.YES_OPTION);
    }

    private static JMenuBar criarMenu(
            JFrame frame,
            MainCanvas canvas) {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuGrafo = new JMenu("Grafo");
        JMenu menuAlgoritmos = new JMenu("Algoritmos");

        JMenuItem novo = new JMenuItem("Novo");
        JMenuItem adicionarVertice = new JMenuItem("Adicionar vértice");
        JMenuItem adicionarAresta = new JMenuItem("Adicionar aresta");
        JMenuItem removerVertice = new JMenuItem("Remover vértice");
        JMenuItem removerAresta = new JMenuItem("Remover aresta");
        JMenuItem prim = new JMenuItem("Prim (AGM)");
        JMenuItem buscaProfundidade = new JMenuItem("Busca em profundidade guiada");
        JMenuItem roy = new JMenuItem("Roy (componentes)");
        JMenuItem mapaParana = new JMenuItem("Carregar mapa do Paraná");
        JMenuItem aEstrela = new JMenuItem("A* (caminho mínimo)");
        JMenuItem dsatur = new JMenuItem("DSATUR (coloração)");

        novo.addActionListener(e -> {

            Grafo novoGrafo = criarGrafo();

            canvas.setGrafo(novoGrafo);
        });

        mapaParana.addActionListener(e -> {
            canvas.setGrafo(MapaParana.criar());
        });

        aEstrela.addActionListener(e -> {
            canvas.executarAEstrela();
        });

        dsatur.addActionListener(e -> {
            canvas.executarDsatur();
        });

        adicionarVertice.addActionListener(e -> {
            canvas.ativarAdicionarVertice();
        });

        adicionarAresta.addActionListener(e -> {
            canvas.ativarAdicionarAresta();
        });

        removerVertice.addActionListener(e -> {
            canvas.ativarRemoverVertice();
        });

        removerAresta.addActionListener(e -> {
            canvas.ativarRemoverAresta();
        });

        prim.addActionListener(e -> {
            canvas.executarPrim();
        });

        buscaProfundidade.addActionListener(e -> {
            canvas.executarBuscaProfundidade();
        });

        roy.addActionListener(e -> {
            canvas.executarRoy();
        });

        menuGrafo.add(novo);
        menuGrafo.add(mapaParana);
        menuGrafo.addSeparator();
        menuGrafo.add(adicionarVertice);
        menuGrafo.add(adicionarAresta);
        menuGrafo.addSeparator();
        menuGrafo.add(removerVertice);
        menuGrafo.add(removerAresta);

        menuAlgoritmos.add(prim);
        menuAlgoritmos.add(buscaProfundidade);
        menuAlgoritmos.add(roy);
        menuAlgoritmos.add(aEstrela);
        menuAlgoritmos.add(dsatur);

        menuBar.add(menuGrafo);
        menuBar.add(menuAlgoritmos);

        return menuBar;
    }
}
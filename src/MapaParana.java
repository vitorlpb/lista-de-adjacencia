public class MapaParana {

    // Posição na tela (x, y) aproximada da figura mapa.png
    // e coordenadas geográficas (latitude, longitude) de cada cidade.
    public static Grafo criar() {

        Grafo grafo = new Grafo(false);

        cidade(grafo, "Umuarama",          185, 141, -23.766, -53.325);
        cidade(grafo, "Maringá",           347,  65, -23.420, -51.933);
        cidade(grafo, "Londrina",          439,  60, -23.310, -51.163);
        cidade(grafo, "Toledo",            120, 255, -24.725, -53.743);
        cidade(grafo, "Cascavel",          162, 319, -24.956, -53.455);
        cidade(grafo, "Foz do Iguaçu",      60, 415, -25.547, -54.588);
        cidade(grafo, "Francisco Beltrão", 233, 520, -26.081, -53.055);
        cidade(grafo, "Guarapuava",        411, 390, -25.390, -51.463);
        cidade(grafo, "São Mateus do Sul", 512, 467, -25.874, -50.383);
        cidade(grafo, "Ponta Grossa",      546, 363, -25.095, -50.162);
        cidade(grafo, "Curitiba",          651, 394, -25.428, -49.273);
        cidade(grafo, "Paranaguá",         740, 409, -25.520, -48.509);

        estrada(grafo, "Umuarama", "Maringá", 190);
        estrada(grafo, "Maringá", "Londrina", 114);
        estrada(grafo, "Umuarama", "Toledo", 126);
        estrada(grafo, "Toledo", "Cascavel", 50);
        estrada(grafo, "Maringá", "Ponta Grossa", 314);
        estrada(grafo, "Londrina", "Ponta Grossa", 273);
        estrada(grafo, "Cascavel", "Guarapuava", 250);
        estrada(grafo, "Guarapuava", "Ponta Grossa", 165);
        estrada(grafo, "Cascavel", "Foz do Iguaçu", 143);
        estrada(grafo, "Cascavel", "Francisco Beltrão", 186);
        estrada(grafo, "Francisco Beltrão", "São Mateus do Sul", 354);
        estrada(grafo, "São Mateus do Sul", "Curitiba", 157);
        estrada(grafo, "Ponta Grossa", "Curitiba", 114);
        estrada(grafo, "Curitiba", "Paranaguá", 90);

        return grafo;
    }

    private static void cidade(
            Grafo grafo, String nome, int x, int y, double latitude, double longitude) {
        grafo.adicionarVertice(new Vertice(nome, x, y, latitude, longitude));
    }

    private static void estrada(Grafo grafo, String origem, String destino, double km) {
        grafo.adicionarAresta(grafo.getVertice(origem), grafo.getVertice(destino), km);
    }
}

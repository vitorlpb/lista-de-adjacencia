public class Vertice {
    private final String rotulo;

    private int x;
    private int y;

    private final double latitude;
    private final double longitude;

    public Vertice(String rotulo, int x, int y, double latitude, double longitude) {
        this.rotulo = rotulo;
        this.x = x;
        this.y = y;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getRotulo() {
        return rotulo;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}

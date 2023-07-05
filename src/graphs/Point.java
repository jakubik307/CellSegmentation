package graphs;

public record Point(int x, int y) {
    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }

    public double distanceTo(Point other) {
        return Math.sqrt(Math.pow(other.x() - x(), 2) + Math.pow(other.y() - y(), 2));
    }
}

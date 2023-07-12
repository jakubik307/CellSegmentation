package graphs;

public record Point(int x, int y) {
    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }

    public double distanceTo(Point other) {
        return Math.sqrt(Math.pow(other.x() - x(), 2) + Math.pow(other.y() - y(), 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}

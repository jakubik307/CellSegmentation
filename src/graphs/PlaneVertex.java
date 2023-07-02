package graphs;

public class PlaneVertex extends Vertex{
    private final int x;
    private final int y;
    public PlaneVertex(int id, int x, int y) {
        super(id);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PlaneVertex that = (PlaneVertex) o;

        if (x != that.x) return false;
        return y == that.y;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + x;
        result = 31 * result + y;
        return result;
    }
}

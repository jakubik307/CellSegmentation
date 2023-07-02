package graphs;

import java.util.Objects;

public class Vertex {
    private final int id;

    public Vertex(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Vertex) obj;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }

}

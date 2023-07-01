package Graphs;

import java.util.List;
import java.util.Objects;

public class Cycle {
    private final List<Vertex> vertices;
    private final int weight;

    public Cycle(List<Vertex> vertices, int weight) {
        this.vertices = vertices;
        this.weight = weight;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Cycle other = (Cycle) obj;
        if (vertices.size() != other.vertices.size()) {
            return false;
        }
        // Sprawdzamy, czy istnieje permutacja wierzchołków, która jest równa innej permutacji
        for (int i = 0; i < vertices.size(); i++) {
            boolean isEqual = true;
            for (int j = 0; j < vertices.size(); j++) {
                Vertex vertex = vertices.get((i + j) % vertices.size()); // Używamy modulo, aby uzyskać cykliczne permutacje wierzchołków
                if (!vertex.equals(other.vertices.get(j))) {
                    isEqual = false;
                    break;
                }
            }
            if (isEqual) {
                return true;
            }
        }
        // Sprawdzamy, czy istnieje permutacja wierzchołków, która jest równa innej permutacji w odwrotnej kolejności
        for (int i = 0; i < vertices.size(); i++) {
            boolean isEqual = true;
            for (int j = 0; j < vertices.size(); j++) {
                Vertex vertex = vertices.get((i - j + vertices.size()) % vertices.size()); // Używamy modulo, aby uzyskać cykliczne permutacje wierzchołków w odwrotnej kolejności
                if (!vertex.equals(other.vertices.get(j))) {
                    isEqual = false;
                    break;
                }
            }
            if (isEqual) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices);
    }

    @Override
    public String toString() {
        return "Cycle: " + vertices.toString() + "      Sum of weights: " + weight;
    }
}

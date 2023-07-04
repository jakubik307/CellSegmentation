package graphs;

import java.util.*;

@SuppressWarnings("unused")
public class Graph {
    protected Map<Vertex, List<Edge>> adjacencyMap;
    protected List<Edge> edges;

    public Graph() {
        adjacencyMap = new HashMap<>();
        edges = new ArrayList<>();
    }

    public Map<Vertex, List<Edge>> getAdjacencyMap() {
        return adjacencyMap;
    }

    private void addVertex(int i) {
        adjacencyMap.put(new Vertex(i), new ArrayList<>());
    }

    public void addEdge(Vertex source, Vertex destination) {
        Edge edge = new Edge(source, destination);
        adjacencyMap.get(source).add(edge);
        edges.add(edge);
    }

    public List<Edge> getAdjacentEdges(Vertex vertex) {
        return adjacencyMap.get(vertex);
    }

    public List<Vertex> getVertices() {
        return new ArrayList<>(adjacencyMap.keySet());
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Cycle> findAllCycles() {
        List<Cycle> cycles = new ArrayList<>();
        Set<Vertex> visited = new HashSet<>();
        for (Vertex vertex : getVertices()) {
            if (!visited.contains(vertex)) {
                List<Vertex> path = new ArrayList<>();
                dfs(vertex, visited, path, cycles);
            }
        }
        cycles = filterOutTwoNodeCycles(cycles);
        cycles.sort(Comparator.comparingInt(Cycle::getSize).reversed());
        return cycles;
    }

    private void dfs(Vertex vertex, Set<Vertex> visited, List<Vertex> path, List<Cycle> cycles) {
        visited.add(vertex);
        path.add(vertex);

        List<Edge> adjacentEdges = adjacencyMap.get(vertex);
        for (Edge edge : adjacentEdges) {
            Vertex neighbor = edge.destination();
            if (path.size() > 2 && neighbor.equals(path.get(0))) {
                // Cykl został znaleziony
                List<Vertex> cycle = new ArrayList<>(path);
                Cycle newCycle = new Cycle(cycle);
                if (!cycles.contains(newCycle)) {
                    cycles.add(newCycle);
                }
            } else if (!visited.contains(neighbor)) {
                dfs(neighbor, visited, path, cycles);
            }
        }

        path.remove(vertex);
        visited.remove(vertex);
    }

    private List<Cycle> filterOutTwoNodeCycles(List<Cycle> cycles) {
        List<Cycle> filteredCycles = new ArrayList<>();
        for (Cycle cycle : cycles) {
            if (cycle.getVertices().size() > 2) {
                filteredCycles.add(cycle);
            }
        }
        return filteredCycles;
    }
}

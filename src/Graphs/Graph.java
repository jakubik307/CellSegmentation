package Graphs;

import java.util.*;

public class Graph {
    private static final int MIN_VERTICES = 1;
    private static final int MAX_VERTICES = 10;
    private static final int MIN_WEIGHT = 1;
    private static final int MAX_WEIGHT = 20;
    private static final double EDGE_PROBABILITY = 0.5;

    protected Map<Vertex, List<Edge>> adjacencyMap;
    protected List<Edge> edges;

    protected Vertex startVertex;
    protected Vertex endVertex;

    public Graph() {
        adjacencyMap = new HashMap<>();
        edges = new ArrayList<>();
    }

    private void addVertex(int i) {
        adjacencyMap.put(new Vertex(i), new ArrayList<>());
    }

    public void addEdge(Vertex source, Vertex destination, int weight) {
        Edge edge = new Edge(source, destination, weight);
        adjacencyMap.get(source).add(edge);
        edges.add(edge);
    }

    public List<Edge> getAdjacentEdges(Vertex vertex) {
        return adjacencyMap.get(vertex);
    }

    public List<Vertex> getVertices() {
        return new ArrayList<>(adjacencyMap.keySet());
    }

    public Vertex getStartVertex() {
        return startVertex;
    }

    public Vertex getEndVertex() {
        return endVertex;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void generateDijkstraGraph() {
        Random random = new Random();
        int numVertices = random.nextInt(MIN_VERTICES, MAX_VERTICES + 1);

        for (int i = 1; i <= numVertices; i++) {
            addVertex(i);
        }

        for (int i = 1; i <= numVertices; i++) {
            for (int j = i + 1; j <= numVertices; j++) {
                if (Math.random() <= EDGE_PROBABILITY) {
                    int weight = random.nextInt(MIN_WEIGHT, MAX_WEIGHT + 1);
                    addEdge(new Vertex(i), new Vertex(j), weight);
                    addEdge(new Vertex(j), new Vertex(i), weight);
                }
            }
        }

        startVertex = new Vertex(random.nextInt(numVertices) + 1);
        if (numVertices > 1)
            do endVertex = new Vertex(random.nextInt(numVertices) + 1); while (startVertex.equals(endVertex));
    }

    public void generateGabrielGraph() {
    }

    public void generateRNGraph() {
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
        cycles.sort(Comparator.comparingInt(Cycle::getWeight).reversed());
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
                int weight = calculateCycleWeight(cycle);
                Cycle newCycle = new Cycle(cycle, weight);
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

    private int calculateCycleWeight(List<Vertex> cycle) {
        int weightSum = 0;
        for (int i = 0; i < cycle.size(); i++) {
            Vertex source = cycle.get(i);
            Vertex destination = cycle.get((i + 1) % cycle.size()); // Używamy modulo, aby uzyskać cykliczne połączenie ostatniego wierzchołka z pierwszym
            for (Edge edge : adjacencyMap.get(source)) {
                if (edge.destination().equals(destination)) {
                    weightSum += edge.weight();
                    break;
                }
            }
        }
        return weightSum;
    }
}

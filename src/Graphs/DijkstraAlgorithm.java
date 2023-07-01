package Graphs;

import java.util.*;

public class DijkstraAlgorithm {
    public static List<Vertex> findShortestPath(Graph graph) {
        Vertex source = graph.getStartVertex();

        Map<Vertex, Integer> distances = new HashMap<>();
        Map<Vertex, Vertex> previousVertices = new HashMap<>();
        Set<Vertex> visited = new HashSet<>();

        for (Vertex vertex : graph.getVertices()) {
            distances.put(vertex, Integer.MAX_VALUE);
        }
        distances.put(source, 0);

        while (visited.size() < graph.getVertices().size()) {
            Vertex currentVertex = getMinimumDistanceVertex(distances, visited);
            if (currentVertex == null) break;
            visited.add(currentVertex);

            List<Edge> adjacentEdges = graph.getAdjacentEdges(currentVertex);
            if (adjacentEdges != null) {
                for (Edge edge : adjacentEdges) {
                    Vertex neighbor = edge.destination();
                    int weight = edge.weight();
                    int totalDistance = distances.get(currentVertex) + weight;

                    if (totalDistance < distances.get(neighbor)) {
                        distances.put(neighbor, totalDistance);
                        previousVertices.put(neighbor, currentVertex);
                    }
                }
            }
        }

        return getPath(graph.getEndVertex(), previousVertices);
    }

    private static Vertex getMinimumDistanceVertex(Map<Vertex, Integer> distances, Set<Vertex> visited) {
        Vertex minVertex = null;
        int minDistance = Integer.MAX_VALUE;

        for (Map.Entry<Vertex, Integer> entry : distances.entrySet()) {
            Vertex vertex = entry.getKey();
            int distance = entry.getValue();

            if (distance < minDistance && !visited.contains(vertex)) {
                minVertex = vertex;
                minDistance = distance;
            }
        }

        return minVertex;
    }

    private static List<Vertex> getPath(Vertex endVertex, Map<Vertex, Vertex> previousVertices) {
        List<Vertex> path = new ArrayList<>();
        Vertex currentVertex = endVertex;

        while (currentVertex != null) {
            path.add(0, currentVertex);
            currentVertex = previousVertices.get(currentVertex);
        }

        return path;
    }
}


package graphs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@SuppressWarnings("unused")
@Deprecated
public class FastCycleDetection {
    public static List<Cycle> enumerateCycles(Graph graph) {
        List<Cycle> cycles = new ArrayList<>();
        int numVertices = graph.getVertices().size();

        // Initialize the queue with all vertices
        Queue<List<Vertex>> queue = new LinkedList<>();
        for (Vertex vertex : graph.getVertices()) {
            List<Vertex> path = new ArrayList<>();
            path.add(vertex);
            queue.add(path);
        }

        while (!queue.isEmpty()) {
            List<Vertex> path = queue.poll();
            Vertex head = path.get(0);
            Vertex tail = path.get(path.size() - 1);
            int k = path.size();

            // Check if there is an edge from tail to head
            if (graph.getAdjacencyMap().get(tail).contains(new Edge(tail, head))) {
                // A cycle is found
                cycles.add(new Cycle(path));
            }

            // Generate new k + 1 length open paths from the k length open path
            for (Edge edge : graph.getAdjacencyMap().get(tail)) {
                Vertex neighbor = edge.destination();

                if (path.contains(neighbor)) {
                    continue;  // Skip if the neighbor is already in the path
                }
                if (neighbor.getID() > head.getID()) {
                    List<Vertex> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }

            // Set the register to k + 1 if the current path is the last k length open path in the queue
            if (queue.isEmpty() || queue.peek().size() != k) {
                k++;
            }
        }

        return cycles;
    }
}

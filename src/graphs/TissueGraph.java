package graphs;

import org.jgrapht.Graph;
import org.jgrapht.alg.cycle.PatonCycleBase;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TissueGraph {
    public static Graph<Point, DefaultEdge> generateRNGraph(List<Point> list) {
        Graph<Point, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        for (Point point : list) {
            graph.addVertex(point);
        }

        for (Point p1 : graph.vertexSet()) {
            for (Point p2 : graph.vertexSet()) {
                if (!p1.equals(p2)) {
                    boolean isValidEdge = true;

                    double distP1P2 = Math.sqrt(Math.pow(p2.x() - p1.x(), 2) + Math.pow(p2.y() - p1.y(), 2));

                    for (Point p3 : graph.vertexSet()) {
                        if (!p3.equals(p1) && !p3.equals(p2)) {
                            double distP1P3 = Math.sqrt(Math.pow(p1.x() - p3.x(), 2) + Math.pow(p1.y() - p3.y(), 2));
                            double distP2P3 = Math.sqrt(Math.pow(p2.x() - p3.x(), 2) + Math.pow(p2.y() - p3.y(), 2));
                            if (distP2P3 < distP1P2 && distP1P3 < distP1P2) {
                                isValidEdge = false;
                                break;
                            }
                        }
                    }

                    if (isValidEdge) graph.addEdge(p1, p2);
                }
            }
        }

        return graph;
    }

    public static List<List<Point>> getCyclesPaton(Graph<Point, DefaultEdge> graph) {
        Set<List<Point>> allCycles = new HashSet<>();
        PatonCycleBase<Point, DefaultEdge> cycleBase = new PatonCycleBase<>(graph);

        cycleBase.getCycleBasis().getCycles().forEach(cycle -> {
            List<Point> currentCycle = new ArrayList<>();
            cycle.forEach(defaultEdge -> {
                Point source = graph.getEdgeSource(defaultEdge);
                Point target = graph.getEdgeTarget(defaultEdge);
                if (!currentCycle.contains(source)) currentCycle.add(source);
                if (!currentCycle.contains(target)) currentCycle.add(target);
            });
            allCycles.add(currentCycle);
        });

        List<List<Point>> cycleList = new ArrayList<>(allCycles.stream().toList());
        cycleList.sort((o1, o2) -> o2.size() - o1.size());

        return cycleList;
    }
}

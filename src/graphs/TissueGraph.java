package graphs;

import org.jgrapht.Graph;
import org.jgrapht.alg.cycle.PatonCycleBase;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;

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

    @SuppressWarnings("unused")
    public static List<List<Point>> getCyclesPaton(Graph<Point, DefaultEdge> graph) {
        Set<List<Point>> allCycles = new HashSet<>();
        PatonCycleBase<Point, DefaultEdge> cycleBase = new PatonCycleBase<>(graph);

        cycleBase.getCycleBasis().getCycles().forEach(cycle -> {
            List<Point> currentCycle = new ArrayList<>();
            DefaultEdge startEdge = cycle.get(0);
            DefaultEdge nextEdge = cycle.get(1);
            cycle.forEach(defaultEdge -> {
                if (defaultEdge.equals(startEdge)) {
                    if (graph.getEdgeSource(nextEdge).equals(graph.getEdgeSource(startEdge)) || graph.getEdgeTarget(nextEdge).equals(graph.getEdgeSource(startEdge))) {
                        currentCycle.add(graph.getEdgeSource(startEdge));
                    } else {
                        currentCycle.add(graph.getEdgeTarget(startEdge));
                    }
                } else {
                    if (currentCycle.contains(graph.getEdgeSource(defaultEdge))) {
                        currentCycle.add(graph.getEdgeTarget(defaultEdge));
                    } else {
                        currentCycle.add(graph.getEdgeSource(defaultEdge));
                    }
                }

            });
            allCycles.add(currentCycle);
        });

        List<List<Point>> cycleList = new ArrayList<>(allCycles.stream().toList());
        cycleList.sort(Comparator.comparingInt(List::size));

        return cycleList;
    }

    public static List<List<Point>> getCycles(Graph<Point, DefaultEdge> graph) {
        Set<List<Point>> allCycles = new HashSet<>();
        List<DefaultEdge> edges = new ArrayList<>(graph.edgeSet());
        Map<Point, List<Point>> clockwiseGraph = new HashMap<>();

        graph.vertexSet().forEach(point -> {
            clockwiseGraph.put(point, new ArrayList<>());
            graph.outgoingEdgesOf(point).forEach(defaultEdge -> {
                if (graph.getEdgeSource(defaultEdge).equals(point)) {
                    clockwiseGraph.get(point).add(graph.getEdgeTarget(defaultEdge));
                } else {
                    clockwiseGraph.get(point).add(graph.getEdgeSource(defaultEdge));
                }
            });
            clockwiseGraph.get(point).sort(new ClockwiseComparator(point));
        });

        for (DefaultEdge edge : edges) {
            allCycles.add(getClockwiseCycle(clockwiseGraph, graph.getEdgeSource(edge), graph.getEdgeTarget(edge)));
            allCycles.add(getClockwiseCycle(clockwiseGraph, graph.getEdgeTarget(edge), graph.getEdgeSource(edge)));
        }

        // Remove cycles with less than 3 points
        allCycles.removeIf(Objects::isNull);
        allCycles.removeIf(cycle -> cycle.size() < 3);

        // Remove cycles permutations
        List<List<Point>> allCyclesList = new ArrayList<>(allCycles);
        for (int i = 0; i < allCyclesList.size(); i++) {
            for (int j = allCyclesList.size() - 1; j >= 0; j--) {
                if (i != j && new HashSet<>(allCyclesList.get(i)).containsAll(allCyclesList.get(j))) {
                    allCyclesList.remove(j);
                }
            }
        }

        return allCyclesList;
    }

    private static List<Point> getClockwiseCycle(Map<Point, List<Point>> graph, Point start, Point next) {
        List<Point> cycle = new ArrayList<>();
        Set<Point> visited = new HashSet<>();

        cycle.add(start);
        visited.add(start);

        cycle.add(next);
        visited.add(next);

        Point prev = start;
        Point current = next;

        while (true) {
            int pos = (graph.get(current).indexOf(prev) + 1) % graph.get(current).size();
            prev = current;
            current = graph.get(current).get(pos);
            if (current.equals(start)) break;
            if (visited.contains(current)) return null;
            cycle.add(current);
            visited.add(current);
        }

        return cycle;
    }
}

package graphs;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class PlaneGraph extends Graph {

    private final List<PlaneVertex> vertices;

    public PlaneGraph(List<PlaneVertex> vertices) {
        super();
        this.vertices = vertices;
        for (PlaneVertex vertex : vertices) {
            addVertex(vertex.getID(), vertex.getX(), vertex.getY());
        }
    }

    public void generateGabrielGraph() {
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                boolean isValidEdge = true;
                PlaneVertex p1 = vertices.get(i);
                PlaneVertex p2 = vertices.get(j);

                double distance = Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
                PlaneVertex center = new PlaneVertex(0, (p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);

                for (int k = 0; k < vertices.size(); k++) {
                    if (k != i && k != j) {
                        PlaneVertex p3 = vertices.get(k);
                        double radius = Math.sqrt(Math.pow(center.getX() - p3.getX(), 2) + Math.pow(center.getY() - p3.getY(), 2));
                        if (radius < distance / 2) {
                            isValidEdge = false;
                            break;
                        }
                    }
                }

                if (isValidEdge) {
                    addEdge(p1, p2);
                    addEdge(p2, p1);
                }
            }
        }
    }

    public void generateRNGraph() {
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                boolean isValidEdge = true;
                PlaneVertex p1 = vertices.get(i);
                PlaneVertex p2 = vertices.get(j);

                double distP1P2 = Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));

                for (int k = 0; k < vertices.size(); k++) {
                    if (k != i && k != j) {
                        PlaneVertex p3 = vertices.get(k);
                        double distP1P3 = Math.sqrt(Math.pow(p1.getX() - p3.getX(), 2) + Math.pow(p1.getY() - p3.getY(), 2));
                        double distP2P3 = Math.sqrt(Math.pow(p2.getX() - p3.getX(), 2) + Math.pow(p2.getY() - p3.getY(), 2));
                        if (distP2P3 < distP1P2 && distP1P3 < distP1P2) {
                            isValidEdge = false;
                            break;
                        }
                    }
                }

                if (isValidEdge) {
                    addEdge(p1, p2);
                    addEdge(p2, p1);
                }
            }
        }
    }

    private void addVertex(int id, int x, int y) {
        PlaneVertex vertex = new PlaneVertex(id, x, y);
        adjacencyMap.put(vertex, new ArrayList<>());
//        vertices.add(vertex);
    }
}

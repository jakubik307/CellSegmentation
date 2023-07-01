package Graphs;

import View.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@SuppressWarnings("unused")
public class PlaneGraph extends Graph {
    private static final int MIN_VERTICES = 10;
    private static final int MAX_VERTICES = 20;
    private static final int MIN_WEIGHT = 1;
    private static final int MAX_WEIGHT = 20;

    private final ArrayList<PlaneVertex> vertices;

    public PlaneGraph() {
        adjacencyMap = new HashMap<>();
        edges = new ArrayList<>();
        vertices = new ArrayList<>();
    }

    @Override
    public void generateGabrielGraph() {
        Random random = new Random();
        int numVertices = random.nextInt(MIN_VERTICES, MAX_VERTICES + 1);

        for (int i = 1; i <= numVertices; i++) {
            addVertex(i, random.nextInt(50, GUI.WIDTH - 50), random.nextInt(100, GUI.HEIGHT) - 100);
        }

        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                boolean isValidEdge = true;
                PlaneVertex p1 = vertices.get(i);
                PlaneVertex p2 = vertices.get(j);

                double distance = Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
                PlaneVertex center = new PlaneVertex(0, (p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);

                for (int k = 0; k < numVertices; k++) {
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
                    int weight = random.nextInt(MIN_WEIGHT, MAX_WEIGHT + 1);
                    addEdge(p1, p2, weight);
                    addEdge(p2, p1, weight);
                }
            }
        }

        startVertex = vertices.get(random.nextInt(numVertices));
        if (numVertices > 1)
            do endVertex = vertices.get(random.nextInt(numVertices)); while (startVertex.equals(endVertex));
    }

    @Override
    public void generateRNGraph() {
        Random random = new Random();
        int numVertices = random.nextInt(MIN_VERTICES, MAX_VERTICES + 1);

        for (int i = 1; i <= numVertices; i++) {
            addVertex(i, random.nextInt(50, GUI.WIDTH - 50), random.nextInt(100, GUI.HEIGHT) - 100);
        }

        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                boolean isValidEdge = true;
                PlaneVertex p1 = vertices.get(i);
                PlaneVertex p2 = vertices.get(j);

                double distP1P2 = Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));

                for (int k = 0; k < numVertices; k++) {
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
                    int weight = random.nextInt(MIN_WEIGHT, MAX_WEIGHT + 1);
                    addEdge(p1, p2, weight);
                    addEdge(p2, p1, weight);
                }
            }
        }

        startVertex = vertices.get(random.nextInt(numVertices));
        if (numVertices > 1)
            do endVertex = vertices.get(random.nextInt(numVertices)); while (startVertex.equals(endVertex));
    }

    private void addVertex(int i, int x, int y) {
        PlaneVertex vertex = new PlaneVertex(i, x, y);
        adjacencyMap.put(vertex, new ArrayList<>());
        vertices.add(vertex);
    }

}

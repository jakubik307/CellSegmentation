package View;

import Graphs.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static Graphs.DijkstraAlgorithm.findShortestPath;

public class GUI extends JFrame {
    public static int WIDTH = 600;
    public static int HEIGHT = 700;
    public static int BUTTON_PANEL_HEIGHT = 50;

    private Graph graph;
    private List<Vertex> vertices;
    private List<Vertex> shortestPath;

    private int drawingOption = 0;

    private BufferedImage backgroundImage;

    public GUI() {

        loadBackgroundImage();
        WIDTH = backgroundImage.getWidth();
        HEIGHT = backgroundImage.getHeight();

        setSize(new Dimension(WIDTH, HEIGHT + BUTTON_PANEL_HEIGHT));
        setResizable(false);

        generateDijkstraGraph();
        setTitle("Cell Segmentation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, null);
                switch (drawingOption) {
                    case 0 -> drawDijkstraGraph(g);
                    case 1 -> drawPlaneGraph(g);
                }
            }
        };
        graphPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        graphPanel.setOpaque(false);
        add(graphPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton generateButton = new JButton("Dijkstra");
        generateButton.addActionListener(e -> {
            drawingOption = 0;
            generateDijkstraGraph();
            repaint();
        });
        buttonPanel.add(generateButton);

        JButton rngButton = new JButton("RNG");
        rngButton.addActionListener(e -> {
            drawingOption = 1;
            generateRNGraph();
            repaint();
        });
        buttonPanel.add(rngButton);

        JButton gabrielButton = new JButton("Gabriel");
        gabrielButton.addActionListener(e -> {
            drawingOption = 1;
            generateGabrielGraph();
            repaint();
        });
        buttonPanel.add(gabrielButton);

        JButton cycleButton = new JButton("Find cycles");
        cycleButton.addActionListener(e -> {
            showCycles();
            graph.findAllCycles();
        });
        buttonPanel.add(cycleButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private void showCycles() {
        // Tworzenie modelu listy
        JFrame listFrame = new JFrame("Cycle list");
        listFrame.setPreferredSize(new Dimension(400, 400));
        listFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        listFrame.setLocationRelativeTo(null);

        // Tworzenie modelu listy
        DefaultListModel<String> model = new DefaultListModel<>();
        List<Cycle> cycles = graph.findAllCycles();
        model.addElement("Found " + cycles.size() + " cycles");
        for (Cycle cycle : cycles) {
            model.addElement(cycle.toString());
        }

        // Tworzenie listy
        JList<String> list = new JList<>(model);

        // Dodawanie listy do panelu z paskiem przewijania
        JScrollPane scrollPane = new JScrollPane(list);

        // Dodawanie panelu do okna
        listFrame.getContentPane().add(scrollPane);

        // Ustawianie rozmiaru i wyświetlanie okna
        listFrame.pack();
        listFrame.setVisible(true);
    }

    private void drawDijkstraGraph(Graphics g) {
        int vertexRadius = 30;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 3;
        double angleStep = 2 * Math.PI / vertices.size();

        for (Vertex vertex : vertices) {
            int id = vertex.getID();
            double angle = id * angleStep;

            int x = (int) (centerX + Math.cos(angle) * radius);
            int y = (int) (centerY + Math.sin(angle) * radius);

            if (vertex.equals(graph.getStartVertex())) g.setColor(Color.GREEN);
            else if (vertex.equals(graph.getEndVertex())) g.setColor(Color.RED);
            else g.setColor(Color.BLUE);

            g.fillOval(x - vertexRadius, y - vertexRadius, 2 * vertexRadius, 2 * vertexRadius);
            g.setColor(Color.BLACK);
            g.drawString(Integer.toString(id), (int) ((x - 5) + Math.cos(angle) * 10), (int) (((y + 5)) + Math.sin(angle) * 10));
        }

        g.setColor(Color.BLACK);

        List<Edge> edges = graph.getEdges();

        for (Edge edge : edges) {
            Vertex source = edge.source();
            Vertex destination = edge.destination();

            double srcAngle = source.getID() * angleStep;
            double destAngle = destination.getID() * angleStep;

            int x1 = (int) (centerX + Math.cos(srcAngle) * radius);
            int y1 = (int) (centerY + Math.sin(srcAngle) * radius);
            int x2 = (int) (centerX + Math.cos(destAngle) * radius);
            int y2 = (int) (centerY + Math.sin(destAngle) * radius);
            g.drawLine(x1, y1, x2, y2);
            g.drawString(Integer.toString(edge.weight()), (x1 + x2) / 2, (y1 + y2) / 2);
        }


        for (int i = 0; i < shortestPath.size() - 1; i++) {
            Vertex source = shortestPath.get(i);
            Vertex destination = shortestPath.get(i + 1);

            int sourceX = (int) (centerX + Math.cos(source.getID() * angleStep) * radius);
            int sourceY = (int) (centerY + Math.sin(source.getID() * angleStep) * radius);
            int destX = (int) (centerX + Math.cos(destination.getID() * angleStep) * radius);
            int destY = (int) (centerY + Math.sin(destination.getID() * angleStep) * radius);

            g.setColor(Color.RED);
            g.drawLine(sourceX, sourceY, destX, destY);
        }
    }

    private void drawPlaneGraph(Graphics g) {
        int vertexRadius = 5;

        for (Vertex vertex : vertices) {
            if (vertex instanceof PlaneVertex) {
                int id = vertex.getID();

                int x = ((PlaneVertex) vertex).getX();
                int y = ((PlaneVertex) vertex).getY();

                if (vertex.equals(graph.getStartVertex())) g.setColor(Color.GREEN);
                else if (vertex.equals(graph.getEndVertex())) g.setColor(Color.RED);
                else g.setColor(Color.LIGHT_GRAY);

                g.fillOval(x - vertexRadius, y - vertexRadius, 2 * vertexRadius, 2 * vertexRadius);
                g.setColor(Color.BLACK);
                g.drawString(Integer.toString(id), x + 5, y + 5);
            }
        }

        g.setColor(Color.BLACK);
        List<Edge> edges = graph.getEdges();

        for (Edge edge : edges) {
            Vertex source = edge.source();
            Vertex destination = edge.destination();

            if (source instanceof PlaneVertex && destination instanceof PlaneVertex) {
                int x1 = ((PlaneVertex) source).getX();
                int y1 = ((PlaneVertex) source).getY();
                int x2 = ((PlaneVertex) destination).getX();
                int y2 = ((PlaneVertex) destination).getY();
                g.drawLine(x1, y1, x2, y2);
                g.drawString(Integer.toString(edge.weight()), (x1 + x2) / 2, (y1 + y2) / 2);
            }
        }

        for (int i = 0; i < shortestPath.size() - 1; i++) {
            Vertex source = shortestPath.get(i);
            Vertex destination = shortestPath.get(i + 1);

            int x1 = ((PlaneVertex) source).getX();
            int y1 = ((PlaneVertex) source).getY();
            int x2 = ((PlaneVertex) destination).getX();
            int y2 = ((PlaneVertex) destination).getY();

            g.setColor(Color.RED);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    private void generateDijkstraGraph() {
        graph = new Graph();
        graph.generateDijkstraGraph();
        vertices = graph.getVertices();
        shortestPath = findShortestPath(graph);
    }

    private void generateRNGraph() {
        graph = new PlaneGraph();
        graph.generateRNGraph();
        vertices = graph.getVertices();
        shortestPath = findShortestPath(graph);
    }

    private void generateGabrielGraph() {
        graph = new PlaneGraph();
        graph.generateGabrielGraph();
        vertices = graph.getVertices();
        shortestPath = findShortestPath(graph);
    }

    private void loadBackgroundImage() {
        try {
            File imageFile = new File("img/input.png");
            backgroundImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

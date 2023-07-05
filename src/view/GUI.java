package view;

import graphs.Point;
import graphs.TissueGraph;
import imageProcessing.ImageProcessing;
import main.Main;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("SpellCheckingInspection")
public class GUI extends JFrame {
    public final static int BUTTON_PANEL_HEIGHT = 50;
    public static int WIDTH = 600;
    public static int HEIGHT = 700;

    private Graph<Point, DefaultEdge> graph;
    private List<Point> vertices;
    private List<List<Point>> cycles;
    private List<Polygon> polygons;
    private int cycleIndex = 0;

    private BufferedImage backgroundImage;

    public GUI() {
        generateGraph();
        convertCyclesToPolygons();

        loadBackgroundImage();
        WIDTH = backgroundImage.getWidth();
        HEIGHT = backgroundImage.getHeight();

        setSize(new Dimension(WIDTH, HEIGHT + BUTTON_PANEL_HEIGHT));

        setTitle("Cell Segmentation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, null);
                drawPlaneGraph(g);
                drawCycle(g);
            }
        };
        graphPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        graphPanel.setOpaque(false);
        add(graphPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton inputButton = new JButton("Graph on input");
        inputButton.addActionListener(e -> {
            loadBackgroundImage();
            repaint();
        });
        buttonPanel.add(inputButton);

        JButton edgeButton = new JButton("Graph on edges");
        edgeButton.addActionListener(e -> {
            loadEdgesImage();
            repaint();
        });
        buttonPanel.add(edgeButton);

        JButton cycleButton = new JButton("List of cycles");
        cycleButton.addActionListener(e -> showCycles());
        buttonPanel.add(cycleButton);

        JButton nextCycleButton = new JButton("Show next cycle");
        nextCycleButton.addActionListener(e -> {
            cycleIndex++;
            repaint();
        });
        buttonPanel.add(nextCycleButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private void showCycles() {
        // Tworzenie modelu listy
        JFrame listFrame = new JFrame("Cycle list");
        listFrame.setPreferredSize(new Dimension(500, 500));
        listFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        listFrame.setLocationRelativeTo(null);

        // Tworzenie modelu listy
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("Found " + cycles.size() + " cycles");
        for (List<Point> list : cycles) {
            StringBuilder builder = new StringBuilder();
            for (Point point : list) {
                builder.append(point.toString());
                builder.append(", ");
            }
            model.addElement(builder.toString());
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

    private void drawPlaneGraph(Graphics g) {
        int vertexRadius = 2;

        for (Point vertex : vertices) {
            int x = vertex.x();
            int y = vertex.y();

            // Vertex color
            g.setColor(Color.GREEN);

            g.fillOval(x - vertexRadius, y - vertexRadius, 2 * vertexRadius, 2 * vertexRadius);
        }

        // Edge color
        g.setColor(Color.RED);
        Set<DefaultEdge> edges = graph.edgeSet();

        for (DefaultEdge edge : edges) {
            Point source = graph.getEdgeSource(edge);
            Point target = graph.getEdgeTarget(edge);

            int x1 = source.x();
            int y1 = source.y();
            int x2 = target.x();
            int y2 = target.y();
            g.drawLine(x1, y1, x2, y2);
        }
    }

    private void drawCycle(Graphics g) {
        g.setColor(new Color(66, 117, 245, 150));
        Polygon polygon = polygons.get(cycleIndex % polygons.size());
        g.drawPolygon(polygon);
        g.fillPolygon(polygon);
    }

    private void loadBackgroundImage() {
        try {
            File imageFile = new File("img/input.png");
            backgroundImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadEdgesImage() {
        try {
            File imageFile = new File("img/edges.png");
            backgroundImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateGraph() {
        this.vertices = ImageProcessing.generateKeypointsList();
        this.graph = TissueGraph.generateRNGraph(vertices);
        this.cycles = TissueGraph.getCyclesPaton(graph);

        System.out.println(vertices.size());
    }

    private void convertCyclesToPolygons() {
        List<Polygon> polygons = new ArrayList<>();

        for (List<Point> cycle : cycles) {
            if (cycle.size() >= Main.MIN_CYCLE_LENGTH && cycle.size() <= Main.MAX_CYCLE_LENGTH) {
                Polygon polygon = new Polygon();
                for (Point point : cycle) {
                    polygon.addPoint(point.x(), point.y());
                }
                polygons.add(polygon);
            }
        }

        this.polygons = polygons;
    }
}
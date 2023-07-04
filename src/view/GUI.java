package view;

import graphs.*;
import imageProcessing.ImageProcessing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GUI extends JFrame {
    public final static int BUTTON_PANEL_HEIGHT = 50;

    public static int WIDTH = 600;
    public static int HEIGHT = 700;
    private PlaneGraph graph;
    private List<PlaneVertex> vertices;

    private BufferedImage backgroundImage;

    public GUI() {
        generateGraph();
        loadBackgroundImage();
        WIDTH = backgroundImage.getWidth();
        HEIGHT = backgroundImage.getHeight();

        setSize(new Dimension(WIDTH, HEIGHT + BUTTON_PANEL_HEIGHT));
//        setResizable(false);

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
        listFrame.setPreferredSize(new Dimension(500, 500));
        listFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        listFrame.setLocationRelativeTo(null);

        // Tworzenie modelu listy
        DefaultListModel<String> model = new DefaultListModel<>();
        List<Cycle> cycles = FastCycleDetection.enumerateCycles(graph);
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

    private void drawPlaneGraph(Graphics g) {
        int vertexRadius = 2;

        for (PlaneVertex vertex : vertices) {
            int x = vertex.getX();
            int y = vertex.getY();

            // Vertex color
            g.setColor(Color.GREEN);

            g.fillOval(x - vertexRadius, y - vertexRadius, 2 * vertexRadius, 2 * vertexRadius);
        }

        // Edge color
        g.setColor(Color.RED);
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
            }
        }
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
        vertices = ImageProcessing.generateKeypointsList();
        System.out.println(vertices.size());
        graph = new PlaneGraph(vertices);
        graph.generateRNGraph();
    }
}

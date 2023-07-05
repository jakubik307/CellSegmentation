package imageProcessing;

import graphs.Point;
import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageProcessing {
    private final static int MAX_LINEPOINTS = 30000;
    private final static int MAX_KEYPOINTS = 0;
    private final static double POINT_SPACING = 10;

    public static void main(String[] args) {
        generateKeypointsList();
    }

    public static List<Point> generateKeypointsList() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // ------------------------ EDGE DETECTION ------------------------

        // Load the input image
        Mat input = Imgcodecs.imread("img/input.png");

        // Convert the image to grayscale
        Mat grayImage = new Mat();
        Imgproc.cvtColor(input, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Apply Canny edge detection
        Mat edges = new Mat();
        Imgproc.Canny(grayImage, edges, 180, 400);

        // Save the marked edges image
        Imgcodecs.imwrite("img/edges.png", edges);

        // ------------------------ LINEPOINTS DETECTION ------------------------

        // Create a linepoints detector
        LinepointsDetector linepointsDetector = new LinepointsDetector(8);

        // Detect linepoints in the image
        MatOfKeyPoint linepoints = new MatOfKeyPoint();
        linepoints.fromList(linepointsDetector.detect(edges));

        // Draw linepoints on the image
        Mat outputImage = new Mat();
        Features2d.drawKeypoints(edges, linepoints, outputImage);

        // Save the marked linepoints image
        Imgcodecs.imwrite("img/linepoints.png", outputImage);

        // ------------------------ KEYPOINTS DETECTION ------------------------

        // Create a detector
        Feature2D detector = AgastFeatureDetector.create();

        // Detect keypoints in the image
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        detector.detect(edges, keypoints);

        // Draw keypoints on the image
        outputImage = new Mat();
        Features2d.drawKeypoints(edges, keypoints, outputImage);

        // Save the marked keypoints image
        Imgcodecs.imwrite("img/keypoints.png", outputImage);

        // ------------------------ KEYPOINTS LIST ------------------------

        List<Point> vertices = new ArrayList<>();
        List<KeyPoint> keyPointsList = keypoints.toList();
        List<KeyPoint> linepointsList = linepoints.toList();
        int keyPointsAdded = 0;
        int linePointsAdded = 0;

        Collections.shuffle(keyPointsList);
        for (KeyPoint keyPoint : keyPointsList) {
            if (keyPointsAdded >= MAX_KEYPOINTS) break;
            vertices.add(new Point((int) keyPoint.pt.x, (int) keyPoint.pt.y));
            keyPointsAdded++;
        }

        Collections.shuffle(linepointsList);
        for (KeyPoint linePoint : linepointsList) {
            if (linePointsAdded >= MAX_LINEPOINTS) break;
            vertices.add(new Point((int) linePoint.pt.x, (int) linePoint.pt.y));
            linePointsAdded++;
        }

        // Remove points that are too close to each other

        for (int i = 0; i < vertices.size(); i++) {
            Point point = vertices.get(i);
            for (int j = i + 1; j < vertices.size(); j++) {
                Point otherPoint = vertices.get(j);
                if (point.distanceTo(otherPoint) < POINT_SPACING) {
                    vertices.remove(j);
                    j--;
                }
            }
        }

        return vertices;
    }
}

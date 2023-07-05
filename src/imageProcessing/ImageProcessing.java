package imageProcessing;

import graphs.Point;
import main.Main;
import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.AgastFeatureDetector;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageProcessing {
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

        //Apply Gaussian blur
        Imgproc.GaussianBlur(grayImage, grayImage, new org.opencv.core.Size(5, 5), 0);

        // Apply Canny edge detection
        Mat edges = new Mat();
        Imgproc.Canny(grayImage, edges, Main.CANNY_THRESHOLD1, Main.CANNY_THRESHOLD2);

        // Save the marked edges image
        Imgcodecs.imwrite("img/edges.png", edges);

        // ------------------------ KEYPOINTS DETECTION ------------------------

        // Create a detector
        Feature2D detector = AgastFeatureDetector.create();

        // Detect keypoints in the image
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        detector.detect(edges, keypoints);

        // Draw keypoints on the image
        Mat outputImage = new Mat();
        Features2d.drawKeypoints(edges, keypoints, outputImage);

        // Save the marked keypoints image
        Imgcodecs.imwrite("img/keypoints.png", outputImage);

        // ------------------------ KEYPOINTS LIST ------------------------

        List<Point> vertices = new ArrayList<>();
        List<KeyPoint> keyPointsList = keypoints.toList();
        int keyPointsAdded = 0;

        Collections.shuffle(keyPointsList);
        for (KeyPoint keyPoint : keyPointsList) {
            if (keyPointsAdded >= Main.MAX_KEYPOINTS) break;
            vertices.add(new Point((int) keyPoint.pt.x, (int) keyPoint.pt.y));
            keyPointsAdded++;
        }

        // Remove points that are too close to each other

        for (int i = 0; i < vertices.size(); i++) {
            Point point = vertices.get(i);
            for (int j = i + 1; j < vertices.size(); j++) {
                Point otherPoint = vertices.get(j);
                if (point.distanceTo(otherPoint) < Main.POINT_SPACING) {
                    vertices.remove(j);
                    j--;
                }
            }
        }

        return vertices;
    }
}

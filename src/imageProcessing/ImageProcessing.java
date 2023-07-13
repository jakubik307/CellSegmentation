package imageProcessing;

import graphs.Point;
import main.Main;
import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
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
        MatOfKeyPoint allKeypoints = new MatOfKeyPoint();
        detector.detect(input, allKeypoints);

        // Draw keypoints on the image
        Mat outputImage = new Mat();
        Features2d.drawKeypoints(edges, allKeypoints, outputImage);

        // Save the marked keypoints image
        Imgcodecs.imwrite("img/allKeypoints.png", outputImage);

        // ------------------------ KEYPOINTS LIST ------------------------

        List<Point> vertices = new ArrayList<>();
        List<KeyPoint> keyPointsList = allKeypoints.toList();
        List<KeyPoint> validKeyPointsList = new ArrayList<>();
        MatOfKeyPoint validKeyPoints = new MatOfKeyPoint();

        // Get the vertices from the keypoints
        for (KeyPoint keyPoint : keyPointsList) {
            if (edges.get((int) keyPoint.pt.y, (int) keyPoint.pt.x)[0] == 255) {
                Point point = new Point((int) keyPoint.pt.x, (int) keyPoint.pt.y);
                if (!vertices.contains(point)) {
                    validKeyPointsList.add(keyPoint);
                    vertices.add(point);
                }
            }
        }

        System.out.println("Number of vertices: " + vertices.size());
        validKeyPoints.fromList(validKeyPointsList);

        // Draw keypoints on the image
        Mat validKeypoints = new Mat();
        Features2d.drawKeypoints(edges, validKeyPoints, validKeypoints);

        // Save the marked keypoints image
        Imgcodecs.imwrite("img/keypoints.png", validKeypoints);

        return vertices;
    }
}

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.FastFeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageProcessing {

    public static void main(String[] args) {
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
        LinepointsDetector linepointsDetector = new LinepointsDetector(20);

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
        FastFeatureDetector detector = FastFeatureDetector.create();

        // Detect keypoints in the image
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        detector.detect(edges, keypoints);

        // Draw keypoints on the image
        outputImage = new Mat();
        Features2d.drawKeypoints(edges, keypoints, outputImage);

        // Save the marked keypoints image
        Imgcodecs.imwrite("img/keypoints.png", outputImage);
    }
}

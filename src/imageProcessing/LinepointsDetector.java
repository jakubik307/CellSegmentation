package imageProcessing;

import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class LinepointsDetector {
    private int threshold;

    public LinepointsDetector() {
        this.threshold = 4;
    }

    public LinepointsDetector(int threshold) throws IllegalArgumentException {
        if (threshold <= 0) throw new IllegalArgumentException("Threshold must be greater than 0");
        this.threshold = threshold;
    }

    public List<KeyPoint> detect(Mat image) {
        ArrayList<KeyPoint> keypoints = new ArrayList<>();
        int width = image.width();
        int height = image.height();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (image.get(j, i)[0] == 255) keypoints.add(new KeyPoint(i, j, 1));
            }
        }

        double sensitivity = 1.0 / threshold;

        keypoints.removeIf(keypoint -> Math.random() > sensitivity);

        return keypoints;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}

package main;

import view.GUI;

import javax.swing.*;

public class Main {

    // PARAMETERS
    public static final int MAX_KEYPOINTS = 10000;
    public static final int CANNY_THRESHOLD1 = 130;
    public static final int CANNY_THRESHOLD2 = 320;
    public static final double POINT_SPACING = 6;
    public static final int MIN_CYCLE_LENGTH = 20;
    public static final int MAX_CYCLE_LENGTH = 40;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}

// AgastFeatureDetector, BRISK, FastFeature,
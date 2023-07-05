package main;

import view.GUI;

import javax.swing.*;

public class Main {

    // PARAMETERS
    public static final int MAX_KEYPOINTS = 20000;
    public static final int CANNY_THRESHOLD1 = 130;
    public static final int CANNY_THRESHOLD2 = 320;
    public static final double POINT_SPACING = 11;
    public static final int MIN_CYCLE_LENGTH = 7;
    public static final int MAX_CYCLE_LENGTH = 30;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}

// TODO: 05/07/2023 add low and high quality edges and points
// AgastFeatureDetector, BRISK, FastFeature,
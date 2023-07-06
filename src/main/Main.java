package main;

import view.GUI;

import javax.swing.*;

public class Main {

    // PARAMETERS
    public static final int MAX_KEYPOINTS = 10_000;
    public static final int CANNY_THRESHOLD1 = 130;
    public static final int CANNY_THRESHOLD2 = 320;
    public static final double POINT_SPACING = 8;
    public static final int MIN_CYCLE_LENGTH = 20;
    public static final int MAX_CYCLE_LENGTH = 40;

    // MAX_KEYPOINTS set on 10_000 does not reduce generated points
    // Canny parameters may change with different images

    // Example p
    // 8, 20, 40
    // 11, 20, 35
    // 15, 8, 30

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
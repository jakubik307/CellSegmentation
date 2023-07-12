package main;

import view.GUI;

import javax.swing.*;

public class Main {

    // PARAMETERS
    public static final int CANNY_THRESHOLD1 = 60;
    public static final int CANNY_THRESHOLD2 = 150;
    public static final int MIN_CYCLE_LENGTH = 1;
    public static final int MAX_CYCLE_LENGTH = 100;

    // MAX_KEYPOINTS set on 10_000 does not reduce number of generated points
    // Canny parameters may change with different images

    // Sample parameters for respectively POINT_SPACING, MIN_CYCLE_LENGTH, MAX_CYCLE_LENGTH
    // 3, 25, 70
    // 8, 20, 40
    // 11, 10, 35
    // 15, 7, 30

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
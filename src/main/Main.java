package main;

import view.GUI;

import javax.swing.*;

public class Main {

    // PARAMETERS
    public static final int CANNY_THRESHOLD1 = 60;
    public static final int CANNY_THRESHOLD2 = 150;
    public static final int MIN_CYCLE_AREA = 1000;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
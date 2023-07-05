import view.GUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}

// TODO: 05/07/2023 extract parameters to main
// TODO: 05/07/2023 add point spacing parameter
// TODO: 05/07/2023 add low and high quality edges and points
// TODO: 05/07/2023 add cycle preview (with cycle length parameter)
// AgastFeatureDetector, BRISK, FastFeature,
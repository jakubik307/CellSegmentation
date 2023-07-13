package graphs;

import java.util.Comparator;

public class ClockwiseComparator implements Comparator<Point> {
    private final Point center;

    public ClockwiseComparator(Point center) {
        this.center = center;
    }

    @Override
    public int compare(Point p1, Point p2) {
        double angle1 = Math.atan2(p1.y() - center.y(), p1.x() - center.x());
        double angle2 = Math.atan2(p2.y() - center.y(), p2.x() - center.x());

        return Double.compare(angle1, angle2);
    }
}

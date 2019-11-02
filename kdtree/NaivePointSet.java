package kdtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Naive nearest neighbor implementation using a linear scan.
 */
public class NaivePointSet implements PointSet {
    private List<Point> point;

    /**
     * Instantiates a new NaivePointSet with the given points.
     * @param points a non-null, non-empty list of points to include
     *               (makes a defensive copy of points, so changes to the list
     *               after construction don't affect the point set)
     */
    public NaivePointSet(List<Point> points) {
        point = new ArrayList();
        for (int i = 0; i < points.size(); i++) {
            point.add(points.get(i));
        }
    }

    /**
     * Returns the point in this set closest to (x, y) in O(N) time,
     * where N is the number of points in this set.
     */
    @Override
    public Point nearest(double x, double y) {
        int nearest = 0;
        double distance = -1.0;
        for (int i = 1; i < point.size(); i++) {
            double thisDist = point.get(i).distanceSquaredTo(x, y);
            if (thisDist < distance) {
                nearest = i;
                distance = thisDist;
            }
        }
        return point.get(nearest);
    }
}

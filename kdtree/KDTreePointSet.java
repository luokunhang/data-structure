package kdtree;

import java.util.List;

public class KDTreePointSet implements PointSet {

    private PointNode root;

    private static class PointNode {
        Point point;
        PointNode less;
        PointNode more;

        PointNode(Point point) {
            this.point = point;
            this.less = this.more = null;
        }
    }

    /**
     * Instantiates a new KDTree with the given points.
     * @param points a non-null, non-empty list of points to include
     *               (makes a defensive copy of points, so changes to the list
     *               after construction don't affect the point set)
     */
    public KDTreePointSet(List<Point> points) {
        for (int i = 0; i < points.size(); i++) {
            root = insert(root, points.get(0), true);
        }
    }

    private PointNode insert(PointNode root, Point toInsert, boolean compareX) {
        if (root == null) {
            root = new PointNode(toInsert);
            return root;
        }
        if (compareX) {
            if (toInsert.x() <= root.point.x()) {
                root.less = insert(root.less, toInsert, false);
            } else {
                root.more = insert(root.more, toInsert, false);
            }
        } else {
            if (toInsert.y() <= root.point.y()) {
                root.less = insert(root.less, toInsert, true);
            } else {
                root.more = insert(root.more, toInsert, true);
            }
        }
        return root;
    }

    /**
     * Returns the point in this set closest to (x, y) in (usually) O(log N) time,
     * where N is the number of points in this set.
     */
    @Override
    public Point nearest(double x, double y) {
        return nearest(root, root.point, x, y, true);
    }

    private Point nearest(PointNode node, Point best, double x, double y, boolean compareX) {
        if (node == null) {
            return best;
        }
        if (node.point.distanceSquaredTo(x, y) < best.distanceSquaredTo(x, y)) {
            best = node.point;
        }
        PointNode good;
        PointNode bad;
        boolean checkBad = false;
        if (compareX) {
            if (x <= node.point.x()) {
                good = node.less;
                bad = node.more;
            } else {
                bad = node.less;
                good = node.more;
            }
            if (y == node.point.y()) {
                checkBad = true;
            }
        } else {
            if (y <= node.point.y()) {
                good = node.less;
                bad = node.more;
            } else {
                bad = node.less;
                good = node.more;
            }
            if (x == node.point.x()) {
                checkBad = true;
            }
        }
        best = nearest(good, best, x, y, !compareX);
        if (checkBad) {
            best = nearest(bad, best, x, y, !compareX);
        }
        return best;
    }
}

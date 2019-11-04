package kdtree;

import java.util.List;

public class KDTreePointSet implements PointSet {
    private PointNode root;
    private double gx;
    private double gy;

    private static class PointNode {
        Point point;
        PointNode less;
        PointNode more;

        PointNode(Point point) {
            this.point = point;
            this.less = null;
            this.more = null;
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
            root = insert(root, new Point(points.get(i).x(), points.get(i).y()), true);
        }
    }

    private PointNode insert(PointNode node, Point toInsert, boolean compareX) {
        if (node == null) {
            node = new PointNode(toInsert);
            return node;
        }
        if (compareX) {
            if (toInsert.x() <= node.point.x()) {
                node.less = insert(node.less, toInsert, false);
            } else {
                node.more = insert(node.more, toInsert, false);
            }
        } else {
            if (toInsert.y() <= node.point.y()) {
                node.less = insert(node.less, toInsert, true);
            } else {
                node.more = insert(node.more, toInsert, true);
            }
        }
        return node;
    }

    /**
     * Returns the point in this set closest to (x, y) in (usually) O(log N) time,
     * where N is the number of points in this set.
     */
    @Override
    public Point nearest(double x, double y) {
        this.gx = x;
        this.gy = y;
        return nearest(root, root.point, true);
    }

    private Point nearest(PointNode node, Point best, boolean compareX) {
        if (node == null) {
            return best;
        }
        if (node.point.distanceSquaredTo(gx, gy) < best.distanceSquaredTo(gx, gy)) {
            best = node.point;
        }
        PointNode good;
        PointNode bad;
        if (compareX) {
            if (gx <= node.point.x()) {
                good = node.less;
                bad = node.more;
            } else {
                bad = node.less;
                good = node.more;
            }
            best = nearest(good, best, false);
            if (best.distanceSquaredTo(gx, gy) > Math.pow(gx - node.point.x(), 2)) {
                best = nearest(bad, best, false);
            }
        } else {
            if (gy <= node.point.y()) {
                good = node.less;
                bad = node.more;

            } else {
                bad = node.less;
                good = node.more;
            }
            best = nearest(good, best, true);
            if (best.distanceSquaredTo(gx, gy) > Math.pow(gy - node.point.y(), 2)) {
                best = nearest(bad, best, true);
            }
        }
        return best;
    }
}

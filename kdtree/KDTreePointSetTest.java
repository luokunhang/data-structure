package kdtree;

import org.junit.Test;
import java.util.*;

public class KDTreePointSetTest {
    @Test
    public void testInserting() {
        int seed = 42;
        Random random = new Random(seed);

        List<Point> points = new ArrayList();
        int ite = 100000;
        for (int i = 0; i < 5; i++) {
            Point curr = new Point(random.nextDouble(), random.nextDouble());
            points.add(curr);
        }
        PointSet naiveSet = new NaivePointSet(points);
        PointSet KDSet = new KDTreePointSet(points);

        for(int i = 0; i < ite; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            Point naiveFind = naiveSet.nearest(x, y);
            Point KDFind = KDSet.nearest(x, y);
            if (!naiveFind.equals(KDFind)) {
                System.out.println("Error occurs at " + i);
            }
        }
    }
}

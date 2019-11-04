package kdtree;

import org.junit.Test;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class KDTreePointSetTest {
    @Test
    public void testInserting() {
        int seed = 373;
        Random random = new Random(seed);

        List<Point> points = new ArrayList();
        int ite = 5000;
        for (int i = 0; i < 100000; i++) {
            Point curr = new Point(random.nextDouble(), random.nextDouble());
            points.add(curr);
        }
        PointSet naiveSet = new NaivePointSet(points);
        PointSet kdSet = new KDTreePointSet(points);

        for (int i = 0; i < ite; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            Point naiveFind = naiveSet.nearest(x, y);
            Point kdFind = kdSet.nearest(x, y);
            assertTrue("Error occurs at " + i, naiveFind.equals(kdFind));
        }
    }
}

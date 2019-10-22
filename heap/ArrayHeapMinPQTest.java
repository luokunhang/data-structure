package heap;

import deques.ArrayDeque;
import deques.LinkedDeque;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ArrayHeapMinPQTest {
    @Test
    public void testAddAndRemoveSmallest() {
        // Random seed ensures that each test run is reproducible
        int seed = 373; // or your favorite number
        Random random = new Random(seed);

        // Create a new ArrayDeque (reference implementation)
        ExtrinsicMinPQ<Integer> expectedPQ = new NaiveMinPQ<>();
        // Create a new LinkedDeque (testing implementation)
        ExtrinsicMinPQ<Integer> testingPQ = new ArrayHeapMinPQ<>();

        int ite = 50000;
        // Add 1000000 random integers to both the expectedDeque and testingDeque implementations
        for (int i = 0; i < ite; i += 1) {
            double p = random.nextDouble();
            expectedPQ.add(i, p);
            testingPQ.add(i, p);
        }

        for (int i = 0; i < ite; i += 1) {
            double p = random.nextDouble();
            expectedPQ.changePriority(i, p);
            testingPQ.changePriority(i, p);
        }

        // Check that testingDeque matches expectedDeque on all 1000000 integers
        for (int i = 0; i < ite; i += 1) {
            int expectedValue = expectedPQ.removeSmallest();
            int testingValue = testingPQ.removeSmallest();
            assertEquals("Failed on iteration " + i, expectedValue, testingValue);
            // To debug a particular iteration, set a conditional breakpoint
            // Add a regular breakpoint but right click and set a condition like i == 57893
        }
    }
}

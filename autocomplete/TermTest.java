package autocomplete;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TermTest {
    @Test
    public void testSimpleCompareTo() {
        Term a = new Term("autocomplete", 0);
        Term b = new Term("me", 0);
        assertTrue(a.compareTo(b) < 0); // "autocomplete" < "me"
    }

    // Write more unit tests below.
    @Test
    public void testHardCompareTo() {
        Term a = new Term("Hai", 0);
        Term b = new Term("hai", 0);
        assertTrue(a.compareTo(b) < 0);

        a = new Term("hai", 0);
        assertTrue(a.compareTo(b) == 0);

        a = new Term("haiyu", 0);
        assertTrue(a.compareTo(b) > 0);
    }

    @Test
    public void testCompareWeight() {
        Term a = new Term("calm", 0);
        Term b = new Term("calm", 1);
        assertTrue(a.compareToByReverseWeightOrder(b) > 0);
    }
}

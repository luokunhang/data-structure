package autocomplete;

import java.util.Arrays;

public class BinaryRangeSearch implements Autocomplete {
    private Term[] store;

    /**
     * Validates and stores the given array of terms.
     * Assumes that the given array will not be used externally afterwards (and thus may directly
     * store and mutate it).
     * @throws IllegalArgumentException if terms is null or contains null
     */
    public BinaryRangeSearch(Term[] terms) {
        store = terms;
        if (terms  == null) {
            throw new IllegalArgumentException("The array is null.");
        }
        for (int i = 0; i < terms.length; i++) {
            if (terms[i] == null) {
                throw new IllegalArgumentException("Element" + i + "is null.");
            }
        }
        Arrays.sort(store);
    }

    /**
     * Returns all terms that start with the given prefix, in descending order of weight.
     * @throws IllegalArgumentException if prefix is null
     */
    public Term[] allMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Your prefix is null.");
        }
        int first = binaryFirst(prefix, 0, store.length);
        int last = binaryLast(prefix, 0, store.length);
        Term[] toReturn = new Term[last - first + 1];
        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = store[first + i];
        }
        Arrays.sort(toReturn, TermComparators.byReverseWeightOrder());
        return toReturn;
    }

    private int binaryFirst(String p, int s, int e) {
        int n = (s + e) / 2;
        if (store[n].queryPrefix(p.length()).compareTo(p) == 0 && (n == 0 || store[n - 1].queryPrefix(p.length()).compareTo(p) < 0)) {
            return n;
        } else if (store[n].queryPrefix(p.length()).compareTo(p) > 0) {
            return binaryFirst(p, s, n);
        } else {
            return binaryFirst(p, n, e);
        }
    }

    private int binaryLast(String p, int s, int e) {
        int n = (s + e) / 2;
        if (store[n].queryPrefix(p.length()).compareTo(p) == 0 && (n == store.length || store[n + 1].queryPrefix(p.length()).compareTo(p) > 0)) {
            return n;
        } else if (store[n].queryPrefix(p.length()).compareTo(p) > 0) {
            return binaryLast(p, s, n);
        } else {
            return binaryLast(p, n, e);
        }
    }
}

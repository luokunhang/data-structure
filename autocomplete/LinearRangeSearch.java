package autocomplete;

import javax.xml.transform.TransformerFactoryConfigurationError;
import java.util.*;

public class LinearRangeSearch implements Autocomplete {
    private Set<Term> store;

    /**
     * Validates and stores the given array of terms.
     * Assumes that the given array will not be used externally afterwards (and thus may directly
     * store and mutate it).
     * @throws IllegalArgumentException if terms is null or contains null
     */
    public LinearRangeSearch(Term[] terms) {
        if (terms  == null) {
            throw new IllegalArgumentException("The array is null.");
        }
        for (int i = 0; i < terms.length; i++) {
            if (terms[i] == null) {
                throw new IllegalArgumentException("Element" + i + "is null.");
            }
        }
        store = new HashSet<Term>();
        for (Term term: terms) {
            store.add(term);
        }
    }

    /**
     * Returns all terms that start with the given prefix, in descending order of weight.
     * @throws IllegalArgumentException if prefix is null
     */
    public Term[] allMatches(String prefix) {
        LinkedList<Term> qualified = new LinkedList<Term>();
        if (prefix == null) {
            throw new IllegalArgumentException("Your prefix is null.");
        }
        for (Term term: store) {
            if (prefix.length() <= term.query().length() && prefix.equals(term.query().substring(0, prefix.length()))) {
                qualified.add(term);
            }
        }
        qualified.sort(TermComparators.byReverseWeightOrder());
        Term[] toReturn = new Term[qualified.size()];
        for (int i = 0; i < qualified.size(); i++) {
            toReturn[i] = qualified.get(i);
        }
        return toReturn;
    }
}


package autocomplete;

public class Term implements Comparable<Term> {
    private String query;
    private long weight;
    /**
     * Initializes a term with the given query string and weight.
     * @throws IllegalArgumentException if query is null or weight is negative
     */
    public Term(String query, long weight) {
        if (query == null || weight < 0) {
            throw new IllegalArgumentException("Query is null or weight is negative!");
        }
        this.query = query;
        this.weight = weight;
    }

    /**
     * Compares the two terms in lexicographic order by query.
     * @throws NullPointerException if the specified object is null
     */
    public int compareTo(Term that) {
        if (that == null) {
            throw new NullPointerException("Term passed is null!");
        }
        return this.query().compareTo(that.query());
    }

    /** Compares to another term, in descending order by weight. */
    public int compareToByReverseWeightOrder(Term that) {
        if (that == null) {
            throw new NullPointerException("Term passed is null!");
        }
        return (int) (that.weight() - this.weight());
    }

    /**
     * Compares to another term in lexicographic order, but using only the first r characters
     * of each query. If r is greater than the length of any term's query, compares using the
     * term's full query.
     * @throws IllegalArgumentException if r < 0
     */
    public int compareToByPrefixOrder(Term that, int r) {
        if (that == null) {
            throw new NullPointerException("Term passed is null!");
        }
        if (r < 0) {
            throw new IllegalArgumentException("Length is negative.");
        }
        if (r > this.query().length() || r > that.query().length()) {
            return compareTo(that);
        } else {
            return this.query().substring(0, r).compareTo(that.query().substring(0, r));
        }
    }

    /** Returns this term's query. */
    public String query() {
        return query;
    }

    /**
     * Returns the first r characters of this query.
     * If r is greater than the length of the query, returns the entire query.
     * @throws IllegalArgumentException if r < 0
     */
    public String queryPrefix(int r) {
        if (r < 0) {
            throw new IllegalArgumentException("Length is negative.");
        }
        if (r >= query.length()) {
            return query;
        } else {
            return query.substring(0, r);
        }
    }

    /** Returns this term's weight. */
    public long weight() {
        return weight;
    }
}

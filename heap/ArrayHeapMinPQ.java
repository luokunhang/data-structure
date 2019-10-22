package heap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private ArrayList<PriorityNode> items;
    private HashMap<T, Integer> quick;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        items.add(null);
        quick = new HashMap<>();
    }

    /*
    Here's a helper method and a method stub that may be useful. Feel free to change or remove
    them, if you wish.
     */

    /**
     * A helper method to create arrays of T, in case you're using an array to represent your heap.
     * You shouldn't need this if you're using an ArrayList instead.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArray(int newCapacity) {
        return (T[]) new Object[newCapacity];
    }

    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        quick.replace(items.get(a).getItem(), a, b);
        quick.replace(items.get(b).getItem(), b, a);
        PriorityNode temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
    }

    /**
     * Adds an item with the given priority value.
     * Assumes that item is never null.
     * Runs in O(log N) time (except when resizing).
     * @throws IllegalArgumentException if item is already present in the PQ
     */
    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already exists.");
        }
        items.add(new PriorityNode(item, priority));
        quick.put(item, size());
        swim(size());
    }

    private void swim(int n) {
        if (n > 1 && items.get(n/2).getPriority() > items.get(n).getPriority()) {
            swap(n, n/2);
            swim(n/2);
        }
    }

    /**
     * Returns true if the PQ contains the given item; false otherwise.
     * Runs in O(log N) time.
     */
    @Override
    public boolean contains(T item) {
        return quick.containsKey(item);
    }

    /**
     * Returns the item with the smallest priority.
     * Runs in O(log N) time.
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T getSmallest() {
        if (items.isEmpty()) {
            throw new NoSuchElementException("PQ is empty.");
        }
        return items.get(1).getItem();
    }

    /**
     * Removes and returns the item with the smallest priority.
     * Runs in O(log N) time (except when resizing).
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T removeSmallest() {
        if (items.isEmpty()) {
            throw new NoSuchElementException("PQ is empty.");
        }
        quick.remove(items.get(1).getItem());
        swap(1, size());
        T keep = items.remove(size()).getItem();
        sink(1);
        return keep;
    }

    private void sink(int n) {
        if (n * 2 + 1 <= size()) {
            if (items.get(n).getPriority() > items.get(n * 2).getPriority() &&
                    items.get(n).getPriority() > items.get(n * 2 + 1).getPriority()) {
                if (items.get(n * 2 + 1).getPriority() < items.get(n * 2).getPriority()) {
                    swap(n, n * 2 + 1);
                } else {
                    swap(n, n * 2);
                }
            } else if (items.get(n).getPriority() > items.get(n * 2).getPriority()) {
                swap(n, n * 2);
            } else if (items.get(n).getPriority() > items.get(n * 2 + 1).getPriority()) {
                swap(n, n * 2 + 1);
            }
        } else if (n * 2 <= size()) {
            if (items.get(n).getPriority() > items.get(n * 2).getPriority()) {
                swap(n, n * 2);
            }
        }
    }

    /**
     * Changes the priority of the given item.
     * Runs in O(log N) time.
     * @throws NoSuchElementException if the item is not present in the PQ
     */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("Not an item in PQ.");
        }
        int currInd = quick.get(item);
        items.get(currInd).priority = priority;
        sink(currInd);
        currInd = quick.get(item);
        swim(currInd);
    }

    /**
     * Returns the number of items in the PQ.
     * Runs in O(log N) time.
     */
    @Override
    public int size() {
        return items.size() - 1;
    }

    private class PriorityNode implements Comparable<ArrayHeapMinPQ.PriorityNode> {
        private T item;
        private double priority;

        PriorityNode(T e, double p) {
            this.item = e;
            this.priority = p;
        }

        T getItem() {
            return item;
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double priority) {
            this.priority = priority;
        }

        @Override
        public int compareTo(ArrayHeapMinPQ.PriorityNode other) {
            if (other == null) {
                return -1;
            }
            return Double.compare(this.getPriority(), other.getPriority());
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || o.getClass() != this.getClass()) {
                return false;
            } else {
                return ((ArrayHeapMinPQ.PriorityNode) o).getItem().equals(getItem());
            }
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }
    }
}

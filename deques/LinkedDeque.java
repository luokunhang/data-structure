package deques;

public class LinkedDeque<T> implements Deque<T> {
    private int size;
    private Node sentinel;

    public LinkedDeque() {
        sentinel = new Node(null);
        size = 0;
    }

    public void addFirst(T item) {
        size += 1;
        Node second = sentinel.next;
        Node add = new Node(item);
        sentinel.next = add;
        add.next = second;
        add.prev = sentinel;
    }

    public void addLast(T item) {
        size += 1;
        Node secLast = sentinel.prev;
        Node add = new Node(item);
        sentinel.prev = add;
        add.next = sentinel;
        add.prev = secLast;
        secLast.next = add;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T rmd = sentinel.next.value;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        return rmd;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T rmd = sentinel.prev.value;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        return rmd;
    }

    public T get(int index) {
        if ((index > size) || (index < 0)) {
            return null;
        }
        Node curr = sentinel;
        while (index >= 0) {
            curr = curr.next;
            index -= 1;
        }
        return curr.value;
    }

    private class Node {
        private T value;
        private Node next;
        private Node prev;

        Node(T value) {
            this.value = value;
            this.next = this;
            this.prev = this;
        }
    }

    public int size() {
        return size;
    }
}

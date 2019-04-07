import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int N;

    class Node {
        Item item;
        Node next;
        Node previous;
    }

    class DequeIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() { return current != null; }
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException("No more elements");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
        public void remove() {
            throw new UnsupportedOperationException("remve() is not supported");
        }
    }

    public Deque() {
        first = null;
        last = null;
        N = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return N;
    }

    public void addFirst(Item item) {
        validateItem(item);
        Node newNode = new Node();
        newNode.item = item;

        if (isEmpty()) {
            first = newNode;
            last = newNode;
        } else {
            newNode.next = first;
            first.previous = newNode;
            first = newNode;
        }
        N++;
    }

    public void addLast(Item item) {
        validateItem(item);
        Node newNode = new Node();
        newNode.item = item;

        if (isEmpty()) {
            first = newNode;
            last = newNode;
        } else {
            last.next = newNode;
            newNode.previous = last;
            last = newNode;
        }
        N++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }

        Item item = first.item;

        if (N == 1) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.previous = null;
        }
        N--;
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                    "The deque is empty."
            );
        }
        Item item = last.item;

        if (N == 1) {
            first = null;
            last = null;
        } else {
            last = last.previous;
            last.next = null;
        }
        N--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private Item getFirstItem() {
        return first == null ? null : first.item;
    }

    private Item getLastItem() {
        return last == null ? null : last.item;
    }

    private void validateItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "Item to be inserted cannot be null");
        }
    }
}
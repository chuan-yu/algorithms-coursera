import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int N;

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int current;
        private Item[] shuffledArray;

        public RandomizedQueueIterator() {
            StdRandom.shuffle(items, 0, N);
            shuffledArray = (Item[]) new Object[N];

            for (int i = 0; i < N; i++) {
                shuffledArray[i] = items[i];
            }
            current = 0;
        }

        public boolean hasNext() {
            return current < N;
        }

        public Item next() {
            if (current == N) {
                throw new NoSuchElementException("No more item");
            }
            Item item = shuffledArray[current];
            current++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove operation is not supported");
        }
    }

    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        N = 0;
    }

    public boolean isEmpty() {
        return (size() == 0);
    }

    public int size() {
        return N;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "Item to be enqueued can't be null");
        }
        if (N == items.length) resize(2 * items.length);
        items[N++] = item;
    }

    public Item dequeue() {
        if (N == 0) {
            throw new NoSuchElementException("The randomized queue is empty");
        }
        int randomIndex = StdRandom.uniform(N);
        Item item = items[randomIndex];
        items[randomIndex] = items[--N];
        items[N] = null;
        if (N > 0 && N == items.length / 4) {
            resize(items.length / 2);
        }
        return item;
    }

    public Item sample() {
        if (N == 0) {
            throw new NoSuchElementException("The randomized queue is empty");
        }
        int randomIndex = StdRandom.uniform(N);
        return items[randomIndex];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void resize(int capacity) {
        if (capacity < N) {
            throw new IllegalArgumentException(
                    "The capacity has to be greater than N."
            );
        }
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            copy[i] = items[i];
        }
        items = copy;
    }

    public static void main(String[] args) {

    }
}

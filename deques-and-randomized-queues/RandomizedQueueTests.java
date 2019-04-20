import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class RandomizedQueueTests {
    private RandomizedQueue<Integer> randomizedQueue;

    @BeforeEach
    void createRandomizedQueue() {
        randomizedQueue = new RandomizedQueue<>();
    }

    @Test
    void initShouldCreateEmptyQueue() {
        assertTrue(randomizedQueue.isEmpty());
    }

    @Test
    void enqueueNullShouldThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> randomizedQueue.enqueue(null));
    }

    @Test
    void enqueueShouldSucceedAndArraySizeShouldDoubleWhenFull() {
        for (int i = 0; i < 16; i++) {
            randomizedQueue.enqueue(i);
            Integer[] items = getItems();
            if (items == null) {
                throw new NullPointerException("items can't be null");
            }
            assertNotNull(items);
            if (i == 0) {
                assertEquals(items.length, 1);
            } else if (i ==1) {
                assertEquals(items.length, 2);
            } else if (i == 2) {
                assertEquals(items.length, 4);
            } else if (i < 4) {
                assertEquals(items.length, 4);
            } else if (i < 8) {
                assertEquals(items.length, 8);
            } else if (i < 16) {
                assertEquals(items.length, 16);
            }
            assertEquals(items[i], i);
        }
    }

    @Test
    void dequeFromEmptyQueueShouldThrow() {
        assertThrows(NoSuchElementException.class, () ->  randomizedQueue.dequeue());
    }

    @Test
    void dequeShouldSucceed() {
        final int M = 16;
        for (int i = 0; i < M; i++) {
            randomizedQueue.enqueue(i);
        }

        for (int i = 0; i < M; i++) {
            int item = randomizedQueue.dequeue();
            assertNotNull(item);
            assertEquals(randomizedQueue.size(), M - i - 1);
        }
    }

    @Test
    void sampleFromEmptyQueueShouldThrow() {
        assertThrows(NoSuchElementException.class, () -> randomizedQueue.sample());
    }

    @Test
    void sampleFromQueueShouldSucceed() {
        final int M = 16;
        for (int i = 0; i < M; i++) {
            randomizedQueue.enqueue(i);
        }

        for (int i = 0; i < M; i++) {
            Integer item = randomizedQueue.sample();
            assertNotNull(item);
        }
    }

    @Test
    void iteratorOverQueueShouldSucceed() {
        final int M = 500;
        HashSet<Integer> queuedInts = new HashSet<>();
        for (int i = 0; i < M; i++) {
            randomizedQueue.enqueue(i);
            queuedInts.add(i);
        }

        int n = 0;
        for (Integer item: randomizedQueue) {
            n++;
            assertNotNull(item);
            if (!queuedInts.remove(item)) {
                fail("Iterator returned the same item more than once.");
            }
        }
        assertEquals(M, n);
    }

    private Integer[] getItems() {
        Integer[] items = null;
        try {
            Field itemsField = randomizedQueue.getClass().getDeclaredField("items");
            itemsField.setAccessible(true);
            Object array = itemsField.get(randomizedQueue);
            int length = Array.getLength(array);
            items = new Integer[length];
            for (int i = 0; i < length; i++) {
                Object item = Array.get(array, i);
                if (item == null) {
                    items[i] = null;
                } else {
                    items[i] = (int) item;
                }
            }
        } catch (Exception e) {
            System.err.println("error: " + e.toString());
        }

        return items;
    }
}

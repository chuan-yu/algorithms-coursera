import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DequeTests {
    private Deque<String> deque;

    @BeforeEach
    public void createDeque() {
        deque = new Deque<>();
    }

    @Test
    void createDequeShouldSucceed() {
        assertNotNull(deque);
    }

    @Test
    void newlyCreatedDequeShouldBeEmpty() {
        assertTrue(deque.isEmpty());
        assertEquals(deque.size(), 0);
    }

    @Test
    void addFirstNullShouldThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> deque.addFirst(null));
    }

    @Test
    void addFirstToEmptyDequeShouldSucceed() {
        String item = "test string";
        deque.addFirst(item);
        assertEquals(deque.size(), 1);
        String firstItem = getFirst(deque);
        String lastItem = getLast(deque);
        assertEquals(firstItem, item);
        assertEquals(lastItem, item);
    }

    @Test
    void addFirstMultipleItemsShouldSucceed() {
        final int N = 10;
        for (int i = 0; i < N; i++) {
            deque.addFirst(Integer.toString(i));
        }
        assertEquals(deque.size(), N);
        String firstItem = getFirst(deque);
        assertEquals(firstItem, Integer.toString(N-1));
        String lastItem = getLast(deque);
        assertEquals(lastItem, "0");
    }

    @Test
    void addLastNullShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> deque.addLast(null));
    }

    @Test
    void addLastToEmptyDequeShouldSucceed() {
        String item = "test string";
        deque.addLast(item);
        assertEquals(deque.size(), 1);
        String firstItem = getFirst(deque);
        String lastItem = getLast(deque);
        assertEquals(firstItem, item);
        assertEquals(lastItem, item);
    }

    @Test
    void addLastMultipleItemsShouldSucceed() {
        final int N = 10;
        for (int i = 0; i < N; i++) {
            deque.addLast(Integer.toString(i));
        }
        assertEquals(deque.size(), N);
        assertEquals(getFirst(deque), "0");
        assertEquals(getLast(deque), Integer.toString(N - 1));
    }

    @Test
    void removeFirstFromEmptyDequeShouldThrow() {
        assertThrows(NoSuchElementException.class,
                () -> deque.removeFirst());
    }

    @Test
    void removeFirstFromDequeShouldSucceed() {
        final int N = 5;
        for (int i = 0; i < N; i++) {
            deque.addFirst(Integer.toString(i));
        }

        for (int i = N-1; i >= 0; i--) {
            String item = deque.removeFirst();
            assertEquals(item, Integer.toString(i));
            assertEquals(deque.size(), i);
        }

        assertNull(getFirst(deque));
        assertNull(getLast(deque));
    }

    @Test
    void removeLastFromEmptyDequeShouldThrow() {
        assertThrows(NoSuchElementException.class,
                () -> deque.removeFirst());
    }

    @Test
    void removeLastFromDequeShouldSucceed() {
        final int N = 5;
        for (int i = 0; i < N; i++) {
            deque.addLast(Integer.toString(i));
        }

        for (int i = N-1; i >= 0; i--) {
            String item = deque.removeLast();
            assertEquals(item, Integer.toString(i));
            assertEquals(deque.size(), i);
        }

        assertNull(getFirst(deque));
        assertNull(getLast(deque));
    }

    @Test
    void addFirstThenRemoveLastShouldSucceed() {
        final int N = 5;
        for (int i = 0; i < N; i++) {
            deque.addFirst(Integer.toString(i));
        }

        for (int i = 0; i < N; i++) {
            String item = deque.removeLast();
            assertEquals(item, Integer.toString(i));
            assertEquals(deque.size(), N - i - 1 );
        }

        assertNull(getFirst(deque));
        assertNull(getLast(deque));
    }

    @Test
    void addLastThenRemoveFirstShouldSucceed() {
        final int N = 5;
        for (int i = 0; i < N; i++) {
            deque.addLast(Integer.toString(i));
        }

        for (int i = 0; i < N; i++) {
            String item = deque.removeFirst();
            assertEquals(item, Integer.toString(i));
            assertEquals(deque.size(), N - i - 1 );
        }

        assertNull(getFirst(deque));
        assertNull(getLast(deque));
    }

    @Test
    void iteratorOverDequeShouldSucceed() {
        for (int i = 0; i < 5; i++) {
            deque.addLast(Integer.toString(i));
        }
        int n = 0;
        for (String s: deque) {
            assertEquals(s, Integer.toString(n));
            n++;
        }
    }

    @Test
    void removeFromDequeShouldThrow() {
        deque.addFirst("test");
        Iterator<String> itr = deque.iterator();
        assertThrows(UnsupportedOperationException.class, () -> itr.remove());
    }

    private String getFirst(Deque deque) {
        String firstItem = null;
        try {
            Method getFirstItem = deque.getClass().getDeclaredMethod("getFirstItem");
            getFirstItem.setAccessible(true);
            firstItem = (String) getFirstItem.invoke(deque);
        } catch (Exception e) {
            System.err.println(e.toString());
            fail("Should not throw");
        }
        return firstItem;
    }

    private String getLast(Deque deque) {
        String lastItem = null;
        try {
            Method getLastItem = deque.getClass().getDeclaredMethod("getLastItem");
            getLastItem.setAccessible(true);
            lastItem = (String) getLastItem.invoke(deque);
        } catch (Exception e) {
            System.err.println(e.toString());
            fail("Should not throw");
        }
        return lastItem;
    }
}
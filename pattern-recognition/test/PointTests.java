import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PointTests {

    @Test
    void testCompareTo() {
        Point point0 = new Point(1, 1);
        assertEquals(0, point0.compareTo(new Point(1, 1)));
        assertEquals(-1, point0.compareTo(new Point(1, 2)));
        assertEquals(-1, point0.compareTo(new Point(2, 1)));
        assertEquals(1, point0.compareTo(new Point(1, 0)));
        assertEquals(1, point0.compareTo(new Point(0, 1)));
    }

    @Test
    void testSlopeTo() {
        Point point0 = new Point(1, 1);
        Point point1 = new Point(3, 1);
        Point point2 = new Point(1, 3);
        Point point3 = new Point(3, 7);
        Point point4 = new Point(-2, 7);
        Point point5 = new Point(-3, -7);
        Point point6 = new Point(3, -7);

        double slope0 = point0.slopeTo(point0);
        assertEquals(Double.NEGATIVE_INFINITY, slope0);

        double slope1 = point0.slopeTo(point1);
        assertEquals(0, slope1);

        double slope2 = point0.slopeTo(point2);
        assertEquals(Double.POSITIVE_INFINITY, slope2);

        double slope3 = point0.slopeTo(point3);
        assertEquals(3, slope3);

        double slope4 = point0.slopeTo(point4);
        assertEquals(-2, slope4);

        double slope5 = point0.slopeTo(point5);
        assertEquals(2, slope5);

        double slope6 = point0.slopeTo(point6);
        assertEquals(-4, slope6);
    }

    @Test
    void testSlopeOrder() {
        Point point0 = new Point(1,1 );
        assertEquals(-1,
                point0.slopeOrder().compare(
                        new Point(2, 1),
                        new Point(2, 2)));
        assertEquals(-1,
                point0.slopeOrder().compare(
                        new Point(-2, 2),
                        new Point(2, 2)
                ));

        assertEquals(1,
                point0.slopeOrder().compare(
                        new Point(1, 2),
                        new Point(2, 2)));

        assertEquals(0,
                point0.slopeOrder().compare(
                        new Point(2, 2),
                        new Point(2, 2)));
    }
}

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> points = new SET<>();

    public PointSET() {
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        checkArgument(p);
        points.add(p);
    }

    public boolean contains(Point2D p) {
        checkArgument(p);
        return points.contains(p);
    }

    public void draw() {
        for (Point2D p: points) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        checkArgument(rect);
        ArrayList<Point2D> pointsInRange = new ArrayList<>();
        for (Point2D p: points) {
            if (rect.contains(p)) pointsInRange.add(p);
        }
        return pointsInRange;
    }

    public Point2D nearest(Point2D p) {
        checkArgument(p);
        if (isEmpty()) return null;

        Point2D pointNearest = null;
        double minDistance = Double.MAX_VALUE;
        for (Point2D pointInSet : points) {
            double distance = p.distanceTo(pointInSet);
            if (distance < minDistance) {
                minDistance = distance;
                pointNearest = pointInSet;
            }
        }
        return pointNearest;
    }

    private void checkArgument(Object arg) {
        if (arg == null) throw new IllegalArgumentException("Argument is null");
    }

}

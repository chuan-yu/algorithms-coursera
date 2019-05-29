import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private static final boolean VERTICAL = true;

    private Node root;
    private int n = 0;
    private double shortestDistance;

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;

        public Node(Point2D p, RectHV rect, Node lb, Node rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }

        public Point2D p() {
            return p;
        }

        public RectHV rect() {
            return rect;
        }

        @Override
        public String toString() {
            return p.x() + "," + p.y() + " "
                    + rect.xmin() + "," + rect.ymin() + ","
                    + rect.xmax() + "," + rect.ymax();
        }
    }

    public KdTree() {
    }

    public boolean isEmpty() {
        return (root == null);
    }

    public int size() {
        return n;
    }

    public void insert(Point2D p) {
        checkArgument(p);
        root = insert(root, null, p, VERTICAL, false);
    }

    private Node insert(Node x, Node parent, Point2D p, boolean isVertical, boolean isLeftChild) {
        boolean isParentVertical = !isVertical;
        if (x == null) {
            n += 1;
            RectHV rect = createRectangle(parent, isParentVertical, isLeftChild);
            return new Node(p, rect, null, null);
        }
        int cmp = comparePoints(p, x.p(), isVertical);
        if (cmp < 0)
            x.lb = insert(x.lb, x, p, !isVertical, true);
        else if (cmp > 0)
            x.rt = insert(x.rt, x, p, !isVertical, false);
        else {
            if (!x.p().equals(p)) x.rt = insert(x.rt, x, p, !isVertical, false);
        }
        return x;
    }

    private RectHV createRectangle(Node parent, boolean isVertical, boolean isLeftChild) {
        if (parent == null) return new RectHV(0, 0, 1, 1);
        if (isVertical) {
            if (isLeftChild) return new RectHV(parent.rect().xmin(), parent.rect().ymin(),
                    parent.p().x(), parent.rect().ymax());
            return new RectHV(parent.p().x(), parent.rect().ymin(),
                    parent.rect().xmax(), parent.rect().ymax());
        } else {
            if (isLeftChild) return new RectHV(parent.rect().xmin(), parent.rect.ymin(),
                    parent.rect().xmax(), parent.p().y());
            return new RectHV(parent.rect().xmin(), parent.p().y(),
                    parent.rect().xmax(), parent.rect().ymax());
        }
    }

    public boolean contains(Point2D p) {
        checkArgument(p);
        return (get(p) != null);
    }

    private Node get(Point2D p) {
        Node x = root;
        boolean isVertical = true;
        while (x != null) {
            int cmp = comparePoints(p, x.p(), isVertical);
            if (cmp < 0) x = x.lb;
            else if (cmp > 0) x = x.rt;
            else {
                if (x.p().equals(p)) return x;
                else x = x.rt;
            }
            isVertical = !isVertical;
        }
        return null;
    }

    public void draw() {
        StdDraw.square(0.5, 0.5, 0.5);
        draw(root, VERTICAL);
        StdDraw.show();
    }

    private void draw(Node x, boolean isVertical) {
        if (x == null) return;
        draw(x.lb, !isVertical);
        drawNode(x, isVertical);
        draw(x.rt, !isVertical);
    }

    private void drawLine(double x0, double y0,
                          double x1, double y1,
                          Color color) {
        StdDraw.setPenRadius();
        StdDraw.setPenColor(color);
        StdDraw.line(x0, y0, x1, y1);
    }

    private void drawPoint(double x, double y) {
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(x, y);
    }

    private void drawNode(Node x, boolean isVertical) {
        drawPoint(x.p().x(), x.p().y());
        if (isVertical)
            drawLine(x.p().x(), x.rect().ymin(),
                    x.p().x(), x.rect().ymax(),
                    StdDraw.RED);
        else
            drawLine(x.rect().xmin(), x.p().y(),
                    x.rect().xmax(), x.p().y(),
                    StdDraw.BLUE);
    }

    public Iterable<Point2D> range(RectHV rect) {
        checkArgument(rect);
        List<Point2D> pointsInRange = new ArrayList<>();
        range(root, rect, pointsInRange);
        return pointsInRange;
    }

    private void range(Node x, RectHV rect, List<Point2D> pointsInRange) {
        if (x == null || !rect.intersects(x.rect())) return;
        if (rect.contains(x.p())) pointsInRange.add(x.p());
        range(x.lb, rect, pointsInRange);
        range(x.rt, rect, pointsInRange);
    }

    public Point2D nearest(Point2D p) {
        checkArgument(p);
        if (isEmpty()) return null;
        shortestDistance = Double.POSITIVE_INFINITY;
        return nearest(root, p);
    }

    private Point2D nearest(Node x, Point2D p) {
        if (x == null || x.rect().distanceSquaredTo(p) >= shortestDistance) return null;
        double distance = distanceBetween(p, x.p());
        if (distance < shortestDistance) {
            shortestDistance = distance;
        }
        Point2D nearestLeft;
        Point2D nearestRight;
        boolean goLeft = true;
        if (x.lb != null && x.rt != null)
            goLeft = x.lb.rect().distanceSquaredTo(p) <= x.rt.rect().distanceSquaredTo(p);

        if (goLeft) {
            nearestLeft = nearest(x.lb, p);
            nearestRight = nearest(x.rt, p);
        } else {
            nearestRight = nearest(x.rt, p);
            nearestLeft = nearest(x.lb, p);
        }

        List<Point2D> points = new ArrayList<>();
        points.add(nearestLeft);
        points.add(x.p());
        points.add(nearestRight);
        return nearestAmong(p, points);
    }

    private double distanceBetween(Point2D p1, Point2D p2) {
        if (p1 == null || p2 == null) return Double.MAX_VALUE;
        return p1.distanceSquaredTo(p2);
    }

    private Point2D nearestAmong(Point2D p, List<Point2D> points) {
        List<Double> distances = new ArrayList<>();
        for (Point2D point : points) {
            distances.add(distanceBetween(p, point));
        }
        int minIndex = findMinimumIndex(distances);
        return points.get(minIndex);
    }

    private int findMinimumIndex(List<Double> list) {
        double min = Double.MAX_VALUE;
        int minIndex = list.size();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) < min) {
                min = list.get(i);
                minIndex = i;
            }
        }
        return minIndex;
    }

    private int comparePoints(Point2D p1, Point2D p2, boolean isVertical) {
        if (isVertical) {
            if (p1.x() > p2.x()) return 1;
            if (p1.x() < p2.x()) return -1;
            return 0;
        } else {
            if (p1.y() > p2.y()) return 1;
            if (p1.y() < p2.y()) return -1;
            return 0;
        }
    }

    private void checkArgument(Object arg) {
        if (arg == null) throw new IllegalArgumentException("Argument is null");
    }
}

import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;

public class FastCollinearPoints {
    private Queue<LineSegment> segments = new Queue<>();

    public FastCollinearPoints(Point[] points) {
        Point[] copiedPoints = copyPointArray(points);
        Merge.sort(copiedPoints);
        validatePoints(copiedPoints);
        Point[] auxArray = copyPointArray(copiedPoints);
        for (int i = 0; i < copiedPoints.length; i++) {
            Arrays.sort(auxArray, copiedPoints[i].slopeOrder());
            LineSegment[] lineSegments = findLineSegment(auxArray);
            if (lineSegments != null) {
                for (LineSegment s: lineSegments) {
                    segments.enqueue(s);
                }
            }
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] resultSegments = new LineSegment[segments.size()];
        int i = 0;
        for (LineSegment s: segments) {
            resultSegments[i] = s;
            i++;
        }
        return resultSegments;
    }

    private Point[] copyPointArray(Point[] points) {
        Point[] copy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            copy[i] = points[i];
        }
        return copy;
    }

    private void validatePoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points can't be null");
        }

        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Point can't be null");
            }

            if (points[i].compareTo(points[i+1]) == 0) {
                throw new IllegalArgumentException("Duplicate points found");
            }
        }

        if (points.length != 1 && points[points.length - 1] == null) {
            throw new IllegalArgumentException("Point can't be null");
        }
    }

    private LineSegment[] findLineSegment(Point[] sortedPoints) {
        Queue<Point> pointQueue = new Queue<>();
        Queue<LineSegment> lineSegments = new Queue<>();
        pointQueue.enqueue(sortedPoints[0]);
        int current = 1;
        int next = current + 1;
        while (current < sortedPoints.length - 2 && next < sortedPoints.length) {
            if (sortedPoints[0].slopeTo(sortedPoints[current]) == sortedPoints[0].slopeTo(sortedPoints[next])) {
                if (pointQueue.size() == 1) {
                    pointQueue.enqueue(sortedPoints[current]);
                }
                pointQueue.enqueue(sortedPoints[next]);
                next++;
            } else {
                if (pointQueue.size() >= 4) {
//                    Point[] collinearArray = convertToArray(pointQueue);
//                    Merge.sort(collinearArray);
//                    if (sortedPoints[0].compareTo(collinearArray[0]) < 0) {
//                        lineSegments.enqueue(new LineSegment(collinearArray[0], collinearArray[collinearArray.length - 1]));
//                    }
                    addLineSegment(lineSegments, sortedPoints[0], pointQueue);
                    pointQueue = new Queue<>();
                    pointQueue.enqueue(sortedPoints[0]);
//                    lineSegments.enqueue(getLineSegment(convertToArray(pointQueue)));
                } else {
                    current = next;
                    next = current + 1;
                    pointQueue = new Queue<>();
                    pointQueue.enqueue(sortedPoints[0]);
                }
            }
        }

        if (pointQueue.size() >= 4) {
//            lineSegments.enqueue(getLineSegment(convertToArray(pointQueue)));
            addLineSegment(lineSegments, sortedPoints[0], pointQueue);
        }

        LineSegment[] segmentArray = new LineSegment[lineSegments.size()];
        for (int i = 0; i < segmentArray.length; i++) {
            segmentArray[i] = lineSegments.dequeue();
        }
        return segmentArray;
    }

    private Point[] convertToArray(Queue<Point> queue) {
        Point[] points = new Point[queue.size()];
        int i = 0;
        while (!queue.isEmpty()) {
            points[i] = queue.dequeue();
            i++;
        }
        return points;
    }

    private void addLineSegment(Queue<LineSegment> lineSegments, Point ref, Queue<Point> collinearPointQueue) {
        Point[] collinearArray = convertToArray(collinearPointQueue);
        Merge.sort(collinearArray);
        if (ref.compareTo(collinearArray[0]) <= 0) {
            lineSegments.enqueue(new LineSegment(collinearArray[0], collinearArray[collinearArray.length - 1]));
        }
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}

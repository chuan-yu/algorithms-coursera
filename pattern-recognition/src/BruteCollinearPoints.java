import edu.princeton.cs.algs4.Insertion;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;

public class BruteCollinearPoints {

    private Queue<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        Point[] copiedPoints = copyPointArray(points);
        Merge.sort(copiedPoints);
        validateArg(copiedPoints);
        segments = new Queue<>();
        for (int i = 0; i < copiedPoints.length - 3; i++) {
            for (int j = i + 1; j < copiedPoints.length - 2; j++) {
                for (int k = j + 1; k < copiedPoints.length - 1; k++) {
                    if (copiedPoints[i].slopeTo(copiedPoints[j]) != copiedPoints[i].slopeTo(copiedPoints[k]))
                        continue;
                    for (int l = k + 1; l < copiedPoints.length; l++) {
                        if (copiedPoints[i].slopeTo(copiedPoints[j]) == copiedPoints[i].slopeTo(copiedPoints[l])) {
                            Point[] fourPoints = {copiedPoints[i], copiedPoints[j], copiedPoints[k], copiedPoints[l]};
                            Insertion.sort(fourPoints);
                            segments.enqueue(new LineSegment(fourPoints[0], fourPoints[3]));
                        }
                    }
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

    private void validateArg(Point[] points) {
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

    private Point[] copyPointArray(Point[] points) {
        Point[] copy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            copy[i] = points[i];
        }
        return copy;
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        Point[] points = new Point[n];
        int i = 0;
        while (!StdIn.isEmpty()) {
            points[i] = new Point(StdIn.readInt(), StdIn.readInt());
            i++;
        }
        BruteCollinearPoints brute = new BruteCollinearPoints(points);
        LineSegment[] segments = brute.segments();
        for (LineSegment s: segments) {
            System.out.println(s);
        }
    }
}

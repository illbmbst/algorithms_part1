import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private List<LineSegment> lineSegments = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Constructor argument shouldn't be null");
        }

        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException("Point shouldn't be null");
            }
        }

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];

            for (int j = 0; j < points.length; j++) {
                if (j == i) {
                    continue;
                }
                Point q = points[j];

                if (p.compareTo(q) == 0) {
                    throw new IllegalArgumentException("Array shouldn't contain repeated points");
                }

                //slope
                double slope = p.slopeTo(q);

                for (int k = 0; k < points.length; k++) {
                    if (k == i || k == j) {
                        continue;
                    }
                    Point r = points[k];

                    if (q.slopeTo(r) != slope) {
                        break;
                    }

                    for (int l = 0; l < points.length; l++) {
                        if (l == i || l == j || l == k) {
                            continue;
                        }
                        Point s = points[l];

                        if (s.slopeTo(r) != slope) {
                            break;
                        }

                        Point[] colPoints = new Point[]{p, q, r, s};
                        Arrays.sort(colPoints);

                        if (Arrays.equals(colPoints, new Point[]{p, q, r, s})) {
                            lineSegments.add(new LineSegment(p, s));
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

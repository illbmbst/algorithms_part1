import java.util.ArrayList;
import java.util.List;

public class BruteCollinearPoints {

    public static void main(String[] args) {

    }

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
            Point min = p;
            Point max = p;

            for (int j = 0; j < points.length; j++) {
                if (j == i) {
                    continue;
                }
                Point q = points[j];

                if (p.compareTo(q) == 0) {
                    throw new IllegalArgumentException("Array shouldn't contain repeated points");
                }

                if (min.compareTo(q) >= 1) {
                    min = q;
                }
                if (max.compareTo(q) <= -1) {
                    max = q;
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

                        lineSegments.add(new LineSegment(min, max));
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return (LineSegment[]) lineSegments.toArray();
    }
}

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Set;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> set = new TreeSet<>();

    public PointSET() {
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        set.add(p);
    }

    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    public void draw() {
        for (Point2D point : set) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        Set<Point2D> result = new TreeSet<>();
        for (Point2D point : set) {
            if (rect.contains(point)) {
                result.add(point);
            }
        }
        return result;
    }

    public Point2D nearest(Point2D p) {
        if (set.isEmpty()) {
            return null;
        }
        double minDist = Double.MAX_VALUE;
        Point2D nearest = null;
        for (Point2D point2D : set) {
            double dist = point2D.distanceTo(p);
            if (dist < minDist) {
                minDist = dist;
                nearest = point2D;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {

    }
}

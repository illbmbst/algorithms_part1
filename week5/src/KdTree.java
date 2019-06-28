import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Collections;


public class KdTree {

    private Node root = null;
    private int size = 0;

    private static class Node {
        private Point2D point;
        private RectHV rect;
        private Node left;
        private Node right;

        public Node(Point2D point) {
            this.point = point;
        }
    }


    public KdTree() {
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (root == null) {
            root = new Node(p);
            size++;
        } else {
            put(p, root, false);
        }
    }

    private void put(Point2D p, Node root, boolean even) {
        double cmp = cmp(p, root.point, even);
        if (cmp > 0) {
            if (root.right == null) {
                root.right = new Node(p);
                size++;
            } else {
                put(p, root.right, !even);
            }
        } else {
            if (root.left == null) {
                root.left = new Node(p);
                size++;
            } else {
                put(p, root.left, !even);
            }
        }
    }

    public boolean contains(Point2D p) {
        return exists(p, root, false);
    }

    private boolean exists(Point2D p, Node root, boolean even) {
        if (root == null) {
            return false;
        }
        if (p.equals(root.point)) {
            return true;
        }
        double cmp = cmp(p, root.point, even);
        if (cmp > 0) {
            return exists(p, root.right, !even);
        } else {
            return exists(p, root.left, !even);
        }
    }

    private int cmp(Point2D p1, Point2D p2, boolean even) {
        return even ? Point2D.X_ORDER.compare(p1, root.point) : Point2D.Y_ORDER.compare(p2, root.point);
    }

    public void draw() {

    }

    public Iterable<Point2D> range(RectHV rect) {
        return Collections.emptySet();
    }

    public Point2D nearest(Point2D p) {
        return p;
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.1, 0.2));
        kdTree.insert(new Point2D(0.5, 0.01));
        kdTree.insert(new Point2D(0.04, 0.01));
        kdTree.insert(new Point2D(0.1, 0.01));
        kdTree.insert(new Point2D(0.06, 0.7));
        kdTree.insert(new Point2D(0.055, 0.55));
        kdTree.insert(new Point2D(0.066, 0.03));
        kdTree.insert(new Point2D(0.1, 0.6));

        System.out.println(kdTree.contains(new Point2D(0.06, 0.7)));
        System.out.println(kdTree.contains(new Point2D(0.5, 0.01)));
        System.out.println(kdTree.contains(new Point2D(0.5, 0.02)));
    }
}

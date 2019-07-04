import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class KdTree {

    private Node root = null;
    private int size = 0;

    public KdTree() {
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p must be not null");
        }
        if (root == null) {
            root = new Node(p);
            size++;
        } else {
            put(p, root, false);
        }
    }

    private void put(Point2D p, Node root, boolean even) {
        double cmp = cmp(p, root.point, even);
        if (cmp >= 0) {
            if (root.right == null) {
                RectHV rect = even ? new RectHV(root.rect.xmin(), root.point.y(), root.rect.xmax(), root.rect.ymax())
                        : new RectHV(root.point.x(), root.rect.ymin(), root.rect.xmax(), root.rect.ymax());
                root.right = new Node(p, rect);
                size++;
            } else {
                put(p, root.right, !even);
            }
        } else {
            if (root.left == null) {
                RectHV rect = even ? new RectHV(root.rect.xmin(), root.rect.ymin(), root.rect.xmax(), root.point.y())
                        : new RectHV(root.rect.xmin(), root.rect.ymin(), root.point.x(), root.rect.ymax());
                root.left = new Node(p, rect);
                size++;
            } else {
                put(p, root.left, !even);
            }
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p must be not null");
        }
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
        if (cmp >= 0) {
            return exists(p, root.right, !even);
        } else {
            return exists(p, root.left, !even);
        }
    }

    private int cmp(Point2D p1, Point2D p2, boolean even) {
        return even ? Point2D.Y_ORDER.compare(p1, p2) : Point2D.X_ORDER.compare(p1, p2);
    }

    public void draw() {
        StdDraw.setScale(0, 1);
        drawNode(root, true);
    }

    private void drawNode(Node node, boolean even) {
        if (node == null) {
            return;
        }
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        node.point.draw();

        StdDraw.setPenRadius();
        if (even) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
        }

        drawNode(node.left, !even);
        drawNode(node.right, !even);
    }

    public Iterable<Point2D> range(RectHV rect) {
        return range(root, rect);
    }

    private Collection<Point2D> range(Node root, RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("rect must be not null");
        }
        if (root == null || !root.rect.intersects(rect)) {
            return Collections.emptySet();
        }
        List<Point2D> res = new ArrayList<>();
        if (rect.contains(root.point)) {
            res.add(root.point);
        }
        res.addAll(range(root.right, rect));
        res.addAll(range(root.left, rect));
        return res;
    }


    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p must be not null");
        }
        return nearest(root, p, root.point);
    }

    private Point2D nearest(Node root, Point2D point, Point2D nearest) {
        if (root == null) {
            return nearest;
        }
        if (root.rect.distanceTo(point) > nearest.distanceTo(point)) {
            return nearest;
        }
        if (root.point.distanceTo(point) < nearest.distanceTo(point)) {
            nearest = root.point;
        }
        if (root.left != null && root.left.rect.contains(point)) {
            nearest = nearest(root.left, point, nearest);
            nearest = nearest(root.right, point, nearest);
        } else {
            nearest = nearest(root.right, point, nearest);
            nearest = nearest(root.left, point, nearest);
        }
        return nearest;
    }

    private static class Node {
        private Point2D point;
        private RectHV rect;
        private Node left;
        private Node right;

        Node(Point2D point) {
            this.point = point;
            this.rect = new RectHV(0, 0, 1, 1);
        }

        Node(Point2D point, RectHV rect) {
            this.point = point;
            this.rect = rect;
        }
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.1, 0.2)); //A
        kdTree.insert(new Point2D(0.5, 0.02)); //B
        kdTree.insert(new Point2D(0.04, 0.01)); //C
        kdTree.insert(new Point2D(0.2, 0.01)); //D
        kdTree.insert(new Point2D(0.06, 0.7)); //E

        kdTree.insert(new Point2D(0.055, 0.55));
        kdTree.insert(new Point2D(0.066, 0.03));
        kdTree.insert(new Point2D(0.1, 0.6));
        kdTree.insert(new Point2D(0.5, 0.5));
        kdTree.insert(new Point2D(0.4, 0.8));

        System.out.println(kdTree.contains(new Point2D(0.06, 0.7)));
        System.out.println(kdTree.contains(new Point2D(0.5, 0.01)));
        System.out.println(kdTree.contains(new Point2D(0.5, 0.02)));

        kdTree.draw();
    }
}

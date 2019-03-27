import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {

    private List<LineSegment> segments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {

        if (points == null) {
            throw new IllegalArgumentException("Constructor argument shouldn't be null");
        }

        Arrays.sort(points);

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Point shouldn't be null");
            }

            Point p = points[i];
            Comparator<Point> comparator = points[i].slopeOrder();
            // Сортируем по отклонению
            Arrays.sort(points, comparator);

//            System.out.print("Point #" + i + ": ");
//            for (Point point : points) {
//                System.out.print(p.slopeTo(point) + ", ");
//            }
//            System.out.println();

            if (p.compareTo(points[1]) == 0) {
                throw new IllegalArgumentException("Array shouldn't contain repeated points");
            }

            // вычисляем отклонение между p и первой точкой после p
            double slope = p.slopeTo(points[1]);
            int start = 1;
            int count = 1;
            // проходимся по всем точкам начиная с третьей
            for (int j = start + 1; j < points.length; j++) {
                double curSlope = p.slopeTo(points[j]);

                //Если отклонение такое же, как и у предидущей точки
                if (curSlope == slope) {
                    // Увеличиваем счетчик линейных точек и идем дальше
                    count++;
                } else {
                    // если образовалась линия из 4 или более точек
                    addSegment(p, count, start, points);
                    count = 1;
                    start = j;
                    slope = curSlope;
                }
            }

            // Последний сегмент
            addSegment(p, count, start, points);

            Arrays.sort(points);
        }
    }

    private void addSegment(Point p, int count, int start, Point[] points) {
        if (count >= 3) {
            // 1. Собираем все последовательные точки в массив
            Point[] collinear = new Point[count + 1];
            collinear[0] = p;
            for (int k = 1; k <= count; k++) {
                collinear[k] = points[start + k - 1];
            }
            // 2. Сортируем
            Arrays.sort(collinear);
            // 3. Если первая точка получившегося массива совпадает с p - добавляем новый сегмент
            if (p.equals(collinear[0])) {
//                System.out.print("Point #" + i + ": ");
//                for (Point point : collinear) {
//                    System.out.print(p.slopeTo(point) + ", ");
//                }
//                System.out.println();
                segments.add(new LineSegment(collinear[0], collinear[collinear.length - 1]));
            }
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    public static void main(String[] args) {
        draw("resources/rs1423.txt");
    }

    private static void draw(String file) {
        // read the n points from a file
        In in = new In(file);
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

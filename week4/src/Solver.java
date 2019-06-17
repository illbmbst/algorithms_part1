import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;

public class Solver {

    private Node solution = null;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("initial is null");
        }
        MinPQ<Node> pq = new MinPQ<>(Comparator.comparingInt(o -> o.getManhattan() + o.moves));
        MinPQ<Node> twinPq = new MinPQ<>(Comparator.comparingInt(o -> o.getManhattan() + o.moves));
        pq.insert(new Node(initial));
        Board twin = initial.twin();
        twinPq.insert(new Node(twin));
        boolean solved = false;
        while (!solved) {
            Node min = pq.delMin();
            Node twinMin = twinPq.delMin();

            if (twinMin.board.isGoal()) {
                solved = true;
            } else {
                for (Board neighbor : twinMin.board.neighbors()) {
                    if (!neighbor.equals(twinMin.prev == null ? null : twinMin.prev.board)) {
                        twinPq.insert(new Node(neighbor, twinMin));
                    }
                }

                if (min.board.isGoal()) {
                    solved = true;
                    this.solution = min;
                } else {
                    for (Board neighbor : min.board.neighbors()) {
                        if (!neighbor.equals(min.prev == null ? null : min.prev.board)) {
                            pq.insert(new Node(neighbor, min));
                        }
                    }
                }
            }
        }
    }


    public boolean isSolvable() {
        return this.solution != null;
    }

    public int moves() {
        if (this.solution == null) {
            return -1;
        }
        return this.solution.moves;
    }

    public Iterable<Board> solution() {
        if (this.solution == null) {
            return null;
        }
        Deque<Board> result = new LinkedList<>();
        Node i = this.solution;
        while (i != null) {
            result.addFirst(i.board);
            i = i.prev;
        }
        return result;
    }

    private class Node {

        int moves;
        Board board;
        Node prev;
        private int manhattanCache = -1;

        Node(Board board) {
            this.board = board;
            this.moves = 0;
            this.prev = null;
        }

        Node(Board board, Node prev) {
            this.board = board;
            this.moves = prev.moves + 1;
            this.prev = prev;
        }

        public int getManhattan() {
            if (manhattanCache >= 0) {
                return manhattanCache;
            }
            return board.manhattan();
        }
    }

    public static void main(String[] args) {
        Board board1 = new Board(new int[][]{
                new int[]{1, 0},
                new int[]{3, 2}
        });
        Solver solver = new Solver(board1);
        System.out.println(solver.isSolvable());
        System.out.println("Moves: " + solver.moves());
        for (Board board : solver.solution()) {
            System.out.println(board);
        }
    }
}

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {

    private final int[][] blocks;
    private final int n;
    private Board twin = null;

    public Board(int[][] blocks) {
        this.blocks = copy(blocks);
        this.n = blocks.length;
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int hamming = 0;
        int count = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int block = blocks[i][j];
                if (block != 0 && block != count) {
                    hamming++;
                }
                count++;
            }
        }
        return hamming;
    }

    public int manhattan() {
        int manhattan = 0;
        for (int curY = 1; curY <= n; curY++) {
            for (int curX = 1; curX <= n; curX++) {
                int block = blocks[curY - 1][curX - 1];
                if (block == 0) {
                    continue;
                }
                int[] goal = findGoal(block);
                manhattan += Math.abs((goal[0] + 1 - curY)) + Math.abs((goal[1] + 1 - curX));
            }
        }
        return manhattan;
    }

    /**
     * Finds goal position of element x in array.
     */
    private int[] findGoal(int x) {
        int position = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (position == x) {
                    return new int[] {i, j};
                }
                position++;
            }
        }
        return new int[] {0, 0};
    }

    public boolean isGoal() {
        return this.hamming() == 0;
    }

    public Board twin() {
        if (this.twin != null) {
            return this.twin;
        }
        int[][] copy = copy(this.blocks);
        int aX, aY;
        do {
            aY = StdRandom.uniform(0, n);
            aX = StdRandom.uniform(0, n);
        } while (copy[aY][aX] == 0);
        int bX, bY;
        do {
            bY = StdRandom.uniform(0, n);
            bX = StdRandom.uniform(0, n);
        } while ((copy[bY][bX] == 0) || (aX == bX && aY == bY));
        int a = copy[aY][aX];
        int b = copy[bY][bX];
        copy[aY][aX] = b;
        copy[bY][bX] = a;
        Board newTwin = new Board(copy);
        this.twin = newTwin;
        return newTwin;
    }

    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) {
            return false;
        }
        if (this.n != that.dimension()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.blocks[i][j] != that.blocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        return NeighborsIterator::new;
    }

    private class NeighborsIterator implements Iterator<Board> {
        int blankX, blankY;
        Stack<Board> neighbors = new Stack<>();

        NeighborsIterator() {
            int[] blankBlock = getBlankBlock();
            blankX = blankBlock[1];
            blankY = blankBlock[0];

            if (blankX - 1 >= 0) {
                int[][] copy = copy(blocks);
                copy[blankY][blankX] = blocks[blankY][blankX - 1];
                copy[blankY][blankX - 1] = blocks[blankY][blankX];
                neighbors.push(new Board(copy));
            }
            if (blankY - 1 >= 0) {
                int[][] copy = copy(blocks);
                copy[blankY][blankX] = blocks[blankY - 1][blankX];
                copy[blankY - 1][blankX] = blocks[blankY][blankX];
                neighbors.push(new Board(copy));
            }
            if (blankX + 1 < n) {
                int[][] copy = copy(blocks);
                copy[blankY][blankX] = blocks[blankY][blankX + 1];
                copy[blankY][blankX + 1] = blocks[blankY][blankX];
                neighbors.push(new Board(copy));
            }
            if (blankY + 1 < n) {
                int[][] copy = copy(blocks);
                copy[blankY][blankX] = blocks[blankY + 1][blankX];
                copy[blankY + 1][blankX] = blocks[blankY][blankX];
                neighbors.push(new Board(copy));
            }
        }

        @Override
        public boolean hasNext() {
            return !neighbors.isEmpty();
        }

        @Override
        public Board next() {
            if (neighbors.isEmpty()) {
                throw new NoSuchElementException();
            }
            return neighbors.pop();
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private static int[][] copy(int[][] blocks) {
        int[][] result = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            System.arraycopy(blocks[i], 0, result[i], 0, blocks.length);
        }
        return result;
    }

    private int[] getBlankBlock() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.blocks[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{0, 0};
    }


    // --------------------

    public static void main(String[] args) {
        testHamming();
        testManhattan();
        testEquals();
        testNeighbors();
        testFindGoal();
        testTwin();
        testCopy();
        getBlankBlockTest();
    }

    private static void testHamming() {
        System.out.println("*** Hamming ***");
        Board board1 = new Board(new int[][]{
                new int[]{1, 2, 3},
                new int[]{4, 5, 6},
                new int[]{7, 0, 8}
        });
        int result1 = board1.hamming();
        System.out.println(String.format("Board1: expected = %d, result = %d", 1, result1));
        Board board2 = new Board(new int[][]{
                new int[]{1, 2, 3},
                new int[]{4, 5, 6},
                new int[]{7, 8, 0}
        });
        int result2 = board2.hamming();
        System.out.println(String.format("Board2: expected = %d, result = %d", 0, result2));
    }

    private static void testManhattan() {
        System.out.println("*** Manhattan ***");
        Board board1 = new Board(new int[][]{
                new int[]{8, 2, 3},
                new int[]{4, 0, 6},
                new int[]{7, 5, 1}
        });
        int result1 = board1.manhattan();
        System.out.println(String.format("Board1: expected = %d, result = %d", 8, result1));

        Board board2 = new Board(new int[][]{
                new int[]{8, 1, 3},
                new int[]{4, 0, 2},
                new int[]{7, 6, 5}
        });
        int result2 = board2.manhattan();
        System.out.println(String.format("Board2: expected = %d, result = %d", 10, result2));
    }

    private static void testEquals() {
        System.out.println("*** Equals ***");
        Board board1 = new Board(new int[][]{
                new int[]{8, 2, 3},
                new int[]{4, 0, 6},
                new int[]{7, 5, 1}
        });
        Board board2 = new Board(new int[][]{
                new int[]{8, 2, 3},
                new int[]{4, 0, 6},
                new int[]{7, 5, 1}
        });
        System.out.println(String.format("Expected = %b, result = %b", true, board1.equals(board2)));
    }

    private static void testNeighbors() {
        System.out.println("*** Neighbors ***");
        Board board1 = new Board(new int[][]{
                new int[]{7, 6, 2},
                new int[]{0, 3, 8},
                new int[]{1, 5, 4}
        });
        for (Board neighbor : board1.neighbors()) {
            System.out.println(neighbor);
        }
    }

    private static void testFindGoal() {
        System.out.println("*** FindGoal ***");
        Board board1 = new Board(new int[][]{
                new int[]{8, 2, 3},
                new int[]{0, 4, 6},
                new int[]{7, 5, 1}
        });
        int[] goal1 = board1.findGoal(1);
        System.out.println(String.format("Goal1: Expected = 0, 0, result = %d, %d", goal1[0], goal1[1]));
        int[] goal2 = board1.findGoal(4);
        System.out.println(String.format("Goal2: Expected = 1, 0, result = %d, %d", goal2[0], goal2[1]));
        int[] goal3 = board1.findGoal(8);
        System.out.println(String.format("Goal3: Expected = 2, 1, result = %d, %d", goal3[0], goal3[1]));
    }

    private static void testTwin() {
        System.out.println("*** Twin ***");
        Board board1 = new Board(new int[][]{
                new int[]{1, 6, 4},
                new int[]{7, 0, 8},
                new int[]{2, 5, 3}
        });
        System.out.println(board1.twin());
    }

    private static void testCopy() {
        System.out.println("*** Copy ***");
        Board board1 = new Board(new int[][]{
                new int[]{1, 6, 4},
                new int[]{7, 0, 8},
                new int[]{2, 3, 5}
        });
        System.out.println(board1);
        System.out.println(new Board(copy(board1.blocks)).toString());
    }

    private static void getBlankBlockTest() {
        System.out.println("*** GetBlankBlock ***");
        Board board1 = new Board(new int[][]{
                new int[]{1, 6, 4},
                new int[]{7, 0, 8},
                new int[]{2, 3, 5}
        });
        int[] blank = board1.getBlankBlock();
        if (blank[0] != 1 && blank[1] != 1) {
            System.err.println("Error getBlankBlock");
        }

        Board board2 = new Board(new int[][]{
                new int[]{1, 6, 4},
                new int[]{7, 5, 0},
                new int[]{2, 3, 8}
        });
        int[] blank2 = board2.getBlankBlock();
        if (blank2[0] != 1 && blank2[1] != 2) {
            System.err.println("Error getBlankBlock");
        }
    }
}

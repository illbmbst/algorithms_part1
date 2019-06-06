import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class Board {

    private int[][] blocks;
    private int n;
    private int manhattanCache = -1;

    public Board(int[][] blocks) {
        this.blocks = blocks;
        this.n = blocks.length;
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int hamming = 0;
        int count = 1;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
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
        if (manhattanCache != -1) {
            return manhattanCache;
        }
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


        manhattanCache = manhattan;
        return manhattan;
    }

    /**
     * Finds goal position of element x in array.
     * @param x
     * @return
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
        return hamming() == 0;
    }

    public Board twin() {
        int aX, aY;
        do {
            aX = StdRandom.uniform(1, 10);
            aY = StdRandom.uniform(1, 10);
        } while (blocks[aX][aY] != 0);
        int bX, bY;
        do {
            bX = StdRandom.uniform(1, 10);
            bY = StdRandom.uniform(1, 10);
        } while (blocks[bX][bY] != 0);

        Board newBoard = copy();
        int a = newBoard.blocks[aX][aY];
        int b = newBoard.blocks[bX][bY];
        newBoard.blocks[aX][aY] = b;
        newBoard.blocks[bX][bY] = a;
        return newBoard;
    }

    public boolean equals(Object y) {
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) {
            return false;
        }
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (this.blocks[i][j] != that.blocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        return () -> new Iterator<Board>() {

            int blankX, blankY;
            {
                int[] blankBlock = getBlankBlock();
                blankX = blankBlock[1];
                blankY = blankBlock[0];
            }

            Stack<Board> neighbors = new Stack<>();
            {
                if (blankX - 1 >= 0) {
                    Board b = copy();
                    b.blocks[blankY][blankX] = blocks[blankY][blankX - 1];
                    b.blocks[blankY][blankX - 1] = blocks[blankY][blankX];
                    neighbors.push(b);
                }
                if (blankY - 1 >= 0) {
                    Board b = copy();
                    b.blocks[blankY][blankX] = blocks[blankY - 1][blankX];
                    b.blocks[blankY - 1][blankX] = blocks[blankY][blankX];
                    neighbors.push(b);
                }
                if (blankX + 1 < n) {
                    Board b = copy();
                    b.blocks[blankY][blankX] = blocks[blankY][blankX + 1];
                    b.blocks[blankY][blankX + 1] = blocks[blankY][blankX];
                    neighbors.push(b);
                }
                if (blankY + 1 < n) {
                    Board b = copy();
                    b.blocks[blankY][blankX] = blocks[blankY + 1][blankX];
                    b.blocks[blankY + 1][blankX] = blocks[blankY][blankX];
                    neighbors.push(b);
                }
            }

            @Override
            public boolean hasNext() {
                return !neighbors.isEmpty();
            }

            @Override
            public Board next() {
                return neighbors.pop();
            }
        };
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

    private Board copy() {
        int[][] result = new int[dimension()][dimension()];
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                result[i][j] = blocks[i][j];
            }
        }
        return new Board(result);
    }

    public int[] getBlankBlock() {
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (this.blocks[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{0, 0};
    }

    public static void main(String[] args) {
        testHamming();
        testManhattan();
        testEquals();
        testNeighbors();
        testFindGoal();
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
        boolean result1 = board1.equals(board2);
        System.out.println(String.format("Expected = %b, result = %b", true, result1));
    }

    private static void testNeighbors() {
        System.out.println("*** Neighbors ***");
        Board board1 = new Board(new int[][]{
                new int[]{8, 2, 3},
                new int[]{0, 4, 6},
                new int[]{7, 5, 1}
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
}

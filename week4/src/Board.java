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
        for (int curY = 1; curY <= dimension(); curY++) {
            for (int curX = 1; curX <= dimension(); curX++) {
                int block = blocks[curY - 1][curX - 1];
                if (block == 0) {
                    continue;
                }
                int goalX, goalY;
                if (block == 1) {
                    goalX = 1;
                } else if (block % dimension() != 0) {
                    goalX = block % dimension();
                } else {
                    goalX = dimension();
                }
                goalY = ((block - 1) / dimension()) + 1;
                manhattan += Math.abs((goalY - curY)) + Math.abs((goalX - curX));
            }
        }
        manhattanCache = manhattan;
        return manhattan;
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
}

import edu.princeton.cs.algs4.StdRandom;

public class Board {

    private int[][] blocks;

    public Board(int[][] blocks) {
        this.blocks = blocks;
    }

    public int dimension() {
        return blocks.length;
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
        return 0;
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
        return null;
    }

    public String toString() {
        return null;
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

    public static void main(String[] args) {
        testHamming();
        testManhattan();
        testEquals();
    }

    private static void testHamming() {
        Board board1 = new Board(new int[][]{
                new int[]{1, 2, 3},
                new int[]{4, 5, 6},
                new int[]{7, 0, 8}
        });
        int result1 = board1.hamming();
        System.out.println(String.format("Board1: expected = %d, result = %d", 1, result1));
    }

    private static void testManhattan() {
        Board board1 = new Board(new int[][]{
                new int[]{8, 2, 3},
                new int[]{4, 0, 6},
                new int[]{7, 5, 1}
        });
        int result1 = board1.manhattan();
        System.out.println(String.format("Board1: expected = %d, result = %d", 8, result1));
    }

    private static void testEquals() {
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
}

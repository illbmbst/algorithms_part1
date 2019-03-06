import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF uf;
    private final Cell[][] grid;
    private int opened = 0;
    private final int n;
    private final int topSite;
    private final int bottomSite;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be <= 0");
        }
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.grid = new Cell[n][n];
        this.n = n;
        this.topSite = n * n;
        this.bottomSite = n * n + 1;
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = new Cell(false, count);
                count++;
            }
        }
    }

    public void open(int row, int col) {
        checkArgs(row, col);
        int rowIdx = row - 1;
        int colIdx = col - 1;
        Cell current = grid[rowIdx][colIdx];
        if (current.open) {
            return;
        }
        current.open = true;
        // union up
        if (row > 1) {
            Cell up = grid[rowIdx - 1][colIdx];
            if (up.open) {
                this.uf.union(current.number, up.number);
            }
        }
        // union down
        if (row < n) {
            Cell down = grid[rowIdx + 1][colIdx];
            if (down.open) {
                this.uf.union(current.number, down.number);
            }
        }
        // union left
        if (col > 1) {
            Cell left = grid[rowIdx][colIdx - 1];
            if (left.open) {
                this.uf.union(current.number, left.number);
            }
        }
        // union right
        if (col < n) {
            Cell right = grid[rowIdx][colIdx + 1];
            if (right.open) {
                this.uf.union(current.number, right.number);
            }
        }
        // virtual top
        if (rowIdx == 0) {
            this.uf.union(topSite, current.number);
        }
        // virtual bottom
        if (rowIdx == n - 1) {
            this.uf.union(bottomSite, current.number);
        }
        opened++;
    }

    public boolean isOpen(int row, int col) {
        checkArgs(row, col);
        return grid[row - 1][col - 1].open;
    }

    public boolean isFull(int row, int col) {
        checkArgs(row, col);
        int rowIdx = row - 1;
        int colIdx = col - 1;
        return uf.connected(grid[rowIdx][colIdx].number, topSite);
    }

    public int numberOfOpenSites() {
        return opened;
    }

    public boolean percolates() {
        return uf.connected(topSite, bottomSite);
    }

    private void checkArgs(int row, int col) {
        if (row > n || row < 1 || col > n || col < 1) {
            throw new IllegalArgumentException("Invalid input: row=" + row + ", col=" + col);
        }
    }

    private static class Cell {
        boolean open;
        int number;

        public Cell(boolean open, int number) {
            this.open = open;
            this.number = number;
        }
    }
}

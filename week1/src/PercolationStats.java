import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] results;
    private double mean = 0;
    private double stddev = 0;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials should be <= 0");
        }
        this.results = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            for (int x = 0; x < n * n; x++) {
                int row;
                int col;
                do {
                    row = StdRandom.uniform(1, n + 1);
                    col = StdRandom.uniform(1, n + 1);
                } while (percolation.isOpen(row, col));
                percolation.open(row, col);
                if (percolation.percolates()) {
                    results[i] = percolation.numberOfOpenSites() * 1d / (n * n);
                    break;
                }
            }
        }
    }

    public double mean() {
        if (mean == 0) {
            mean = StdStats.mean(results);
        }
        return mean;
    }

    public double stddev() {
        if (stddev == 0) {
            stddev = StdStats.stddev(results);
        }
        return stddev;
    }

    public double confidenceLo() {
        return mean() - 1.96d * stddev() / Math.sqrt(results.length);
    }

    public double confidenceHi() {
        return mean() + 1.96d * stddev() / Math.sqrt(results.length);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, T);
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}

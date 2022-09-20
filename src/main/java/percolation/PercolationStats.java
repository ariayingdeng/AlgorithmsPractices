package percolation;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_LEVEL = 1.96;
    private final double[] percolationThresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw  new IllegalArgumentException("n or trials must be larger than 0");
        }

        percolationThresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int randomRow = StdRandom.uniformInt(n) + 1;
                int randomCol = StdRandom.uniformInt(n) + 1;
                percolation.open(randomRow, randomCol);
            }
            percolationThresholds[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

//    private double determinePercolationThresholds(int n) {
//        Percolation percolation = new Percolation(n);
//        while (!percolation.percolates()) {
//            int randomRow = StdRandom.uniformInt(n) + 1;
//            int randomCol = StdRandom.uniformInt(n) + 1;
//            percolation.open(randomRow, randomCol);
//        }
//        return percolation.numberOfOpenSites() / (n * n);
//    }

    // sample mean of percolation threshold
    public double mean() {
//        double total = 0;
//        for (int i = 0; i < percolationThresholds.length; i++) {
//            total += percolationThresholds[i];
//        }
//        return total / percolationThresholds.length;
        return StdStats.mean(percolationThresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
//        double mean = mean();
//        double total = 0;
//        for (int i = 0; i < percolationThresholds.length; i++) {
//            total += Math.pow((percolationThresholds[i] - mean), 2);
//        }
//        return Math.sqrt(total / (percolationThresholds.length - 1));
        return StdStats.stddev(percolationThresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double mean = mean();
        double s = stddev();
        int numOfSamples = percolationThresholds.length;
        return mean - (CONFIDENCE_LEVEL * s) / Math.sqrt(numOfSamples);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mean = mean();
        double s = stddev();
        int numOfSamples = percolationThresholds.length;
        return mean + (CONFIDENCE_LEVEL * s) / Math.sqrt(numOfSamples);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        StdOut.println("mean\t= " + stats.mean());
        StdOut.println("stddev\t= " + stats.stddev());
        StdOut.println("95% confidence interval\t= [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}

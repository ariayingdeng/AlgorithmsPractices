package percolation;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF uf;
    private boolean[] sites;
    private final int n; // n-by-n grid
    private int numOpen;
    private boolean percolate;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be larger than 0");
        }

        numOpen = 0;
        this.n = n;
        int num = n * n;

        // initialize the sites being blocked
        sites = new boolean[num + 1];
        for (int i = 0; i < sites.length; i++) {
            sites[i] = false;
        }

        // initialize the empty union-find data structure
        // a virtual top site with the value of 0 and the grid starts with 1
        uf = new WeightedQuickUnionUF(num + 1);
    }

    private int getUFIndex(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IllegalArgumentException("Outside range of rows and cols");
        } else {
            return (row - 1) * n + col;
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int index = getUFIndex(row, col);
        if (!sites[index]) {
            numOpen++;
            sites[index] = true;

            // the first row: connect the site with the virtual top
            if (row == 1)
                uf.union(index, 0);

            // connect the open site with other open neighbors
            // check top site(row-1, col)
            if (row > 1 && isOpen(row - 1, col))
                uf.union(index, index - n);

            // check bottom site(row+1, col)
            if (row < n && isOpen(row + 1, col))
                uf.union(index + n, index);

            // check left site(row, col-1)
            if (col > 1 && isOpen(row, col - 1))
                uf.union(index, index - 1);

            // check right site(row, col+1)
            if (col < n && isOpen(row, col + 1))
                uf.union(index + 1, index);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int index = getUFIndex(row, col);
        if (sites[index]) {
            return true;
        }
        return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int index = getUFIndex(row, col);
        // if the site is connected to the virtual top
        if (uf.find(0) == uf.find(index)) {
            return true;
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        if (uf.find(0) > n * (n - 1)) {
            return true;
        }
        return false;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(4);
        percolation.open(2, 3);
        percolation.open(2, 2);
        percolation.open(1, 2);
        percolation.open(4, 3);
        percolation.open(3, 3);
        StdOut.println("Check (4, 3) isFull? " + percolation.isFull(4, 3));
        StdOut.println("Check percolation? " + percolation.percolates());
    }
}


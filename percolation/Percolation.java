import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF ufTop;
    private WeightedQuickUnionUF ufBottom;
    private boolean[] openness;
    private int size;
    private int n;
    private int nOpenSites;
    private boolean percolates;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        this.n = n;
        size = n * n + 1;
        ufTop = new WeightedQuickUnionUF(size);
        ufBottom = new WeightedQuickUnionUF(size);
        openness = new boolean[size];
        openness[0] = true;
        nOpenSites = 0;
        percolates = false;
    }

    public void open(int row, int col) {
        validateRowCol(row, col);
        int p = convertRowColToIndex(row, col);
        if (!openness[p]) {
            openness[p] = true;
            nOpenSites++;
            int up = p - n;
            int down = p + n;
            int left = p - 1;
            int right = p + 1;
            if (up >= 1 && openness[up]) {
                ufTop.union(p, up);
                ufBottom.union(p, up);
            }
            if (down <= size - 1 && openness[down]) {
                ufTop.union(p, down);
                ufBottom.union(p, down);
            }
            if (left > 0 && left % n != 0 && openness[left]) {
                ufTop.union(p, left);
                ufBottom.union(p, left);
            }
            if (right <= size - 1 && p % n != 0 && openness[right]){
                ufTop.union(p, right);
                ufBottom.union(p, right);
            }
        }
        if (row == 1) {
            ufTop.union(0, p);
        }
        if (row == n) {
            ufBottom.union(0, p);
        }
        if (ufTop.connected(0, p) && ufBottom.connected(0, p)) {
            percolates = true;
        }
    }

    public boolean isOpen(int row, int col) {
        validateRowCol(row, col);
        int index = convertRowColToIndex(row, col);
        return openness[index];
    }

    public boolean isFull(int row, int col) {
        if (!isOpen(row, col)) {
            return false;
        }

        if (row == 1) {
            return true;
        }

        int index = convertRowColToIndex(row, col);
        return ufTop.connected(0, index);
    }

    public int numberOfOpenSites() {
        return nOpenSites;
    }

    public boolean percolates() {
        return percolates;
    }

    private void validateRowCol(int row, int col) {
        if ((row <= 0) || (row > n) || (col <= 0) || (col > n)) {
            throw new IllegalArgumentException(
                    "row and col should be between 1 and n (inclusive)");
        }
    }

    private int convertRowColToIndex(int row, int col) {
        return (row - 1) * n + col;
    }
}

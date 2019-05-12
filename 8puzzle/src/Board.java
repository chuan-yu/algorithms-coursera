import java.util.ArrayList;

public class Board {

    private final int[][] blocks;
    private final int d;
    private Coordinate blankCoordinate;
    private int hammingDistance = -1;
    private int manhattanDistance = -1;

    private class Coordinate {
        public final int x;
        public final int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public Board(int[][] blocks) {
        this.blocks = blocks;
        d = blocks.length;
        int n = 1;
        hammingDistance = hamming();
        manhattanDistance = manhattan();
    }

    public int dimension() {
        return d;
    }

    public int hamming() {
        if (hammingDistance < 0) {
            hammingDistance = 0;
            for (int i = 0; i < d; i++) {
                for (int j = 0; j < d; j++) {
                    // Record the blank location
                    if (blocks[i][j] == 0) {
                        blankCoordinate = new Coordinate(i, j);
                    }
                    if ((i == d - 1) && (j == d - 1)) break;
                    if (blocks[i][j] != i * d + j + 1) hammingDistance++;
                }
            }
        }
        return hammingDistance;
    }

    public int manhattan() {
        if (manhattanDistance < 0) {
            manhattanDistance = 0;
            for (int i = 0; i < d; i++) {
                for (int j = 0; j < d; j++) {
                    if (blocks[i][j] == 0) continue;
                    int rowDistance;
                    int colDistance;
                    rowDistance = Math.abs(((blocks[i][j] - 1) / d) - i);
                    colDistance = Math.abs(((blocks[i][j] - 1) % d) - j);
                    manhattanDistance = manhattanDistance + rowDistance + colDistance;
                }
            }
        }
        return manhattanDistance;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public Board twin() {
        int[][] twinBlocks = new int[d][d];
        int temp = 0;
        int counter = 0;
        Coordinate p = null;
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                if (counter == 0 && blocks[i][j] != 0) {
                    // save the first none-zero value and its coordinate
                    temp = blocks[i][j];
                    p = new Coordinate(i, j);
                    counter += 1;
                } else if (counter == 1 && blocks[i][j] != 0) {
                    // swap with the second non-zero value
                    twinBlocks[p.x][p.y] = blocks[i][j];
                    twinBlocks[i][j] = temp;
                    counter += 1;
                } else {
                    twinBlocks[i][j] = blocks[i][j];
                }
            }
        }
        return new Board(twinBlocks);
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        if (this.hamming() != that.hamming()) return false;
        if (this.manhattan() != that.manhattan()) return false;
        return blocksEqual(this.blocks, that.blocks);
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighborsList = new ArrayList<>();
        Coordinate[] coordinates = {
                new Coordinate(blankCoordinate.x, blankCoordinate.y - 1),
                new Coordinate(blankCoordinate.x - 1, blankCoordinate.y),
                new Coordinate(blankCoordinate.x + 1, blankCoordinate.y),
                new Coordinate(blankCoordinate.x, blankCoordinate.y + 1)
        };

        for (Coordinate c : coordinates) {
            if (c.x >= 0 && c.x <= d-1 && c.y >= 0 && c.y <= d-1) {
                neighborsList.add(new Board(swap(copy2DArray(blocks), c, blankCoordinate)));
            }
        }

        return neighborsList;
    }

    private int[][] swap(int[][] b, Coordinate c1, Coordinate c2) {
        int temp = b[c1.x][c1.y];
        b[c1.x][c1.y] = b[c2.x][c2.y];
        b[c2.x][c2.y] = temp;
        return b;
    }

    private int[][] copy2DArray(int[][] arr) {
        int[][] copy = new int[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                copy[i][j] = arr[i][j];
            }
        }
        return copy;
    }

    private boolean blocksEqual(int[][] x, int[][] y) {
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                if (x[i][j] != y[i][j])
                    return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(d + "\n");
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        int[][] blocks = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
//        int[][] blocks = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        Board board = new Board(blocks);
        System.out.println(board.hamming());
        System.out.println(board.isGoal());

//        System.out.println(board.twin());
    }
}

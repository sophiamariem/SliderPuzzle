/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Board {
    private final int size;
    private final int[][] tiles;
    private final int manhattan;
    private final int hamming;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }

        this.tiles = tiles.clone();
        this.size = tiles[0].length;
        this.manhattan = manhattan();
        this.hamming = hamming();
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(size);
        s.append("\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return this.size;
    }

    // number of tiles out of place
    public int hamming() {
        if (hamming != 0) {
            return hamming;
        }
        int count = 0;
        int correctPos = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != correctPos && tiles[i][j] != 0) {
                    count++;
                }
                correctPos++;
            }
        }

        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattan != 0) {
            return manhattan;
        }
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != 0) {
                    count += Math.abs(i - ((tiles[i][j] - 1) / size)) + Math
                            .abs(j - ((tiles[i][j] - 1) % size));
                }
            }
        }
        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != 0) {
                    int pos = (i * size) + j + 1;
                    if (tiles[i][j] != pos) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }

        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }

        Board other = (Board) y;
        return Objects.equals(this.size, other.size) &&
                Arrays.deepEquals(this.tiles, other.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new LinkedList<>();

        int x = 0;
        int y = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] == 0) {
                    x = i;
                    y = j;
                }
            }
        }

        if (x > 0) {
            neighbors.add(new Board(exchange(x, y, x - 1, y)));
        }
        if (x < size - 1) {
            neighbors.add(new Board(exchange(x, y, x + 1, y)));
        }
        if (y > 0) {
            neighbors.add(new Board(exchange(x, y, x, y - 1)));
        }
        if (y < size - 1) {
            neighbors.add(new Board(exchange(x, y, x, y + 1)));
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(tiles);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1; j++) {
                if (tiles[i][j] != 0 && tiles[i][j + 1] != 0) {
                    twin = new Board(exchange(i, j, i, j + 1));
                }
            }
        }
        return twin;
    }

    private int[][] exchange(int i, int j, int k, int m) {
        int[][] copy = new int[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                copy[x][y] = tiles[x][y];
            }
        }

        int temp = copy[i][j];
        copy[i][j] = copy[k][m];
        copy[k][m] = temp;

        return copy;
    }
}

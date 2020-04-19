/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Solution solution;
    private Boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        MinPQ<Solution> queue = new MinPQ<>();
        queue.insert(new Solution(initial));

        MinPQ<Solution> twinQueue = new MinPQ<>();
        twinQueue.insert(new Solution(initial.twin()));

        while (true) {
            solution = calculateNext(queue);
            if (solution != null || calculateNext(twinQueue) != null) {
                return;
            }
        }
    }

    private Solution calculateNext(MinPQ<Solution> q) {
        if (!q.isEmpty()) {
            Solution sol = q.delMin();
            if (sol.board.isGoal()) {
                return sol;
            }
            Iterable<Board> neighbors = sol.board.neighbors();
            for (Board neighbor : neighbors) {
                if (sol.previous == null || !neighbor.equals(sol.previous.board)) {
                    q.insert(new Solution(neighbor, sol));
                }
            }
        }
        return null;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (solvable == null) {
            solvable = solution != null;
        }
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return isSolvable() ? solution.moves : -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }

        Stack<Board> solutionSeq = new Stack<>();
        Solution sol = solution;
        while (sol.previous != null) {
            solutionSeq.push(sol.board);
            sol = sol.previous;
        }

        solutionSeq.push(sol.board);
        return solutionSeq;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private class Solution implements Comparable<Solution> {
        private final Board board;
        private final int moves;
        private final Solution previous;
        private final int manhattan;

        public Solution(Board board) {
            this.board = board;
            this.moves = 0;
            this.previous = null;
            this.manhattan = board.manhattan();
        }

        public Solution(Board board, Solution previous) {
            this.board = board;
            this.moves = previous.moves + 1;
            this.previous = previous;
            this.manhattan = board.manhattan();
        }

        public int compareTo(Solution other) {
            return (manhattan - other.manhattan) + (this.moves - other.moves);
        }
    }

}

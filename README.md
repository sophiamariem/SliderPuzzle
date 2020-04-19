# Slider Puzzle

Write a program to solve the 8-puzzle problem (and its natural generalizations) using the A* search algorithm.

## The problem

The 8-puzzle is a sliding puzzle that is played on a 3-by-3 grid with 8 square tiles labeled 1 through 8, plus a blank square. The goal is to rearrange the tiles so that they are in row-major order, using as few moves as possible. You are permitted to slide tiles either horizontally or vertically into the blank square. The following diagram shows a sequence of moves from an initial board (left) to the goal board (right).

![8puzzle4moves](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/4moves.png)

## String representation
The toString() method returns a string composed of n + 1 lines. The first line contains the board size n; the remaining n lines contains the n-by-n grid of tiles in row-major order, using 0 to designate the blank square.

![String representation](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/string-representation.png)

## Hamming and Manhattan distances
To measure how close a board is to the goal board, we define two notions of distance. The Hamming distance betweeen a board and the goal board is the number of tiles in the wrong position. The Manhattan distance between a board and the goal board is the sum of the Manhattan distances (sum of the vertical and horizontal distance) from the tiles to their goal positions.

![Hamming and Manhattan distances](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/hamming-manhattan.png)

## Comparing two boards for equality
Two boards are equal if they are have the same size and their corresponding tiles are in the same positions. The equals() method is inherited from java.lang.Object, so it must obey all of Java’s requirements.

## Neighboring boards
The neighbors() method returns an iterable containing the neighbors of the board. Depending on the location of the blank square, a board can have 2, 3, or 4 neighbors.

![Neighboring boards](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/neighbors3.png)

## Performance requirements
Your implementation should support all Board methods in time proportional to n2 (or better) in the worst case.

## A* search
Now, we describe a solution to the 8-puzzle problem that illustrates a general artificial intelligence methodology known as the A* search algorithm. We define a search node of the game to be a board, the number of moves made to reach the board, and the previous search node. First, insert the initial search node (the initial board, 0 moves, and a null previous search node) into a priority queue. Then, delete from the priority queue the search node with the minimum priority, and insert onto the priority queue all neighboring search nodes (those that can be reached in one move from the dequeued search node). Repeat this procedure until the search node dequeued corresponds to the goal board.

The efficacy of this approach hinges on the choice of priority function for a search node. We consider two priority functions:

 - _The Hamming priority function_ is the Hamming distance of a board plus the number of moves made so far to get to the search node. Intuitively, a search node with a small number of tiles in the wrong position is close to the goal, and we prefer a search node if has been reached using a small number of moves.
 - _The Manhattan priority function_ is the Manhattan distance of a board plus the number of moves made so far to get to the search node.

To solve the puzzle from a given search node on the priority queue, the total number of moves we need to make (including those already made) is at least its priority, using either the Hamming or Manhattan priority function. Why? Consequently, when the goal board is dequeued, we have discovered not only a sequence of moves from the initial board to the goal board, but one that makes the fewest moves. (Challenge for the mathematically inclined: prove this fact.)

## Game tree
One way to view the computation is as a game tree, where each search node is a node in the game tree and the children of a node correspond to its neighboring search nodes. The root of the game tree is the initial search node; the internal nodes have already been processed; the leaf nodes are maintained in a priority queue; at each step, the A* algorithm removes the node with the smallest priority from the priority queue and processes it (by adding its children to both the game tree and the priority queue).

For example, the following diagram illustrates the game tree after each of the first three steps of running the A* search algorithm on a 3-by-3 puzzle using the Manhattan priority function.

![8puzzle game tree](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/neighbors3.png)

## Implementation requirement
To implement the A* algorithm, you must use the [MinPQ](https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/MinPQ.html) data type for the priority queue.

## Two optimizations
To speed up your solver, implement the following two optimizations:

 - _The critical optimization._ A* search has one annoying feature: search nodes corresponding to the same board are enqueued on the priority queue many times (e.g., the bottom-left search node in the game-tree diagram above). To reduce unnecessary exploration of useless search nodes, when considering the neighbors of a search node, don’t enqueue a neighbor if its board is the same as the board of the previous search node in the game tree.
![8puzzle critical optimization](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/critical-optimization.png)
 - _Caching the Hamming and Manhattan priorities._ To avoid recomputing the Manhattan priority of a search node from scratch each time during various priority queue operations, pre-compute its value when you construct the search node; save it in an instance variable; and return the saved value as needed. This caching technique is broadly applicable: consider using it in any situation where you are recomputing the same quantity many times and for which computing that quantity is a bottleneck operation.

## Detecting unsolvable boards
Not all initial boards can lead to the goal board by a sequence of moves, including these two:

![unsolvable slider puzzles](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/unsolvable.png)

To detect such situations, use the fact that boards are divided into two equivalence classes with respect to reachability:

 - Those that can lead to the goal board
 - Those that can lead to the goal board if we modify the initial board by swapping any pair of tiles (the blank square is not a tile).
(Difficult challenge for the mathematically inclined: prove this fact.) To apply the fact, run the A* algorithm on two puzzle instances—one with the initial board and one with the initial board modified by swapping a pair of tiles—in lockstep (alternating back and forth between exploring search nodes in each of the two game trees). Exactly one of the two will lead to the goal board.

***

Full specification found [here](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/specification.php)

***



import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Solver {
    private boolean solved = false;
    private SearchNode searchNode;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode predecessor;
        private final int moves;
        private final int priority;

        public SearchNode(Board board, SearchNode predecessor) {
            this.board = board;
            this.predecessor = predecessor;
            if (predecessor == null) {
                this.moves = 0;
            } else {
                this.moves = predecessor.moves + 1;
            }
            this.priority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(SearchNode node) {
            if (this.priority < node.priority) return -1;
            if (this.priority == node.priority) {
                if (this.board.manhattan() < node.board.manhattan()) return -1;
                if (this.board.manhattan() > node.board.manhattan()) return 1;
                return 0;
            }
            return 1;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() { return moves;}

        public Iterable<SearchNode> neighbors() {
            List<SearchNode> neighborNodes = new ArrayList<SearchNode>();
            for (Board b : board.neighbors()) {
                if (predecessor == null || !b.equals(predecessor.getBoard())) {
                    neighborNodes.add(new SearchNode(b, this));
                }
            }
            return neighborNodes;
        }

        public boolean isGoal() {
            return board.isGoal();
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Initial board can't be null.");
        MinPQ<SearchNode> queue = new MinPQ<>();
        MinPQ<SearchNode> queueTwin = new MinPQ<>();
        searchNode = new SearchNode(initial,null);
        queue.insert(searchNode);
        SearchNode searchNodeTwin = new SearchNode(initial.twin(), null);
        queueTwin.insert(searchNodeTwin);
        while (!queue.isEmpty()) {
//            System.out.println("steps = " + nMoves);
//            printQueue(queue);
            searchNode = queue.delMin();
            searchNodeTwin = queueTwin.delMin();

            if (searchNode.isGoal()) {
                solved = true;
                break;
            }
            if (searchNodeTwin.isGoal()) {
                break;
            }

            Iterable<SearchNode> neighbors = searchNode.neighbors();
            Iterable<SearchNode> neighborsTwin = searchNodeTwin.neighbors();
            for (SearchNode neighbor : neighbors) {
                queue.insert(neighbor);
            }
            for (SearchNode neighbor : neighborsTwin) {
                queueTwin.insert(neighbor);
            }
        }
    }

    public boolean isSolvable() {
        return solved;
    }

    public int moves() {
        if (!isSolvable()) return -1;
        return searchNode.getMoves();
    }

    public Iterable<Board> solution() {
        if (! isSolvable()) return null;
        Stack<Board> steps = new Stack<>();
        SearchNode node = searchNode;
        while (node != null) {
            steps.push(node.getBoard());
            node = node.predecessor;
        }
        return steps;
    }

    private void printQueue(MinPQ<SearchNode> queue) {
        for (SearchNode node : queue) {
            System.out.println("priority = " + node.priority);
            System.out.println("moves = " + node.moves);
            System.out.println("manhattan = " + node.board.manhattan());
            System.out.println(node.getBoard());
        }
    }

    public static void main(String[] args) {

    }
}

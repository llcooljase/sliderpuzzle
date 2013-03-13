/*************************************************************************
* Name: Jason Adleberg
* Login: adleberg
* Precept: P05

* Partner Name: None
* Partner Login: None
* Partner Precept: None
* */

public class Solver {
    
    // vars
    private MinPQ<SearchNode> pq; // as in N x N matrix
    private SearchNode solution;
    
    public Solver(Board initial) { //construct Solver from Board
        
        // is this solvable?
        if (!initial.isSolvable()) 
            throw new IllegalArgumentException("Not solvable!");
        
        // construct MinPQ
        pq = new MinPQ<SearchNode>();
        
        // Put in root
        SearchNode root = new SearchNode(initial, 0, null);  
        pq.insert(root);
        
        // Iterate through and find solution
        while (solution == null) {
            
            // 1. get node
            SearchNode currNode = pq.delMin();
            
            // 2. is it solution? if so we're done.
            if (currNode.returnBoard().isGoal()) {
                solution = currNode;
                return;
            }
            
            // 3. otherwise, get neighbors
            Iterable<Board> neighbors = currNode.board.neighbors();
            
            
            // 4. put them onto priority queue
            for (Board b : neighbors) {
                SearchNode s = new SearchNode(b, currNode.moves + 1, currNode);
                
                // 4a. and check not to insert grandparent (pruning rule).
                // this rule doesn't apply if we're at the first node.
                if (currNode.returnParent() != null) {
                  if (!s.returnBoard().equals(
                                 currNode.returnParent().returnBoard()))
                    pq.insert(s);
                  }
                else pq.insert(s);
            }
            
        }
    }
    
    public int moves() {
        return solution.moves;
    } 
    
    public Iterable<Board> solution() {
        
        // we have solution, so just iterate through
        // get all of its parents + make a stack outta it
        Stack<Board> solutionChain = new Stack<Board>();
        
        // get solution
        SearchNode currNode = solution;
        
        // solution board
        Board solBoard = currNode.returnBoard();
        //moves = 0;
        
        // put onto chain
        solutionChain.push(solBoard);
        
        // while parent doesn't equal null (definition of initial board)
            while (currNode.returnParent() != null) {
                // get board of parent
                Board tempBoard = currNode.returnParent().returnBoard();
                // add to stack
                solutionChain.push(tempBoard);
                // node is now parent
                currNode = currNode.returnParent();
                // increment moves
                //moves++;
            }
        return solutionChain;
    }

   public static void main(String[] args) {
    // create initial board from file
    In in = new In(args[0]);
    int N = in.readInt();
    int[][] blocks = new int[N][N];
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
    Stopwatch s = new Stopwatch();
    Board initial = new Board(blocks);
    Solver solver = new Solver(initial);
    StdOut.println(s.elapsedTime());
    StdOut.println("Minimum number of moves = " + solver.moves());
    for (Board board : solver.solution())
            StdOut.println(board);

}
    
    private class SearchNode implements Comparable<SearchNode> {
        // properties
        private Board board;
        private int moves;
        private SearchNode parent;
    
    // constructor
    private SearchNode(Board tempBoard, int tempMoves, SearchNode tempParent) {
        board = tempBoard;
        moves = tempMoves;
        parent = tempParent;
    }
    
    // return board
    public Board returnBoard() {
        return board;
    }
    
    // return parent
    public SearchNode returnParent() {
        return parent;
    }
    
    // return moves
    public int returnMoves() {
        return moves;
    }
    
    // implement compareTo
    public int compareTo(SearchNode that) {
        // priority function here is manhattan() + moves
        int thisTotal = board.hamming() + moves;
        int thatTotal = that.board.hamming() + that.moves;
        
        if (thisTotal < thatTotal) return -1;
        else if (thisTotal > thatTotal) return +1;
        else return 0;
    }
}
}
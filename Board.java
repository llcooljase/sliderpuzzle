/*************************************************************************
* Name: Jason Adleberg
* Login: adleberg
* Precept: P05

* Partner Name: None
* Partner Login: None
* Partner Precept: None
* */

public class Board {
    
    // vars
    private int N; // as in N x N matrix
    static private int BLANK_SQ = 1; // to account for blank square
    private int[][] grid; // to hold values
    
    public Board(int[][] blocks) { // construct board from N^2 array of blocks
        
        // get length
        this.N = blocks.length;
        grid = new int[N][N];
        
        // fill grid with blocks
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = blocks[i][j];
            }
        }
    }
                                           
    public int size() { // board size N
        return N;
    }
    
    public int hamming() { // number of blocks out of place
        
        // initialize count as 0
        int count = 0;
        
        // see how many blocks are out of place
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (grid[i][j] != i*N + j + BLANK_SQ && grid[i][j] != 0)
                    count++;
            
        // return
        return count;
    }
    
    public int manhattan() { // sum of Manhattan distances b/t blocks and goal
        
        // hold integers to track which row+column should be in
        int shouldBeInRow;
        int shouldBeInCol;
        
        // initialize count
        int count = 0;
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] != 0) {
                    
                    // determine which row and column block should be in
                    shouldBeInRow = (int) Math.floor((grid[i][j] - 1) / N);
                    shouldBeInCol = (grid[i][j] - 1) % N;
                    
                    // subtract w/Math.abs to get distance b/t spot and goal
                    count = count + Math.abs(shouldBeInRow - i)
                        + Math.abs(shouldBeInCol - j);
                }
            }
        }
        return count;
    }
    
    public boolean isGoal() { // is this board the goal board?
        
        // scan through board, look for an inconsistency
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (grid[i][j] != 0)
                    if (grid[i][j] != i*N + j + 1) return false;
        
        // if have gotten to here, then we everything is in order.
        return true;
    }
    
    public boolean isSolvable() { // is the board solvable?
        int inversions = 0;
        
        /* FOR ODD BOARD SIZE */
        if (N % 2 == 1) {
            
            // inversion finder
            for (int h = 0; h < N*N; h++) {
                for (int m = h; m < N*N; m++) {
                    
                    
                    int i = (int) Math.floor(h/N);
                    int j = h % N;
                    int k = (int) Math.floor(m/N);
                    int l = m % N;
                    
                            if (grid[k][l] != 0) {
                                if (grid[k][l] < grid[i][j]) {
                                    inversions++;
                    
                                }
                            }
                }
            }
            
            // if inversions odd, then no good
            return (inversions % 2 != 1);
        }
        
        /* EVEN BOARD SIZE */
        else {
            
            int blankrow = -1; // set as -1 for debuggning purposes
            // inversion finder
                for (int h = 0; h < N*N; h++) {
                for (int m = h; m < N*N; m++) {
                    int i = (int) Math.floor(h/N);
                    int j = h % N;
                    int k = (int) Math.floor(m/N);
                    int l = m % N;
                    if (grid[i][j] == 0) blankrow = i;
                            if (grid[k][l] != 0) {
                                if (grid[k][l] < grid[i][j]) {
                                    inversions++;
                                   // System.out.println("INV: "+inversions);
                                }
                            }
                }
            }
            //System.out.println(inversions + " " + blankrow);
            // if inversions+blankrow even, no good
            return ((inversions + blankrow) % 2 != 0);
        }
    }
    public boolean equals(Object y) {       // does this board equal y?
        if (y == this) return true; // equal to self
        if (y == null) return false; // false if null
        
        // if not a Board
        if (y.getClass() != this.getClass()) return false;
        
        // we can now safely cast as Board
        Board that = (Board) y;
        
        // return false if different N
        if (this.N != that.N) return false;
        
        // if same N, then return false
        else {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (this.grid[i][j] != that.grid[i][j]) return false;
                }
            }
        }
        return true; // if passed all above checks, then true.
    }
    public Iterable<Board> neighbors() { // return all neighboring boards
        
        // btwn 2 + 4 possibilities
        Stack<Board> stack = new Stack<Board>();
        
        // ints to track grid position of 0
        int zerosAtRow = -1; // set at -1 for debugging purposes, if doesn't
        int zerosAtCol = -1; // find 0 anywhere in the grid.
        
        // find 0
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 0) {
                    zerosAtRow = i;
                    zerosAtCol = j;
                }
            }
        }
        
        /* SWITCH ZERO WITH NORTH */
        if (zerosAtRow != 0) {
            // init new int[][]
            int[][] n = new int[N][N];
            // fill new board with same values
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    n[i][j] = grid[i][j];
                }
            }
            // switch zero with north value
            n[zerosAtRow][zerosAtCol] = n[zerosAtRow-1][zerosAtCol];
            n[zerosAtRow-1][zerosAtCol] = 0;
            // make board from int[][]
            Board no = new Board(n);
            // push onto stack
            stack.push(no);
            }
        
        /* SWITCH ZERO WITH WEST */
        if (zerosAtCol != N-1) {
            // init new int[][]
            int[][] e = new int[N][N];
            // fill new board with same values
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) 
                    e[i][j] = grid[i][j];
            }
            
            // switch zero with east value
            e[zerosAtRow][zerosAtCol] = e[zerosAtRow][zerosAtCol+1];
            e[zerosAtRow][zerosAtCol+1] = 0;
            // make board from int[][]
            Board ea = new Board(e);
            // push onto stack
            stack.push(ea);
            }
        
        /* SWITCH ZERO WITH SOUTH */
        if (zerosAtRow != N-1) {
            // init new int[][]
            int[][] s = new int[N][N];
            // fill new board with same values
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) 
                    s[i][j] = grid[i][j];
            }
            // switch zero with south value
            s[zerosAtRow][zerosAtCol] = s[zerosAtRow+1][zerosAtCol];
            s[zerosAtRow+1][zerosAtCol] = 0;
            // make board from int[][]
            Board so = new Board(s);
            // push onto stack
            stack.push(so);
            }
        
        /* SWITCH ZERO WITH WEST */
        if (zerosAtCol != 0) {
            // init new int[][]
            int[][] w = new int[N][N];
            // fill new board with same values
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) 
                    w[i][j] = grid[i][j];
            }
            // switch zero with west value
            w[zerosAtRow][zerosAtCol] = w[zerosAtRow][zerosAtCol-1];
            w[zerosAtRow][zerosAtCol-1] = 0;
            // make board from int[][]
            Board we = new Board(w);
            // push onto stack
            stack.push(we);
            }
        
        return stack;
    }
    
    public String toString() { // string representation of board
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                s.append(String.format("%2d ", grid[i][j]));
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) { // unit test
        int[][] h = {{1, 3}, {2, 0}};
        Board b = new Board(h);
        
        System.out.println(b);
        System.out.println(b.isSolvable());
        
    }
}
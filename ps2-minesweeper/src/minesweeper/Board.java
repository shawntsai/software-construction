/* Copyright (c) 2007-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * TODO: Specification
 * Representation board of minesweeper.
 * 
 * 
 */
public class Board {
    /** row length **/
    private int sizeX;
    
    /** col length **/
    private int sizeY;
//    private boolean debug;

    private char[][] board;
    /** true when bomb is located**/
    private boolean[][] bomb;
    /** the probability of a bomb exist in a cell**/
    private static final double BOMB_PROB = 0.2;
    /**used to denote there is no bombs surrond**/
    private final char NO_NEIGHBORBOMBS = ' ';
    /** this cell is untouched**/ 
    private final char UNTOUCHED = '-';
    /** this cell has been flagged**/ 
    private final char FLAGGED = 'F';
    
    /** eight direction surrounding the target cell**/
    private final int[][] dirs = new int[][] {{1, 0}, {1, 1}, {0, 1}, {0, -1}, {-1, 1}, {-1, -1}, {-1, 0}, {1, -1}}; 
    
    private static int numPlayer = 0;
    

    // Rep invariant:
    // sizeX and sizeY will not change
    // board only contains untouched '-', flagged 'F',
    // dug: '', [1-8] count neighbors that have a bomb
    
    // Thread Safety:
    // this class is threadsafe because only board, bomb, sizeX, sizeY are mutable object
    // And they will be take care by synchronization
    // the other are all final variable which are immutable
    
    // Abstraction Function:
    // represents board of minesweeper board[i][j] indicate the state of position (i, j)
    // bomb represents the position of bomb, bomb[i][j] = true mean there is a bomb 

    // Safety from rep exposure:
    //    All field are private;
   
    /**
     * Initialize of a random board with a probability of 20 % has a bomb in each location
     * others are unchecked 
     * @param sizeX represent the size of X
     * @param sizeY represent the size of Y
     */
    public Board(int sizeX, int sizeY) {
//        this.debug = debug;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.board = new char[sizeX][sizeY];
        this.bomb = new boolean[sizeX][sizeY];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = UNTOUCHED;
                double prob = Math.random();
                if (prob <= BOMB_PROB) {
                    bomb[i][j] = true;
                }
            }
        }
        checkRep();
    }
    /**
     * Init a board from file
     * file contain x y
     * and board matrix
     * @param file
     */
    public Board(File file) {
        try ( BufferedReader br = new BufferedReader(new FileReader(file.toPath().toString()))) {
            String line = br.readLine();
            String[] dim = line.split("\\s+");
            if (dim.length == 2) {
                this.sizeX = Integer.parseInt(dim[1]);
                this.sizeY = Integer.parseInt(dim[0]);
            }
            this.board = new char[sizeX][sizeY];
            this.bomb = new boolean[sizeX][sizeY];
            int x = 0;
            int y = 0;
            
            for (char[] array: this.board) {
                Arrays.fill(array, UNTOUCHED);
            }
            while((line = br.readLine()) != null) {
                for (char c: line.toCharArray()) {
                    if (c == '1') {
                        bomb[x][y++] = true;
                    }
                    else if (c == '0') {
                        y++;
                    }
                }
                y = 0;
                x++;
            }
//            for (boolean[] array: bomb) {
//                System.out.println(Arrays.toString(array));
//            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkRep();
    }
    
    /**
     *  
     * @param x
     * @param y
     * @return board matrix after dig
     */
    public synchronized String dig(int x, int y) {
        if (x < 0 || y < 0 || x >= board.length || y >= board[0].length || board[x][y] != UNTOUCHED) {
            return this.toString();
        }
        else if (bomb[x][y]) {
            // remove the bomb
            bomb[x][y] = false;
            // update number of count
            int surroundingBombs = getAdjacentBombs(x, y); 
            if ( surroundingBombs != 0) {
                board[x][y] = Integer.toString(surroundingBombs).charAt(0);
            }
            else {
                board[x][y] = UNTOUCHED;
            }
            updateSurroundingCount(x, y);
            dig(x, y);
//            return "BOOM!"+ System.lineSeparator();
            return "BOOM!";
        }
        digRecursive(x, y);
        checkRep();
        return this.toString();
    }
    
    private synchronized void updateSurroundingCount(int x, int y) {
        for (int[] dir: dirs) {
            int _x = dir[0] + x;
            int _y = dir[1] + y;
            
            if (valid(_x, _y) && board[_x][_y] != UNTOUCHED && 
                    board[_x][_y] != FLAGGED) {
                if (board[_x][_y] == '1') {
                    board[_x][_y] = NO_NEIGHBORBOMBS;
                }
                else {
                    board[_x][_y] = Integer.toString(Integer.parseInt("" + board[_x][_y]) - 1).charAt(0);
                }
            }
        }
        checkRep();
    }
    
    private synchronized void digRecursive(int x, int y) {
        // BFS
        Queue<int[]> q = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        q.add(new int[]{x, y});
        visited.add("" + x + "," + y);
        
        while(! q.isEmpty()) {
            int[] pos = q.poll();
            
            int surroundingBombs = getAdjacentBombs(pos[0], pos[1]); 
            if (board[pos[0]][pos[1]] == FLAGGED) {
                continue;
            }
            else if ( surroundingBombs != 0) {
                board[pos[0]][pos[1]] = Integer.toString(surroundingBombs).charAt(0);
            }
            else { // no surrounding bombs
                if (!bomb[pos[0]][pos[1]]) {
                    board[pos[0]][pos[1]] = NO_NEIGHBORBOMBS;
                }
                for (int[] dir: dirs) {
                    int _x = dir[0] + pos[0];
                    int _y = dir[1] + pos[1];
                    if (valid(_x, _y) && ! visited.contains(""+ _x + "," + _y)) {
                        q.add(new int[] {_x, _y});
                        visited.add("" + _x + "," + _y);
                    }
                }
            }
        }
    }
    
    private boolean valid(int x, int y) {
        return (x >= 0 && y >= 0 && x < board.length && y < board[0].length);
    }
    
    /**
     * get number of adjacent bombs in surrounding 8 position
     * @param x
     * @param y
     * @return number of bombs, must be 0 - 8
     */
    private synchronized int getAdjacentBombs(int x, int y) {
        int count = 0;
        for (int[] dir: dirs) {
            int _x = x + dir[0];
            int _y = y + dir[1];
            if (valid(_x, _y) && bomb[_x][_y]) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * flag on (x, y)  
     * @param x
     * @param y
     * @return board matrix after flagged
     */
    public synchronized String flag(int x, int y) {
        if (valid(x, y) && board[x][y] == UNTOUCHED) {
            board[x][y] = FLAGGED;
        }
        checkRep();
        return this.toString();
    }
    /**
     * deflag on (x, y) 
     * @param x
     * @param y
     * @return board matrix
     */
    public synchronized String deflag(int x, int y) {
        if (valid(x, y) && board[x][y] == FLAGGED) {
            board[x][y] = UNTOUCHED;
        }
        checkRep();
        return this.toString();
    }
    
    public void printBomb() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bomb.length; i++) {
            for (int j = 0; j < bomb[0].length; j++) {
                if (bomb[i][j])
                    sb.append('1');
                else sb.append('0');
                sb.append(' ');
            }
            sb.append(System.lineSeparator());
        }
        System.out.println(sb.toString());
    }
    
    /**
     * @return board matrix
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                sb.append(board[i][j]);
                if (j != board[i].length -1) {
                    sb.append(' ');
                }
            }
            sb.append(System.lineSeparator());
        }
        return sb.substring(0, sb.length() -1).toString();
        
//        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + FLAGGED;
        result = prime * result + NO_NEIGHBORBOMBS;
        result = prime * result + UNTOUCHED;
        result = prime * result + Arrays.hashCode(board);
        result = prime * result + Arrays.hashCode(bomb);
        result = prime * result + Arrays.hashCode(dirs);
        result = prime * result + sizeX;
        result = prime * result + sizeY;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Board other = (Board) obj;
        if (FLAGGED != other.FLAGGED)
            return false;
        if (NO_NEIGHBORBOMBS != other.NO_NEIGHBORBOMBS)
            return false;
        if (UNTOUCHED != other.UNTOUCHED)
            return false;
        if (!Arrays.deepEquals(board, other.board))
            return false;
        if (!Arrays.deepEquals(bomb, other.bomb))
            return false;
        if (!Arrays.deepEquals(dirs, other.dirs))
            return false;
        if (sizeX != other.sizeX)
            return false;
        if (sizeY != other.sizeY)
            return false;
        return true;
    }
    void checkRep() {
        assert board.length == sizeX;
        assert board[0].length == sizeY;
        assert bomb.length == sizeX;
        assert bomb[0].length == sizeY;
        assert Board.numPlayer >= 0;
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                assert (board[i][j] == FLAGGED || board[i][j] == UNTOUCHED || (board[i][j] >= '1' && board[i][j] <= '8') || board[i][j] == NO_NEIGHBORBOMBS);
                if (board[i][j] != FLAGGED && board[i][j] != UNTOUCHED) {
                    int surroundingBombs = this.getAdjacentBombs(i, j);
                    if (surroundingBombs == 0) assert board[i][j] == NO_NEIGHBORBOMBS;
                    else assert board[i][j] == String.valueOf(surroundingBombs).charAt(0);
                }  
            }
        }
    }
    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
    
    public synchronized int addPlayer() {
        Board.numPlayer += 1;
        return Board.numPlayer;
    }
    
    public synchronized void removePlayer() {
        Board.numPlayer -= 1;
        assert Board.numPlayer >= 0;
    }
}

/* Copyright (c) 2007-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import java.io.File;
import java.io.IOException;
import static org.junit.Assert.*;



import org.junit.Test;
/**
 * TODO: Description
 */
public class BoardTest {
    
    // TODO: Testing strategy
    /**
     * Test strategy:
     * Dig
     * 
     * 
     */

    private static final String BOARDS_PKG = "/Users/shawn/Documents/workspace/MIT6005/ps2-minesweeper/test/minesweeper/server/boards/";
    
    private static File loadFile(String boardFile) throws IOException {
        return new File(BOARDS_PKG + boardFile);
    }
    
    private Board getTestBoard(String boardFile) {
        try {
            Board board = new Board(loadFile(boardFile));
            return board;
        }
        catch (IOException ie) {
            System.out.print("can not load test file" + boardFile);
        }
        return null;
    }

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    @Test
    public void testConstructor() throws IOException {
        try {
            Board board = new Board(loadFile("board"));
            String row = "- - - - - - -\n";
            String expected = "";
            for (int i = 0; i < 7; i++) {
                expected = expected + row;
            }
            assertEquals(expected.substring(0, expected.length() - 1), board.toString());
        }
        catch (IOException ie){
            throw new IOException("can not load file");
        }
    }
    
    @Test
    public void testDigBomb() {
        Board board =  getTestBoard("test1.txt");
        final int bombX = 1;
        final int bombY = 1;
        assertEquals("BOOM!", board.dig(bombY, bombX));
    }
    
    @Test
    public void testDigNearBomb() {
        Board board =  getTestBoard("test1.txt");
        String ls = System.lineSeparator();
        assertEquals("- - -" + ls +
                     "- - -" + ls +
                     "- - 1" + ls +
                     "- - -", board.dig(2, 2));
    }
    
    @Test
    public void testDigNotNearBomb() {
        Board board = getTestBoard("test2.txt");
        assertEquals("     \n"+
                     "1 1  \n"+
                     "- 1  ", board.dig(2,  2));
    }
    
    @Test
    public void testDigOutOfBound() {
        Board board = getTestBoard("test2.txt");
        assertEquals("- - -\n"+
                     "- - -\n"+
                     "- - -", board.dig(3, 3));
    }
    
    @Test
    public void testDigNearManyBombs() {
        Board board = getTestBoard("test3.txt");
        assertEquals("- - -\n"+
                     "- 2 1\n"+
                     "- 1  \n"+
                     "- 1  ", board.dig(3, 2));
    }
    
    @Test
    public void testDigBombAfterRemoveSurroundingUpdate() {
        Board board = getTestBoard("test3.txt");
        board.dig(3, 2);
        board.dig(0, 0);
        assertEquals("1 - -\n"+
                     "- 1 1\n"+
                     "- 1  \n"+
                     "- 1  ", board.toString());
    }
    
    @Test
    public void testDigBombAfterRemove() {
        Board board =  getTestBoard("test1.txt");
        final int bombX = 1;
        final int bombY = 1;
        board.dig(bombY, bombX);
        assertEquals("     \n"+
                     "     \n"+
                     "     \n"+
                     "     ", board.toString());
        
    }
    
    @Test
    public void testFlag() {
        Board board = getTestBoard("test2.txt");
        assertEquals("F - -\n" +
                     "- - -\n" +
                     "- - -", board.flag(0, 0));
    }
    
    @Test
    public void testDeflag() {
        Board board = getTestBoard("test2.txt");
        board.flag(0, 0);
        board.deflag(0, 0);
        assertEquals("- - -\n" +
                     "- - -\n" +
                     "- - -", board.toString());
    }
    
    @Test
    public void testDigBesideFlagged() {
        Board board = getTestBoard("test3.txt");
        board.flag(1, 1);
        assertEquals("- - -\n"+
                     "- F 1\n"+
                     "- 1  \n"+
                     "- 1  ", board.dig(3, 2));
    }
    
    @Test
    public void testDigInsideFlagged() {
        Board board = getTestBoard("test3.txt");
        board.flag(2, 1);
        board.flag(3, 1);
        board.flag(1, 2);
        assertEquals("- - -\n"+
                     "- 2 F\n"+
                     "- F  \n"+
                     "- F  ", board.dig(3,2));
    }
    
}

/* Copyright (c) 2007-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper.server;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;




/**
 * TODO
 */
public class MinesweeperServerTest {
    
    private static final String LOCALHOST = "127.0.0.1";
    private static int PORT;
    private static final int MAX_CONNECTION_ATTEMPS = 10;
    private static final String BOARDS_PKG = "minesweeper/server/boards/";
    
    
    private static Thread startMinesweeperServer(String boardFile) throws IOException {
        PORT = 4000 + new Random().nextInt(1 << 15);

        final URL boardURL = ClassLoader.getSystemClassLoader().getResource(BOARDS_PKG + boardFile);

        if (boardURL == null) {
            throw new IOException("Fail to locate resource" + boardURL);
        }
        String boardPath;
        try {
            boardPath = new File(boardURL.toURI()).getAbsolutePath();
        } catch (URISyntaxException urise) {
            throw new IOException("Invalid URL " + boardURL, urise); 
        }
        
        final String[] args = new String[] {
                "--debug",
                "--port", Integer.toString(PORT),
                "--file", boardPath
        };
        Thread serverThread = new Thread(() -> MinesweeperServer.main(args));
        serverThread.start();
        return serverThread;
    }
    
    private static Socket connectToMinesweeperServer(Thread server) throws IOException {
        int attempts = 0;
        while (true) {
            try {
                Socket socket = new Socket(LOCALHOST, PORT);
                socket.setSoTimeout(3000);
                return socket;
            } catch(ConnectException ce) {
                if ( ! server.isAlive()) {
                    throw new IOException("Server thread is not running");
                }
                if (++attempts > MAX_CONNECTION_ATTEMPS) {
                    throw new IOException("Exceeded max connection attemps");
                }
                try {Thread.sleep(attempts *10); } catch(InterruptedException ie) {};
            }
        }
    }
    
    @Test(timeout = 10000)
    public void testSmall() throws IOException {
        Thread thread = startMinesweeperServer("test1.txt");
        Socket socket = connectToMinesweeperServer(thread);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertTrue("expected Hello message", in.readLine().startsWith("Welcome"));

        out.println("look");
        assertEquals("- - -", in.readLine());
        assertEquals("- - -", in.readLine());
        assertEquals("- - -", in.readLine());
        assertEquals("- - -", in.readLine());

        out.println("dig 2 0");
        assertEquals("- - 1", in.readLine());
        assertEquals("- - -", in.readLine());
        assertEquals("- - -", in.readLine());
        assertEquals("- - -", in.readLine());
        
        out.println("dig 1 1");
        assertEquals("BOOM!", in.readLine());
        
        out.println("look"); // debug mode is on
        
        assertEquals("     ", in.readLine());
        assertEquals("     ", in.readLine());
        assertEquals("     ", in.readLine());
        assertEquals("     ", in.readLine());

        out.println("bye"); 
        socket.close();
        
    }
    
    @Test(timeout = 10000)
    public void testBoard() throws IOException {
        Thread thread = startMinesweeperServer("board");
        Socket socket = connectToMinesweeperServer(thread);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertTrue("expected HELLO message", in.readLine().startsWith("Welcome"));

        out.println("look");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        out.println("dig 3 1");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - 1 - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        out.println("dig 4 1");
        assertEquals("BOOM!", in.readLine());

        out.println("look"); // debug mode is on
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("1 1          ", in.readLine());
        assertEquals("- 1          ", in.readLine());

        out.println("bye");
        socket.close();
    }
    
    
    @Test(timeout = 10000)
    public void testMultiConnection() throws IOException {
        Thread thread = startMinesweeperServer("test1.txt");
        Socket socket = connectToMinesweeperServer(thread);
        Socket socket2 = connectToMinesweeperServer(thread);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        BufferedReader in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        PrintWriter out2 = new PrintWriter(socket2.getOutputStream(), true);


        assertTrue("expected Hello message", in.readLine().startsWith("Welcome"));
        assertTrue("expected Hello message", in2.readLine().startsWith("Welcome"));

        out.println("look");
        assertEquals("- - -", in.readLine());
        assertEquals("- - -", in.readLine());
        assertEquals("- - -", in.readLine());
        assertEquals("- - -", in.readLine());

        out2.println("look");
        assertEquals("- - -", in2.readLine());
        assertEquals("- - -", in2.readLine());
        assertEquals("- - -", in2.readLine());
        assertEquals("- - -", in2.readLine());


        out.println("dig 2 0");
        assertEquals("- - 1", in.readLine());
        assertEquals("- - -", in.readLine());
        assertEquals("- - -", in.readLine());
        assertEquals("- - -", in.readLine());
        
        out2.println("dig 2 0");
        assertEquals("- - 1", in2.readLine());
        assertEquals("- - -", in2.readLine());
        assertEquals("- - -", in2.readLine());
        assertEquals("- - -", in2.readLine());
 
        out.println("dig 1 1");
        assertEquals("BOOM!", in.readLine());
        
        out2.println("look"); // out2 should also see the change
        out.println("look"); // debug mode is on
        assertEquals("     ", in.readLine());
        assertEquals("     ", in.readLine());
        assertEquals("     ", in.readLine());
        assertEquals("     ", in.readLine());
       
        assertEquals("     ", in2.readLine());
        assertEquals("     ", in2.readLine());
        assertEquals("     ", in2.readLine());
        assertEquals("     ", in2.readLine());

        out.println("bye"); 
        socket.close();
    }
}
    
    

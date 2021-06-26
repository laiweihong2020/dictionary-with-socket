package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App 
{
    /**
     * The main function of the server
     * @param args Terminal inputs
     */
    public static void main( String[] args )
    {
        int port = 1234;
        int threadCount = 10;

        try {
            port = Integer.parseInt(args[0]);
            if(args[1] != null) {
                threadCount = Integer.parseInt(args[1]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No port number entered. Port set to 1234 by default.");
        }
        System.out.println(threadCount);
        boolean isRunning = false;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        // Initiate server socket
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            printAddress(serverSocket);
            DatabaseHandler db = new HashMapDataHandler();
            isRunning = true;
            while(isRunning) {
                Socket socket = serverSocket.accept();
                Runnable requestHandler = new InitialRequestHandler(socket, db);
                executor.execute(requestHandler);
            }
            serverSocket.close();
        } catch (SocketException e) {
            System.out.println("Address " + port + " is in use. Try another port.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printAddress(ServerSocket s) {
        System.out.println(s.getLocalSocketAddress());
    }
}

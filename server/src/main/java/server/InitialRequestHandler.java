package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.google.gson.Gson;

/**
 * The initial request handler is the primary handler that filters requests to their
 * specific request handlers.
 */
public class InitialRequestHandler implements Runnable{
    private Socket socket;
    private DatabaseHandler db;
    private Packet request;
    private Packet response;

    /**
     * Creates a new initial request handler
     * @param socket The socket at which the client is connected to
     * @param db The database handler
     */
    public InitialRequestHandler(Socket socket, DatabaseHandler db) {
        this.db = db;
        this.socket = socket;
        this.request = new Packet();
        this.response = new Packet();
    }

    @Override
    public void run() {
        try {
            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);
            String jsonRequest = new String(dis.readUTF());

            // Convert json string to Java object
            Gson gson = new Gson();
            request = gson.fromJson(jsonRequest, Packet.class);

            // Get the operation mode from the packet
            int operationMode = request.getOperationMode();
            switch(operationMode) {
                case 0:
                    Thread queryRequestHandleThread = new Thread(new QueryRequestHandler(request,response, db));
                    queryRequestHandleThread.start();
                    try {
                        queryRequestHandleThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    Thread additionRequestHandleThread = new Thread(new AdditionRequestHandler(request, response, db));
                    additionRequestHandleThread.start();
                    try {
                        additionRequestHandleThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    Thread removalRequestHandleThread = new Thread(new RemovalRequestHandler(request, response, db));
                    removalRequestHandleThread.start();
                    try {
                        removalRequestHandleThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    Thread updateRequestHandleThread = new Thread(new UpdateRequestHandler(request, response, db));
                    updateRequestHandleThread.start();
                    try {
                        updateRequestHandleThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    Thread connectionTestRequestHandleThread = new Thread(new ConnectionTestRequesthandler(request, response));
                    connectionTestRequestHandleThread.start();
                    try {
                        connectionTestRequestHandleThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    Thread invalidRequestHandleThread = new Thread(new InvalidRequestHandler("INVALID_OP_CODE", response));
                    invalidRequestHandleThread.start();
                    try {
                        invalidRequestHandleThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }

            // Convert the reply to a json string
            String responseJson = gson.toJson(response);

            OutputStream socketOut = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(socketOut);
            dos.writeUTF(responseJson);
            dos.close();
            socketOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

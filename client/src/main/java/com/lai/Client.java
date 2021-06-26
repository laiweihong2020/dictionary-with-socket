package com.lai;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.google.gson.Gson;

/**
 * The client class handles with crafting the desired requests, as well as communications
 * to the server
 */
public class Client {
    private int port;
    private String ip;

    /**
     * Creates a new client
     * @param ip IP address 
     * @param port port number
     */
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Creates a new client
     * @param ip IP address
     * @param port port number but in String
     */
    public Client(String ip, String port) {
        this.ip = ip;
        this.port = Integer.parseInt(port);
    }

    /**
     * Crafts a getMeaning request packet and sends it to the server
     * @param word The word
     * @return The response packet
     * @throws UnknownHostException This happens if the client can't connect to the server
     * @throws IOException This is catch all for any IO related faults
     */
    public Packet getMeaning(String word) throws UnknownHostException, IOException {
        // Craft information in packet
        Packet request = new Packet();
        request.setWord(word);
        request.setOperationMode(0);

        // Json conversion
        Gson gson = new Gson();
        String requestJson = gson.toJson(request);
        
        String replyJson = send(requestJson);
        
        // Object conversion
        Packet reply = gson.fromJson(replyJson, Packet.class);
        return reply;
    }

    /**
     * Crafts a addWord request packet and sends it to the server
     * @param word The word
     * @param meaning The meaning
     * @return The response packet
     * @throws UnknownHostException This happens if the client can't connect to the server
     * @throws IOException This is a catch all for any IO related faults
     */
    public Packet addWord(String word, String meaning) throws UnknownHostException, IOException {
        // Craft information into the packet
        Packet request = new Packet();
        request.setWord(word);
        request.setMeaning(meaning);
        request.setOperationMode(1);

        Gson gson = new Gson();
        String requestJson = gson.toJson(request);

        String replyJson = send(requestJson);

        // Object conversion
        Packet reply = gson.fromJson(replyJson, Packet.class);
        return reply;
    }

    /**
     * Creates a removeWord request packet and sends it to the server
     * @param word The word
     * @return The response packet
     * @throws UnknownHostException This happens if the client can't connect to the server
     * @throws IOException This is a catch all for any IO related faults
     */
    public Packet removeWord(String word) throws UnknownHostException, IOException {
        Packet request = new Packet();
        request.setWord(word);
        request.setOperationMode(2);

        Gson gson = new Gson();
        String requestJson = gson.toJson(request);

        String replyJson = send(requestJson);

        // Object conversion
        Packet reply = gson.fromJson(replyJson, Packet.class);
        return reply;
    }

    /**
     * Creates a updateWord request packet and sends it to the server
     * @param word The word
     * @param meaning The meaning
     * @return The response packet
     * @throws UnknownHostException This happens if the client can't connect to the server
     * @throws IOException This is a catch all for any IO related faults
     */
    public Packet updateWord(String word, String meaning) throws UnknownHostException, IOException {
        // Craft information into the packet
        Packet request = new Packet();
        request.setWord(word);
        request.setMeaning(meaning);
        request.setOperationMode(3);

        Gson gson = new Gson();
        String requestJson = gson.toJson(request);

        String replyJson = "";
        replyJson = send(requestJson);
        // Object conversion
        Packet reply = gson.fromJson(replyJson, Packet.class);
        return reply;
    }

    /**
     * Creates a request packet that tests if the server is live and sends it to the server
     * @return The response packet
     * @throws SocketTimeoutException This happens if the client can't connect to the server
     * @throws IOException This is a catch all for any IO related faults
     */
    public Packet testConnection() throws SocketTimeoutException, IOException {
        // Craft information into the packet
        Packet request = new Packet();
        request.setOperationMode(4);

        Gson gson = new Gson();
        String requestJson = gson.toJson(request);

        String replyJson = "";
        replyJson = send(requestJson);

        // Object conversion
        Packet reply = gson.fromJson(replyJson, Packet.class);
        return reply;
    }

    /**
     * This sends the JSON string to the server
     * @param requestJson The JSON string
     * @return The JSON reply
     * @throws SocketTimeoutException The socket didn't reply within the timeout period
     * @throws IOException This is a catchall for any IO related faults
     */
    public String send(String requestJson) throws SocketTimeoutException, IOException{
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(this.ip, this.port), 6000);
        OutputStream socketOut = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(socketOut);
        dos.writeUTF(requestJson);

        String reply = "";
        boolean isWaitingReply = true;
        while(isWaitingReply) {
            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);
            reply = new String(dis.readUTF());

            if(!reply.isEmpty()) {
                isWaitingReply = false;
            }

            dis.close();
            in.close();
            socket.close();
        }
        return reply;
    }
}

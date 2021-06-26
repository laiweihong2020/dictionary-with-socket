package server;

/**
 * The connection test request handler handles requests that checks if the server is live or not
 */
public class ConnectionTestRequesthandler implements Runnable{
    private Packet packet;
    private Packet response;

    /**
     * Creates a new connection test request handler
     * @param packet The request packet
     * @param response The response packet
     */
    public ConnectionTestRequesthandler(Packet packet, Packet response) {
        this.packet = packet;
        this.response = response;
    }

    @Override
    public void run() {
        // Since the packet reached the server, connection is alive
        int operationMode = packet.getOperationMode();

        response.setOperationMode(operationMode);
        response.setIsOperationSuccess(true);
    }
}

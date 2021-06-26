package server;

import com.google.firebase.database.DatabaseException;

/**
 * The removal request handler deals with requests that removes words from the database
 */
public class RemovalRequestHandler implements Runnable{
    private Packet packet;
    private DatabaseHandler db;
    private Packet response;

    /**
     * Creates a new removal request handler
     * @param packet The request packet
     * @param response The response packet
     * @param db The database wrapper
     */
    public RemovalRequestHandler(Packet packet, Packet response, DatabaseHandler db) {
        this.packet = packet;
        this.db = db;
        this.response = response;
    }

    @Override
    public void run() {
        String word = packet.getWord();
        int operationMode = packet.getOperationMode();

        // Craft response packet
        // response.setWord(word);
        // response.setOperationMode(operationMode);
        // response.setIsOperationSuccess(isOperationSuccess);
        if(db.checkIfWordExists(word)) {
            try {
                db.removeWord(word); 
                // Craft success packet
                response.setWord(word);
                response.setOperationMode(operationMode);
                response.setIsOperationSuccess(true);               
            } catch (DatabaseException e) {
                // Craft failure packet
                response.setWord(word);
                response.setOperationMode(operationMode);
                response.setIsOperationSuccess(false);
                response.setDescription("Database Error");
            }

        } else {
            System.out.println("Word does not exist");

            // Craft failure packet
            response.setWord(word);
            response.setOperationMode(operationMode);
            response.setIsOperationSuccess(false);
            response.setDescription("Word does not exist");
        }
    }
}

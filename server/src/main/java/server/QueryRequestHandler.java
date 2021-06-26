package server;

import com.google.firebase.database.DatabaseException;

/**
 * The query request handler deals with the requests that want to know the meaning of a word
 */
public class QueryRequestHandler implements Runnable{
    private Packet packet;
    private DatabaseHandler db;
    private Packet response;

    /**
     * Creates a new query request handler
     * @param packet The request packet
     * @param response The response packet
     * @param db The database handler
     */
    public QueryRequestHandler(Packet packet, Packet response, DatabaseHandler db) {
        this.packet = packet;
        this.db = db;
        this.response = response;
    }

    @Override
    public void run() {
        String word = packet.getWord();
        int operationMode = packet.getOperationMode();
        try {
            String meaning = db.getMeaning(word);

            // Set the packet attributes
            response.setWord(word);
            response.setMeaning(meaning);
            response.setOperationMode(operationMode);
            response.setIsOperationSuccess(true);            
        } catch (DatabaseException e) {
            // Craft failure packet
            response.setWord(word);
            response.setOperationMode(operationMode);
            response.setIsOperationSuccess(false);
            response.setDescription("Database Error");
        }
    }
}

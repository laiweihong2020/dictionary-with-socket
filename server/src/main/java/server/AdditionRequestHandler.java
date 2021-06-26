package server;

import com.google.firebase.database.DatabaseException;

/**
 * The additional request handler handles requests that adds words into the dictionary
 */
public class AdditionRequestHandler implements Runnable{
    private Packet packet;
    private DatabaseHandler db;
    private Packet response;

    /**
     * Creates a new Addition Request Handler
     * @param packet The request packet
     * @param response The response packet
     * @param db Wrapper for database operations
     */
    public AdditionRequestHandler(Packet packet, Packet response, DatabaseHandler db) {
        this.packet = packet;
        this.db = db;
        this.response = response;
    }

    @Override
    public void run() {
        String word = packet.getWord();
        String meaning = packet.getMeaning();
        int operationMode = packet.getOperationMode();
        try {
            db.saveWord(word, meaning);

            // Craft response packet
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

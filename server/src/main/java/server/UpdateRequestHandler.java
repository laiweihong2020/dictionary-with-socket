package server;

import com.google.firebase.database.DatabaseException;

/**
 * The update request handler deals with requests that wants to update the meaning of the words in the 
 * dictionary
 */
public class UpdateRequestHandler implements Runnable{
    private Packet packet;
    private DatabaseHandler db;
    private Packet response;

    /**
     * Creates a new update request handler
     * @param packet The request packet
     * @param response The response packet
     * @param db The database handlet
     */
    public UpdateRequestHandler(Packet packet, Packet response, DatabaseHandler db) {
        this.packet = packet;
        this.db = db;
        this.response = response;
    }

    @Override
    public void run() {
        String word = packet.getWord();
        String meaning = packet.getMeaning();
        int operationMode = packet.getOperationMode();
        // Check if there is an entry in the db
        if(db.checkIfWordExists(word)) {
            // The word exists in the db
            try {
                // Remove the word instance from the database
                db.removeWord(word);

                // Add the word with the new meaning
                db.saveWord(word, meaning);  

                // Craft success packet
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

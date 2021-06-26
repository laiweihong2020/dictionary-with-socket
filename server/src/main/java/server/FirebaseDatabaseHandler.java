package server;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference.CompletionListener;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * A wrapper for firebase queries
 */
public class FirebaseDatabaseHandler implements DatabaseHandler{
    private DatabaseReference ref = null;
    private boolean hasInit = false; 

    /**
     * Creates a new Firebase Database Handler
     */
    public FirebaseDatabaseHandler() {
        init();
        hasInit = true;
    }

    /**
     * Checks if the Firebase database handler has initialised
     * @return The boolean value of whether the db handler has initialised
     */
    public boolean getHasInit() {
        return this.hasInit;
    }

    /**
     * The initialisation procedure for the Firebase Database Handler
     */
    private void init() {
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream(Param.PATH_TO_JSON);
        } catch (Exception e) {
            System.out.println("serviceAccount cannot be found");
        }

        FirebaseOptions options = null;
        try {
            options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://dictionary-ba468-default-rtdb.firebaseio.com/")
                .build();
        } catch (Exception e) {
            hasInit = false;
        }

        FirebaseApp.initializeApp(options);
        this.ref = FirebaseDatabase
            .getInstance()
            .getReference("/server");
    }
    
    /**
     * Saves a word in the firebase database
     * @param str The word
     * @param meaning The meaning of the word
     * @throws DatabaseException The operation is unsuccessful
     */
    public void saveWord(String str, String meaning) throws DatabaseException{
        final DatabaseFlag completionFlag = new DatabaseFlag(false);
        final DatabaseQueryObject dbObj = new DatabaseQueryObject();

        Word word = new Word(str, meaning);
        DatabaseReference wordRef = ref.child(str);
        wordRef.setValue(word, new CompletionListener(){
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if(error != null) {
                    System.out.println("Data cannot be saved");
                    dbObj.setIsSuccess(false);
                    completionFlag.setFlag(true);
                } else {
                    System.out.println("Data is saved");
                    completionFlag.setFlag(true);
                    dbObj.setIsSuccess(true);
                }
            }
        });
        while(!completionFlag.getFlag()) {

            if (!dbObj.getIsSuccess()) {
                throw new DatabaseException("Failed to fetch data");
            }

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the meaning of the word
     * @param word The word
     * @throws DatabaseException The exception if the operation is unsuccessful
     */
    public String getMeaning(String word) throws DatabaseException{
        final Word wordObj = new Word(word);
        final DatabaseFlag completionFlag = new DatabaseFlag(false);
        final DatabaseQueryObject dbObj = new DatabaseQueryObject();

        ref.child(word).child("meaning").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dataWord = dataSnapshot.getValue(String.class);
                wordObj.setMeaning(dataWord);
                completionFlag.setFlag(true);
                dbObj.setIsSuccess(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dbObj.setIsSuccess(false);
                completionFlag.setFlag(true);
            }
        });

        while(!completionFlag.getFlag()) {
            
            if (!dbObj.getIsSuccess()) {
                throw new DatabaseException("Failed to fetch data.");
            }

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return wordObj.getMeaning();
    }

    public HashMap<String, String> getAllEntries() throws DatabaseException {
        final DatabaseFlag completionFlag = new DatabaseFlag(false);
        final HashMap<String, String> map = new HashMap<String, String>();
        ref.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot dsp:snapshot.getChildren()) {
                    String word = (String) dsp.child("word").getValue();
                    String meaning = (String) dsp.child("meaning").getValue();
                    map.put(word, meaning);
                }
                completionFlag.setFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
            
        });
        

        while(!completionFlag.getFlag()) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * Removes a word from the dictionary
     * @param word The word
     * @throws DatabaseException The exception thrown when the Firebase operation is unsuccessful
     */
    public void removeWord(String word) throws DatabaseException{
        final DatabaseFlag completionFlag = new DatabaseFlag(false);
        final DatabaseQueryObject dbObj = new DatabaseQueryObject();

        DatabaseReference wordRef = ref.child(word);
        wordRef.removeValue(new CompletionListener(){
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if(error != null) {
                    dbObj.setIsSuccess(false);
                    completionFlag.setFlag(true);
                } else {
                    dbObj.setIsSuccess(true);
                    completionFlag.setFlag(true);
                }
            }
        });

        while(!completionFlag.getFlag()) {

            if (!dbObj.getIsSuccess()) {
                throw new DatabaseException("Failed to fetch data");
            }

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if the word exists in the database
     * @param word The word
     * @throws DatabaseException The eception thrown when a Firebase operation is unsuccessful
     */
    public boolean checkIfWordExists(String word) throws DatabaseException{
        final DatabaseFlag completionFlag = new DatabaseFlag(false);
        final DatabaseQueryObject dbObj = new DatabaseQueryObject();

        DatabaseReference wordRef = ref.child(word);
        wordRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    dbObj.setHasWord(true);
                    dbObj.setIsSuccess(true);
                }
                completionFlag.setFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError);
                dbObj.setIsSuccess(false);
                completionFlag.setFlag(true);
            }
        });

        while(!completionFlag.getFlag()) {

            if (!dbObj.getIsSuccess()) {
                System.out.println("Sone");
                throw new DatabaseException("Failed to fetch data");
            }

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return dbObj.getHasWord();
    }
}
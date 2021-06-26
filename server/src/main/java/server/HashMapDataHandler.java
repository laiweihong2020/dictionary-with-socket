package server;

import java.util.HashMap;

/**
 * The HashMap Data Handler handles read and write operations on the hashmap
 */
public class HashMapDataHandler implements DatabaseHandler{
    private HashMap<String, String> dictionary;
    private FirebaseDatabaseHandler offsiteDb;
    protected boolean isWriting = false;

    /**
     * Creates a new HashMap Data Handler
     */
    public HashMapDataHandler() {
        this.dictionary = new HashMap<String, String>();
        this.offsiteDb = new FirebaseDatabaseHandler();
        initialise();
    }

    public void initialise() {
        this.dictionary = this.offsiteDb.getAllEntries();
    }

    /**
     * Saves a word to the dictionary
     * @param str The word
     * @param meaning The meaning of the word
     */
    public synchronized void saveWord(String str, String meaning) {
        while(isWriting == true) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        isWriting = true;
        dictionary.put(str, meaning);
        isWriting = false;

        // Update the firebase on the action
        Runnable firebaseSave = () -> {
            offsiteDb.saveWord(str, meaning);
        };
        Thread thread = new Thread(firebaseSave);
        thread.start();

        notifyAll();
    }

    /**
     * Retrieves the meaning of the word
     * @param word The word
     */
    public synchronized String getMeaning(String word) {
        while(isWriting == true) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        String meaning = dictionary.get(word);
        return meaning;
    }

    /**
     * Removes a word from the dictionary
     * @param word The word
     */
    public synchronized void removeWord(String word) {
        while(isWriting == true) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        isWriting = true;
        dictionary.remove(word);
        isWriting = false;

        // Update the firebase on the action
        Runnable firebaseSave = () -> {
            offsiteDb.removeWord(word);
        };
        Thread thread = new Thread(firebaseSave);
        thread.start();
        
        notifyAll();
    }

    /**
     * Checks if the word exists in the dictionary
     * @param word The word
     */
    public synchronized boolean checkIfWordExists(String word) {
        while(isWriting == true) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        String meaning = dictionary.get(word);
        boolean hasWord = false;

        if(meaning == null) {
            hasWord = false;
        } else {
            hasWord = true;
        }
        return hasWord;
    }
}

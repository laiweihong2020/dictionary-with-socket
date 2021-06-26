package server;

/**
 * DatabaseHandler
 */
public interface DatabaseHandler {
    public void saveWord(String str, String meaning);
    public String getMeaning(String word);
    public void removeWord(String word);
    public boolean checkIfWordExists(String word);
}
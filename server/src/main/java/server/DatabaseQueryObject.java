package server;

/**
 * The database query object returns the status of the operation (ie has the word been set, does the word exists etc)
 */
public class DatabaseQueryObject {
    private boolean isSuccess;
    private boolean hasWord;

    /**
     * Creates a new database query object
     */
    public DatabaseQueryObject() {
        this.isSuccess = true;
        this.hasWord = false;
    }
    
    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean getIsSuccess() {
        return this.isSuccess;
    }

    public void setHasWord(boolean hasWord) {
        this.hasWord = hasWord;
    }

    public boolean getHasWord() {
        return this.hasWord;
    }
}
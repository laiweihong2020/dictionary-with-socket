package server;

/**
 * The database flag is used to keep track of progression during the asynchronous read and write operations of the 
 * database
 */
public class DatabaseFlag {
    private boolean completionFlag;

    /**
     * Creates a new database flag
     * @param completionFlag the initial boolean value of the flag
     */
    public DatabaseFlag(boolean completionFlag){
        this.completionFlag = completionFlag;
    }

    public boolean getFlag() {
        return this.completionFlag;
    }

    public void setFlag(boolean completionFlag) {
        this.completionFlag = completionFlag;
    }
}

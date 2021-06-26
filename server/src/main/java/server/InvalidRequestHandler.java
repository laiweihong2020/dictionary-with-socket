package server;

/**
 * The invalid request handler deals with requests that do not meet the 
 * predefined operation definition
 */
public class InvalidRequestHandler implements Runnable{
    private String ERROR_CODE;
    private Packet response;

    /**
     * Creates a new invalid request handler
     * @param ERROR_CODE The reason for the invalid request
     * @param response The response packet
     */
    public InvalidRequestHandler(String ERROR_CODE, Packet response) {
        this.ERROR_CODE = ERROR_CODE;
        this.response = response;
    }

    @Override
    public void run() {
        switch(ERROR_CODE) {
            case "INVALID_OP_CODE":
                invalidOperationCode();
                break;
            default:
                unknownError();
                break;
        }
    }

    private void invalidOperationCode() {
        // OPERATIONCODE: 80
        response.setOperationMode(80);
    }

    private void unknownError() {
        // OPERATIONCODE: 81
        response.setOperationMode(81);
    }
}

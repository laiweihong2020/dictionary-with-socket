package com.lai;

public class Packet {
    private String word;
    private String meaning;
    private int operationMode;
    private boolean isOperationSuccess;
    private String description;

    public Packet(){
        this.word = "";
        this.meaning = "";
        this.operationMode = 0;
        this.isOperationSuccess = false;
        this.description = "";
    }

    public String getWord() {
        return this.word;
    }

    public String getMeaning() {
        return this.meaning;
    }

    public int getOperationMode() {
        return this.operationMode;
    }

    public boolean getIsOperationSuccess() {
        return this.isOperationSuccess;
    }

    public String getDescription() {
        return this.description;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void setOperationMode(int operationMode) {
        this.operationMode = operationMode;
    }

    public void setIsOperationSuccess(boolean isOperationSuccess) {
        this.isOperationSuccess = isOperationSuccess;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


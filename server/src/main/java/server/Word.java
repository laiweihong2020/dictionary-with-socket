package server;

/**
 * The word object is only used in the Firebase Handler. I can't really remember why 
 * I created it when I have the packet object. Sorry ;-)
 */
public class Word {
    private String word;
    private String meaning;

    public Word(String word) {
        this.word = word;
        this.meaning = "";
    }

    public Word(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public String getWord() {
        return this.word;
    }

    public String getMeaning() {
        return this.meaning;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}

package model;

import java.io.Serializable;

public class Word implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String word;
    private String hint; // Link ảnh gợi ý

    public Word() {}

    public Word(String word, String hint) {
        this.word = word;
        this.hint = hint;
    }

    public Word(int id, String word, String hint) {
        this.id = id;
        this.word = word;
        this.hint = hint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", hint='" + hint + '\'' +
                '}';
    }
}

package model;

import java.io.Serializable;

public class Round implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int matchId;
    private int roundNumber;
    private Word word;
    private int player1_points;
    private int player2_points;

    public Round() {}

    public Round(int matchId, int roundNumber, Word word, int player1_points, int player2_points) {
        this.matchId = matchId;
        this.roundNumber = roundNumber;
        this.word = word;
        this.player1_points = player1_points;
        this.player2_points = player2_points;
    }

    public Round(int id, int matchId, int roundNumber, Word word, int player1_points, int player2_points) {
        this.id = id;
        this.matchId = matchId;
        this.roundNumber = roundNumber;
        this.word = word;
        this.player1_points = player1_points;
        this.player2_points = player2_points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public int getPlayer1_points() {
        return player1_points;
    }

    public void setPlayer1_points(int player1_points) {
        this.player1_points = player1_points;
    }

    public int getPlayer2_points() {
        return player2_points;
    }

    public void setPlayer2_points(int player2_points) {
        this.player2_points = player2_points;
    }

    @Override
    public String toString() {
        return "Round{" +
                "id=" + id +
                ", matchId=" + matchId +
                ", roundNumber=" + roundNumber +
                ", word=" + word +
                ", player1_points=" + player1_points +
                ", player2_points=" + player2_points +
                '}';
    }
}

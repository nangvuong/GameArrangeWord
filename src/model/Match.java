package model;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class Match implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private Playing player1;
    private Playing player2;
    private Date date;
    private List<Round> rounds;

    public Match() {}

    public Match(Playing player1, Playing player2, Date date) {
        this.player1 = player1;
        this.player2 = player2;
        this.date = date;
    }

    public Match(int id, Playing player1, Playing player2, Date date, List<Round> rounds) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.date = date;
        this.rounds = rounds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Playing getPlayer1() {
        return player1;
    }

    public void setPlayer1(Playing player1) {
        this.player1 = player1;
    }

    public Playing getPlayer2() {
        return player2;
    }

    public void setPlayer2(Playing player2) {
        this.player2 = player2;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", player1=" + player1 +
                ", player2=" + player2 +
                ", date=" + date +
                ", rounds=" + rounds +
                '}';
    }
}

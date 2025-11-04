package model;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class Match implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private Player player1;
    private Player player2;
    private int player1_points;
    private int player2_points;
    private Date date;
    private String status; // ONGOING, COMPLETED, ABANDONED
    private List<Round> rounds;

    public Match() {}

    public Match(Player player1, Player player2, Date date) {
        this.player1 = player1;
        this.player2 = player2;
        this.date = date;
        this.player1_points = 0;
        this.player2_points = 0;
        this.status = "ONGOING";
    }

    public Match(int id, Player player1, Player player2, int player1_points, int player2_points, 
                 Date date, String status, List<Round> rounds) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.player1_points = player1_points;
        this.player2_points = player2_points;
        this.date = date;
        this.status = status;
        this.rounds = rounds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
                ", player1_points=" + player1_points +
                ", player2_points=" + player2_points +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", rounds=" + rounds +
                '}';
    }
}

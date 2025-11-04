package model;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String fullName;
    private String username;
    private String password;
    private int totalWins;
    private int totalMatches;

    public Player() {}

    public Player(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.totalWins = 0;
        this.totalMatches = 0;
    }

    public Player(int id, String fullName, String username, String password, int totalWins, int totalMatches) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.totalWins = totalWins;
        this.totalMatches = totalMatches;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", totalWins=" + totalWins +
                ", totalMatches=" + totalMatches +
                '}';
    }
}

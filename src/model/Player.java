package model;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Status: -1 = Offline, 0 = Online, 1 = Playing
    
    private int id;
    private String fullName;
    private String username;
    private String password;
    private int totalWins;
    private int totalMatches;
    private double rating;
    private int status; // -1: Offline, 0: Online, 1: Playing

    public Player() {}

    public Player(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.totalWins = 0;
        this.totalMatches = 0;
        this.rating = 0.0;
        this.status = 0; // Online by default
    }

    public Player(int id, String fullName, String username, String password, int totalWins, int totalMatches) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.totalWins = totalWins;
        this.totalMatches = totalMatches;
        this.rating = totalMatches > 0 ? (double) totalWins / totalMatches * 100 : 0.0;
        this.status = 0; // Online by default
    }

    public Player(int id, String fullName, String username, String password, int totalWins, int totalMatches, double rating) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.totalWins = totalWins;
        this.totalMatches = totalMatches;
        this.rating = rating;
        this.status = 0; // Online by default
    }

    public Player(int id, String fullName, String username, String password, int totalWins, int totalMatches, double rating, int status) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.totalWins = totalWins;
        this.totalMatches = totalMatches;
        this.rating = rating;
        this.status = status;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusString() {
        switch (status) {
            case -1:
                return "Offline";
            case 0:
                return "Online";
            case 1:
                return "Playing";
            default:
                return "Unknown";
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", totalWins=" + totalWins +
                ", totalMatches=" + totalMatches +
                ", rating=" + String.format("%.2f", rating) +
                ", status=" + getStatusString() +
                '}';
    }
}

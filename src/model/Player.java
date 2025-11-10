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
    private int rating; // Tổng điểm qua tất cả games
    private int status; // -1: Offline, 0: Online, 1: Playing

    public Player() {}

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Player(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.totalWins = 0;
        this.totalMatches = 0;
        this.rating = 0;
        this.status = 0; // Online by default
    }

    public Player(int id, String fullName, String username, String password, int totalWins, int totalMatches) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.totalWins = totalWins;
        this.totalMatches = totalMatches;
        this.rating = 0; // Sẽ cập nhật từ games
        this.status = 0; // Online by default
    }

    public Player(int id, String fullName, String username, String password, int totalWins, int totalMatches, int rating) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.totalWins = totalWins;
        this.totalMatches = totalMatches;
        this.rating = rating;
        this.status = 0; // Online by default
    }

    public Player(int id, String fullName, String username, String password, int totalWins, int totalMatches, int rating, int status) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.totalWins = totalWins;
        this.totalMatches = totalMatches;
        this.rating = rating;
        this.status = status;
    }

    public Player(models.User user) {
        this.id = user.getId();
        this.fullName = user.getNickname();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.totalWins = user.getTotalWins();
        this.totalMatches = user.getTotalMatches();
        this.rating = user.getTotalScore();
        this.status = user.isPlaying() ? 1 : (user.isOnline() ? 0 : -1);
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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
                ", rating=" + rating +
                ", status=" + getStatusString() +
                '}';
    }
}

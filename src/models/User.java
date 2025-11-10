package models;

import org.json.JSONObject;

public class User {
  private int id;
  private String username;
  private String password;
  private String nickname;
  private int totalMatches;
  private int totalWins;
  private int totalScore;
  private boolean isOnline;
  private boolean isPlaying;

  public User(String username, String password, String nickname) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public User(int id, String username, String password, String nickname, int totalMatches, int totalWins,
      int totalScore) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.totalMatches = totalMatches;
    this.totalWins = totalWins;
    this.totalScore = totalScore;
  }

  public User(int id, String username, String nickname, int totalMatches, int totalWins, int totalScore) {
    this.id = id;
    this.username = username;
    this.nickname = nickname;
    this.totalMatches = totalMatches;
    this.totalWins = totalWins;
    this.totalScore = totalScore;
  }

  public User(model.Player player) {
    this.id = player.getId();
    this.username = player.getUsername();
    this.password = player.getPassword();
    this.nickname = player.getFullName();
    this.totalMatches = player.getTotalMatches();
    this.totalWins = player.getTotalWins();
    this.totalScore = player.getRating();
    this.isOnline = player.getStatus() != -1;
    this.isPlaying = player.getStatus() == 1;
  }

  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    json.put("id", id);
    json.put("username", username);
    json.put("nickname", nickname);
    json.put("total_matches", totalMatches);
    json.put("total_wins", totalWins);
    json.put("total_score", totalScore);
    return json;
  }

  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getNickname() {
    return nickname;
  }

  public int getTotalMatches() {
    return totalMatches;
  }

  public int getTotalWins() {
    return totalWins;
  }

  public int getTotalScore() {
    return totalScore;
  }

  public boolean isOnline() {
    return isOnline;
  }

  public boolean isPlaying() {
    return isPlaying;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public void setTotalMatches(int totalMatches) {
    this.totalMatches = totalMatches;
  }

  public void setTotalWins(int totalWins) {
    this.totalWins = totalWins;
  }

  public void setTotalScore(int totalScore) {
    this.totalScore = totalScore;
  }

  public void setOnline(boolean isOnline) {
    this.isOnline = isOnline;
  }

  public void setPlaying(boolean isPlaying) {
    this.isPlaying = isPlaying;
  }

}

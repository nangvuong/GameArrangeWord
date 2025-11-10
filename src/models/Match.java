package models;

import java.time.LocalDateTime;

public class Match {
  private int id;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String result;
  private String status;
  private User player1;
  private User player2;

  public Match(LocalDateTime startTime, String result, String status) {
    this.startTime = startTime;
    this.result = result;
    this.status = status;
  }

  public Match(LocalDateTime startTime, LocalDateTime endTime, User player1, User player2) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.player1 = player1;
    this.player2 = player2;
  }

  public Match(int id, LocalDateTime startTime, LocalDateTime endTime, String result, String status, User player1,
      User player2) {
    this.id = id;
    this.startTime = startTime;
    this.endTime = endTime;
    this.result = result;
    this.status = status;
    this.player1 = player1;
    this.player2 = player2;
  }
  // Getters and Setters

  public int getId() {
    return id;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public String getResult() {
    return result;
  }

  public String getStatus() {
    return status;
  }

  public User getPlayer1() {
    return player1;
  }

  public User getPlayer2() {
    return player2;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setPlayer1(User player1) {
    this.player1 = player1;
  }

  public void setPlayer2(User player2) {
    this.player2 = player2;
  }

}

package network;

import org.json.JSONObject;

/**
 * Utility class để gửi các request đến server
 */
public class PlayerIO {
  private static GameClient getClient() {
    return GameClient.getInstance();
  }

  /**
   * Yêu cầu danh sách người chơi online
   */
  public static void requestOnlineUsers() {
    JSONObject request = new JSONObject();
    request.put("action", "GET_ONLINE_USERS");
    getClient().sendMessage(request);
  }

  /**
   * Gửi lời mời chơi đến người chơi khác
   */
  public static void inviteUserToGame(String targetUsername) {
    JSONObject request = new JSONObject();
    request.put("action", "INVITE_USER_TO_GAME");
    request.put("targetUsername", targetUsername);
    getClient().sendMessage(request);
  }

  /**
   * Phản hồi lời mời chơi
   */
  public static void respondToInvitation(String inviterUsername, boolean accept) {
    JSONObject request = new JSONObject();
    request.put("action", "INVITE_USER_TO_GAME_RESPONSE");
    request.put("inviterUsername", inviterUsername);
    request.put("accept", accept);
    getClient().sendMessage(request);
  }

  /**
   * Gửi câu trả lời của người chơi trong một round
   */
  public static void submitAnswer(int matchId, int roundId, int userId, String answer, int timeSpent,
      boolean isCorrect) {
    JSONObject request = new JSONObject();
    request.put("action", "SUBMIT_USER_ANSWER");

    JSONObject data = new JSONObject();
    data.put("matchId", matchId);
    data.put("roundId", roundId);
    data.put("userId", userId);
    data.put("userAnswer", answer);
    data.put("timeSpent", timeSpent);
    data.put("isCorrect", isCorrect);

    request.put("data", data);
    getClient().sendMessage(request);
  }

  /**
   * Thông báo kết thúc game
   */
  public static void quitGame(int matchId, int userId, int opponentId) {
    JSONObject request = new JSONObject();
    request.put("action", "QUIT_GAME");
    JSONObject data = new JSONObject();
    data.put("matchId", matchId);
    data.put("userId", userId);
    data.put("opponentId", opponentId);

    request.put("data", data);
    getClient().sendMessage(request);
  }

  /**
   * Yêu cầu bảng xếp hạng
   */
  public static void requestRanking() {
    JSONObject request = new JSONObject();
    request.put("action", "GET_RANKING_REQUEST");
    getClient().sendMessage(request);
  }

  /**
   * Logout
   */
  public static void logout() {
    JSONObject request = new JSONObject();
    request.put("action", "LOGOUT_REQUEST");
    getClient().sendMessage(request);
  }
}

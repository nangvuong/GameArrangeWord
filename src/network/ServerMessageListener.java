package network;

import org.json.JSONObject;

public interface ServerMessageListener {
  /**
   * Được gọi khi nhận message từ server
   * 
   * @param action  Loại action (VD: LOGIN_RESPONSE, GET_ONLINE_USERS_RESPONSE,
   *                etc.)
   * @param message Nội dung message dạng JSON
   */
  void onMessageReceived(String action, JSONObject message);

  /**
   * Được gọi khi mất kết nối với server
   */
  default void onDisconnected() {
    System.err.println("Lost connection to server");
  }
}

package network;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import model.Player;

/**
 * Class xử lý authentication (login/register) với server
 */
public class PlayerAuth {
  /**
   * Đăng nhập
   * 
   * @param username Tên đăng nhập
   * @param password Mật khẩu
   * @return Player object nếu thành công, null nếu thất bại
   */
  public static Player checkLogin(String name, String password) throws Exception {
    GameClient client = GameClient.getInstance();
    // Kết nốu nếu chưa connect
    if (!client.isConnected()) {
      boolean connected = client.connect();
      if (!connected) {
        throw new Exception("Không thể kết nối đến server. Vui lòng kiểm tra server đã chạy chưa.");
      }
    }
    // Tạo CompletableFuture để chờ response
    CompletableFuture<Player> future = new CompletableFuture<>();
    // Đăng ký listener tạm thời để nhận response
    ServerMessageListener tempListener = new ServerMessageListener() {
      @Override
      public void onMessageReceived(String action, JSONObject message) {
        if ("LOGIN_RESPONSE".equals(action)) {
          String status = message.optString("status", "fail");
          if ("success".equals(status)) {
            JSONObject userJson = message.getJSONObject("user");
            Player player = new Player(
                userJson.getInt("id"),
                userJson.getString("nickname"),
                userJson.getString("username"),
                "", // không lưu password
                userJson.getInt("total_wins"),
                userJson.getInt("total_matches"),
                userJson.getInt("total_score"),
                0 // Online
            );
            future.complete(player);
          } else {
            future.complete(null);
          }
          client.removeMessageListener(this);
        }
      }
    };
    client.addMessageListener(tempListener);
    // Gửi request đăng nhập
    JSONObject loginRequest = new JSONObject();
    loginRequest.put("action", "LOGIN_REQUEST");
    loginRequest.put("username", name);
    loginRequest.put("password", password);
    client.sendMessage(loginRequest);
    // Chờ response tối đa 10 giây
    try {
      Player player = future.get(10, TimeUnit.SECONDS);
      if (player == null) {
        throw new Exception("Tên đăng nhập hoặc mật khẩu không đúng!");
      }
      return player;
    } catch (java.util.concurrent.TimeoutException e) {
      client.removeMessageListener(tempListener);
      throw new Exception("Timeout: Server không phản hồi. Vui lòng thử lại.");
    } catch (Exception e) {
      client.removeMessageListener(tempListener);
      throw new Exception("Lỗi đăng nhập: " + e.getMessage());
    }
  }
  // Đăng kí tài khoản
  public static boolean registerAccount(String username, String password, String nickname) throws Exception {
    GameClient client = GameClient.getInstance();
    // Kết nốu nếu chưa connect
    if (!client.isConnected()) {
      boolean connected = client.connect();
      if (!connected) {
        throw new Exception("Không thể kết nối đến server. Vui lòng kiểm tra server đã chạy chưa.");
      }
    }
    // Tạo CompletableFuture để chờ response
    CompletableFuture<Boolean> future = new CompletableFuture<>();
    // Đăng ký listener tạm thời để nhận response
    ServerMessageListener tempListener = new ServerMessageListener() {
      @Override
      public void onMessageReceived(String action, JSONObject message) {
        if ("REGISTER_RESPONSE".equals(action)) {
          String status = message.optString("status", "fail");
          if ("success".equals(status)) {
            future.complete(true);
          } else {
            future.complete(false);
          }
          client.removeMessageListener(this);
        }
      }
    };
    client.addMessageListener(tempListener);
    // Gửi request đăng kí
    JSONObject registerRequest = new JSONObject();
    registerRequest.put("action", "REGISTER_REQUEST");
    registerRequest.put("username", username);
    registerRequest.put("password", password);
    registerRequest.put("nickname", nickname);
    client.sendMessage(registerRequest);
    // Chờ response tối đa 10 giây
    try {
      boolean isRegistered = future.get(10, TimeUnit.SECONDS);
      return isRegistered;
    } catch (java.util.concurrent.TimeoutException e) {
      client.removeMessageListener(tempListener);
      throw new Exception("Timeout: Server không phản hồi. Vui lòng thử lại.");
    } catch (Exception e) {
      client.removeMessageListener(tempListener);
      throw new Exception("Lỗi đăng kí: " + e.getMessage());
    }
  }
}

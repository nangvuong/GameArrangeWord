package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class GameClient {
  private static GameClient instance;
  private Socket socket;
  private BufferedReader reader;
  private BufferedWriter writer;
  private Thread listenerThread;
  private boolean isConnected = false;
  private List<ServerMessageListener> listeners = new ArrayList<>();

  private static final String SERVER_ADDRESS = "localhost";
  private static final int SERVER_PORT = 8989;

  private GameClient() {
  }

  public static synchronized GameClient getInstance() {
    if (instance == null) {
      instance = new GameClient();
    }
    return instance;
  }

  // Kết nối đến server
  public boolean connect() {
    try {
      socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      isConnected = true;
      // Bắt đầu lắng nghe messgaes từ server
      startListening();
      System.out.println("✅ Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);
      return true;
    } catch (Exception e) {
      System.err.println("❌ Failed to connect to server: " + e.getMessage());
      isConnected = false;
      return false;
    }
  }

  // Gửi Messgae lên server
  public synchronized void sendMessage(JSONObject message) {
    if (!isConnected) {
      System.err.println("❌ Cannot send message: Not connected to server");
      return;
    }
    try {
      writer.write(message.toString());
      writer.newLine();
      writer.flush();
      System.out.println("📤 Sent to server: " + message.toString());
    } catch (Exception e) {
      System.err.println("❌ Failed to send message: " + e.getMessage());
      handleDisconnect();
    }
  }

  /**
   * Bắt đầu lắng nghe messages từ server
   */
  private void startListening() {
    listenerThread = new Thread(() -> {
      try {
        String line;
        while (isConnected && (line = reader.readLine()) != null) {
          System.out.println("📥 Received from server: " + line);
          JSONObject message = new JSONObject(line);
          notifyListeners(message);
        }
      } catch (Exception e) {
        if (isConnected) {
          System.err.println("Connection error: " + e.getMessage());
          handleDisconnect();
        }
      }
    });
    listenerThread.setDaemon(true);
    listenerThread.start();
  }

  /**
   * Thông báo cho tất cả listeners khi nhận message từ server
   */
  private void notifyListeners(JSONObject message) {
    String action = message.optString("action", "");
    for (ServerMessageListener listener : listeners) {
      javax.swing.SwingUtilities.invokeLater(() -> {
        listener.onMessageReceived(action, message);
      });
    }
  }

  /**
   * Đăng ký listener để nhận messages
   */
  public void addMessageListener(ServerMessageListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  /**
   * Hủy đăng ký listener
   */
  public void removeMessageListener(ServerMessageListener listener) {
    listeners.remove(listener);
  }

  /**
   * Xử lý khi mất kết nối
   */
  private void handleDisconnect() {
    isConnected = false;
    try {
      if (socket != null && !socket.isClosed()) {
        socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Thông báo cho listeners về việc disconnect
    for (ServerMessageListener listener : listeners) {
      javax.swing.SwingUtilities.invokeLater(() -> {
        listener.onDisconnected();
      });
    }
  }

  /**
   * Ngắt kết nối
   */
  public void disconnect() {
    isConnected = false;
    try {
      if (writer != null) {
        writer.close();
      }
      if (reader != null) {
        reader.close();
      }
      if (socket != null && !socket.isClosed()) {
        socket.close();
      }
      System.out.println("👋 Disconnected from server");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean isConnected() {
    return isConnected && socket != null && !socket.isClosed();
  }
}

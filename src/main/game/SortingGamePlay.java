package main.game;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import network.GameClient;

public class SortingGamePlay extends JPanel {
  private int matchId;
  private int timeLimit;
  private int timeRemaining;
  private int currentRound = 1;
  private String typeQues; // NUMBERS or LETTERS
  private String sortOrder; // ASCENDING or DESCENDING
  private String instruction;
  private List<String> items;
  private List<String> correctAnswer;
  private List<String> currentOrder;

  private String playerName;
  private String opponentName;
  private int playerId;
  private int opponentId;

  private JPanel itemsPanel;
  private JLabel timerLabel;
  private JLabel instructionLabel;
  private javax.swing.Timer gameTimer; 
  private boolean gameEnded = false;

  private Runnable onGameEnd;

  private static final Color BG_COLOR = new Color(75, 0, 130);
  private static final Color ITEM_COLOR = new Color(100, 100, 255);
  private static final Color ITEM_HOVER = new Color(120, 170, 255);

  public SortingGamePlay(int matchId, JSONObject questionData, JSONObject self, JSONObject opponent, int roundNumber) {
    this.matchId = matchId;
    this.currentRound = roundNumber;

    // Parse question data
    this.typeQues = questionData.optString("typeQues", "NUMBERS");
    this.sortOrder = questionData.optString("sortOrder", "ASCENDING");
    this.timeLimit = questionData.optInt("timeLimit", 30);
    this.instruction = questionData.optString("instruction", "");

    // Parse items
    JSONArray itemsArray = questionData.optJSONArray("items");
    this.items = new ArrayList<>();
    if (itemsArray != null) {
      for (int i = 0; i < itemsArray.length(); i++) {
        items.add(itemsArray.optString(i));
      }
    }

    // Parse correct answer
    JSONArray answerArray = questionData.optJSONArray("correctAnswer");
    this.correctAnswer = new ArrayList<>();
    if (answerArray != null) {
      for (int i = 0; i < answerArray.length(); i++) {
        correctAnswer.add(answerArray.optString(i));
      }
    }

    // Copy items to current order (shuffle)
    this.currentOrder = new ArrayList<>(items);
    Collections.shuffle(currentOrder);

    // Parse player info
    this.playerName = self.optString("nickname", "Player");
    this.playerId = self.optInt("id", 0);
    this.opponentName = opponent.optString("nickname", "Opponent");
    this.opponentId = opponent.optInt("id", 0);

    this.timeRemaining = timeLimit;

    initUI();
    startTimer();
  }

  private void initUI() {
    setLayout(new BorderLayout());
    setBackground(BG_COLOR);
    setPreferredSize(new Dimension(1000, 700));

    // Top panel - Instruction & Timer
    JPanel topPanel = createTopPanel();
    add(topPanel, BorderLayout.NORTH);

    // Center panel - Items to sort
    itemsPanel = createItemsPanel();
    add(itemsPanel, BorderLayout.CENTER);

    // Bottom panel - Submit button
    JPanel bottomPanel = createBottomPanel();
    add(bottomPanel, BorderLayout.SOUTH);
  }

  private JPanel createTopPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(BG_COLOR);
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Player vs Opponent
    JLabel vsLabel = new JLabel(playerName + " vs " + opponentName);
    vsLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
    vsLabel.setForeground(Color.WHITE);
    vsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Instruction
    instructionLabel = new JLabel(instruction);
    instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    instructionLabel.setForeground(new Color(200, 220, 255));
    instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Timer
    timerLabel = new JLabel(timeRemaining + "s");
    timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
    timerLabel.setForeground(new Color(255, 100, 100));
    timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    panel.add(vsLabel);
    panel.add(Box.createVerticalStrut(10));
    panel.add(instructionLabel);
    panel.add(Box.createVerticalStrut(15));
    panel.add(timerLabel);

    return panel;
  }

  private JPanel createItemsPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));
    panel.setBackground(BG_COLOR);

    updateItemsDisplay();

    return panel;
  }

  private void updateItemsDisplay() {
    itemsPanel.removeAll();

    for (int i = 0; i < currentOrder.size(); i++) {
      final int index = i;
      String item = currentOrder.get(i);

      JButton itemButton = new JButton(item);
      itemButton.setFont(new Font("Segoe UI", Font.BOLD, 24));
      itemButton.setPreferredSize(new Dimension(80, 80));
      itemButton.setBackground(ITEM_COLOR);
      itemButton.setForeground(Color.WHITE);
      itemButton.setFocusPainted(false);
      itemButton.setBorderPainted(false);

      // Enable drag & drop
      itemButton.setTransferHandler(new TransferHandler("text"));
      itemButton.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
          JButton btn = (JButton) evt.getSource();
          TransferHandler handler = btn.getTransferHandler();
          handler.exportAsDrag(btn, evt, TransferHandler.MOVE);
        }

        public void mouseEntered(java.awt.event.MouseEvent evt) {
          itemButton.setBackground(ITEM_HOVER);
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
          itemButton.setBackground(ITEM_COLOR);
        }
      });

      // Click to swap with adjacent
      final int currentIndex = index;
      itemButton.addActionListener(e -> swapWithNext(currentIndex));

      itemsPanel.add(itemButton);
    }

    itemsPanel.revalidate();
    itemsPanel.repaint();
  }

  private void swapWithNext(int index) {
    if (index < currentOrder.size() - 1) {
      Collections.swap(currentOrder, index, index + 1);
      updateItemsDisplay();
    }
  }

  private JPanel createBottomPanel() {
    JPanel panel = new JPanel();
    panel.setBackground(BG_COLOR);
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JButton submitBtn = new JButton("✓ Submit Answer");
    submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
    submitBtn.setPreferredSize(new Dimension(200, 50));
    submitBtn.setBackground(new Color(76, 175, 80));
    submitBtn.setForeground(Color.WHITE);
    submitBtn.setFocusPainted(false);
    submitBtn.setBorderPainted(false);
    submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

    submitBtn.addActionListener(e -> submitAnswer());

    panel.add(submitBtn);

    return panel;
  }

  private void startTimer() {
    gameTimer = new javax.swing.Timer(1000, e -> {
      if (gameEnded)
        return;

      timeRemaining--;
      timerLabel.setText(timeRemaining + "s");

      if (timeRemaining <= 5) {
        timerLabel.setForeground(new Color(255, 50, 50));
      }

      if (timeRemaining <= 0) {
        gameTimer.stop();
        submitAnswer(); // Auto submit khi hết giờ
      }
    });
    gameTimer.start();
  }

  private void submitAnswer() {
    if (gameEnded)
      return;

    gameEnded = true;
    if (gameTimer != null) {
      gameTimer.stop();
    }

    // Check if answer is correct
    boolean isCorrect = currentOrder.equals(correctAnswer);
    int timeCompleted = timeLimit - timeRemaining;
    String status = isCorrect ? "CORRECT" : "WRONG";

    System.out.println("📤 Submitting answer: " + status);
    System.out.println("   Your answer: " + currentOrder);
    System.out.println("   Correct answer: " + correctAnswer);

    // Send to server
    JSONObject request = new JSONObject();
    request.put("action", "SUBMIT_USER_ANSWER");

    JSONObject data = new JSONObject();
    data.put("matchId", matchId);
    data.put("userId", playerId);
    data.put("roundNumber", currentRound); 
    data.put("timeCompleted", timeCompleted);
    data.put("status", status);

    request.put("data", data);
    GameClient.getInstance().sendMessage(request);

    // Show result
    showResult(isCorrect);
  }

  private void showResult(boolean isCorrect) {
    String resultText = isCorrect ? "✓ CORRECT!" : "✗ WRONG!";
    Color resultColor = isCorrect ? new Color(0, 255, 120) : Color.RED;

    JLabel resultLabel = new JLabel(resultText);
    resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
    resultLabel.setForeground(resultColor);
    resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

    // Replace center panel with result
    remove(itemsPanel);
    add(resultLabel, BorderLayout.CENTER);
    revalidate();
    repaint();

    // Wait 2 seconds then end game
    javax.swing.Timer endTimer = new javax.swing.Timer(2000, e -> {
      if (onGameEnd != null) {
        onGameEnd.run();
      }
    });
    endTimer.setRepeats(false);
    endTimer.start();
  }

  public void setOnGameEndCallback(Runnable callback) {
    this.onGameEnd = callback;
  }

  public int getMatchId() {
    return matchId;
  }
}
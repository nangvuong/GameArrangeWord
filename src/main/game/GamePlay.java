package main.game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import model.Player;
import model.Match;

public class GamePlay extends JPanel {
    private static final int TOTAL_ROUNDS = 10;
    private static final int TIME_PER_ROUND = 15;
    private static final int BLANK_BOX_SIZE = 60;
    private static final int BLANK_BOX_SPACING = 15;
    private static final int LETTER_BOX_SIZE = 60;
    private static final int LETTER_BOX_SPACING = 10;

    // ===== CACHED RENDERING OBJECTS (Tránh tạo mới trong paintComponent) =====
    private static final Font FONT_LARGE = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font FONT_MEDIUM = new Font("Segoe UI", Font.BOLD, 20);
    private static final Color COLOR_PURPLE_LIGHT = new Color(100, 0, 150);
    private static final Color COLOR_CORRECT = new Color(0, 255, 120);
    private static final Color COLOR_WRONG = Color.RED;
    private static final Color COLOR_LETTER_ACTIVE = new Color(100, 100, 255);
    private static final Color COLOR_LETTER_HOVER = new Color(120, 170, 255);
    private static final Color COLOR_LETTER_USED = new Color(70, 70, 110);
    private static final Color COLOR_BORDER_LETTER = new Color(200, 200, 255);
    private static final Color COLOR_BORDER_USED = new Color(100, 100, 150);
    private static final BasicStroke STROKE_LETTER = new BasicStroke(2);
    private static final BasicStroke STROKE_DASHED = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
            0, new float[] { 5 }, 0);

    private int playerScore = 0;
    private String playerScoreLabel = "0";
    private int opponentScore = 0;
    private String opponentScoreLabel = "0";
    private int currentRound = 1;
    private int timeRemaining = TIME_PER_ROUND;
    private Timer gameTimer;
    private boolean roundEnded = false; // Track xem round đã kết thúc
    private boolean gameEnded = false; // Track xem game đã kết thúc
    private int roundStartTime = TIME_PER_ROUND; // Lưu thời gian bắt đầu round
    private String currentRoundStatus = "UNKNOWN"; // CORRECT, WRONG, TIMEOUT, QUIT

    private String playerName = "Player";
    private String opponentName = "AI";
    private Player currentPlayer;
    private Player opponentPlayer;
    private Match currentMatch;

    private String[] answerLetters; // Ô trống để điền
    private String[] scrambledLetters; // Ô ký tự
    private String correctWord = "EFFECT";
    private boolean[] usedLetters;
    private int[] showResultColors;
    private String hint = "the result of a particular influence";

    private JPanel answerPanel;
    private JLabel hintLabel;
    private JPanel lettersPanel;
    private JLabel timerLabel;
    private JLabel roundLabel;
    private JLabel playerNameLabel;
    private JLabel opponentNameLabel;
    private JPanel playerScorePanel;
    private JPanel opponentScorePanel;
    private JProgressBar progressBar;

    private Runnable onGameEnd;

    public GamePlay() {
        this("Player", "AI");
    }

    public GamePlay(String playerName, String opponentName) {
        setPreferredSize(new Dimension(1000, 700));
        setBackground(new Color(75, 0, 130));
        setLayout(null);

        this.playerName = playerName;
        this.opponentName = opponentName;

        initGame();
        initializeComponents();
        startTimer();
    }

    public GamePlay(Match match, Player currentPlayer) {
        setPreferredSize(new Dimension(1000, 700));
        setBackground(new Color(75, 0, 130));
        setLayout(null);

        this.currentMatch = match;
        this.currentPlayer = currentPlayer;
        this.playerName = match.getPlayer1().getPlayer().getFullName();
        this.opponentName = match.getPlayer2().getPlayer().getFullName();
        this.opponentPlayer = match.getPlayer2().getPlayer();
        this.correctWord = match.getRounds().get(0).getWord().getWord();
        this.hint = match.getRounds().get(0).getWord().getHint();

        initGame();
        initializeComponents();
        startTimer();
    }

    private void initGame() {

        // Chuẩn bị các ô trống
        answerLetters = new String[correctWord.length()];
        Arrays.fill(answerLetters, "");

        showResultColors = new int[correctWord.length()];
        Arrays.fill(showResultColors, 0);

        // Chuẩn bị ký tự xáo trộn
        scrambledLetters = correctWord.split("");
        java.util.List<String> list = Arrays.asList(scrambledLetters);
        Collections.shuffle(list);
        scrambledLetters = list.toArray(new String[0]);

        usedLetters = new boolean[scrambledLetters.length];
        roundStartTime = TIME_PER_ROUND; // Reset thời gian bắt đầu
    }

    private void initializeComponents() {
        // Top Panel - Score
        JPanel topPanel = createTopPanel();
        topPanel.setBounds(0, 0, 1000, 80);
        add(topPanel);

        // Middle Panel - Game Area
        JPanel middlePanel = createMiddlePanel();
        middlePanel.setBounds(0, 80, 1000, 480);
        add(middlePanel);

        // Bottom Panel - Progress Bar
        JPanel bottomPanel = createBottomPanel();
        bottomPanel.setBounds(0, 560, 1000, 40);
        add(bottomPanel);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(75, 0, 130));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 0, 150)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 30, 10, 30);

        // ===== LEFT SIDE: Player Name + Score =====
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(75, 0, 130));
        leftPanel.setOpaque(false);

        playerNameLabel = new JLabel(playerName);
        playerNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        playerNameLabel.setForeground(new Color(200, 200, 255));
        playerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel chứa score
        playerScorePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Vẽ số điểm chính
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
                String scoreText = playerScoreLabel;
                FontMetrics fm = g2.getFontMetrics();
                int scoreX = (getWidth() - fm.stringWidth(scoreText)) / 2;
                int scoreY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(scoreText, scoreX, scoreY);
            }
        };
        playerScorePanel.setBackground(new Color(75, 0, 130));
        playerScorePanel.setOpaque(false);
        playerScorePanel.setPreferredSize(new Dimension(80, 50));
        playerScorePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(playerNameLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(playerScorePanel);

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(leftPanel, gbc);

        // ===== CENTER: Round Info =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(75, 0, 130));
        centerPanel.setOpaque(false);

        roundLabel = new JLabel("Round: 1/10");
        roundLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        roundLabel.setForeground(Color.WHITE);
        roundLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerLabel = new JLabel("15s");
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        timerLabel.setForeground(new Color(255, 100, 100));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(roundLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(timerLabel);

        gbc.gridx = 1;
        gbc.weightx = 0;
        panel.add(centerPanel, gbc);

        // ===== RIGHT SIDE: Opponent Name + Score =====
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(75, 0, 130));
        rightPanel.setOpaque(false);

        opponentNameLabel = new JLabel(opponentName);
        opponentNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        opponentNameLabel.setForeground(new Color(200, 200, 255));
        opponentNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel chứa score
        opponentScorePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Vẽ số điểm chính
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
                String scoreText = opponentScoreLabel;
                FontMetrics fm = g2.getFontMetrics();
                int scoreX = (getWidth() - fm.stringWidth(scoreText)) / 2;
                int scoreY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(scoreText, scoreX, scoreY);
            }
        };
        opponentScorePanel.setBackground(new Color(75, 0, 130));
        opponentScorePanel.setOpaque(false);
        opponentScorePanel.setPreferredSize(new Dimension(80, 50));
        opponentScorePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(opponentNameLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(opponentScorePanel);

        gbc.gridx = 2;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(rightPanel, gbc);

        return panel;
    }

    private JPanel createMiddlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(75, 0, 130));

        // Hint text
        hintLabel = new JLabel(hint);
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        hintLabel.setForeground(new Color(200, 200, 255));
        hintLabel.setBounds(150, 40, 700, 40);
        panel.add(hintLabel);

        // Answer Panel (Blank boxes)
        answerPanel = new JPanel();
        answerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, BLANK_BOX_SPACING, 0));
        answerPanel.setBackground(new Color(75, 0, 130));
        answerPanel.setOpaque(false);
        answerPanel.setBounds(150, 100, 700, BLANK_BOX_SIZE + 20);
        updateAnswerPanel();
        panel.add(answerPanel);

        // Letters Panel
        lettersPanel = new JPanel();
        lettersPanel.setLayout(new FlowLayout(FlowLayout.CENTER, LETTER_BOX_SPACING, 0));
        lettersPanel.setBackground(new Color(75, 0, 130));
        lettersPanel.setOpaque(false);
        lettersPanel.setBounds(100, 220, 800, LETTER_BOX_SIZE + 40);
        updateLettersPanel();
        panel.add(lettersPanel);

        return panel;
    }

    private void updateAnswerPanel() {
        // Tối ưu: Rebuild chỉ khi cần thiết (khác độ dài từ)
        if (answerPanel.getComponentCount() != correctWord.length()) {
            // Rebuild khi độ dài từ thay đổi
            answerPanel.removeAll();

            for (int i = 0; i < correctWord.length(); i++) {
                final int boxIndex = i;
                JPanel boxPanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        g2.setColor(COLOR_PURPLE_LIGHT);
                        g2.setStroke(STROKE_DASHED);
                        g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);

                        String text = boxIndex < answerLetters.length ? answerLetters[boxIndex] : "";
                        if (text != null && !text.isEmpty()) {
                            if (showResultColors[boxIndex] == 1) {
                                g2.setColor(COLOR_CORRECT);
                            } else if (showResultColors[boxIndex] == -1) {
                                g2.setColor(COLOR_WRONG);
                            } else {
                                g2.setColor(Color.WHITE);
                            }

                            g2.setFont(FONT_LARGE);
                            FontMetrics fm = g2.getFontMetrics();
                            int x = (getWidth() - fm.stringWidth(text)) / 2;
                            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                            g2.drawString(text, x, y);
                        }
                    }
                };

                boxPanel.setPreferredSize(new Dimension(BLANK_BOX_SIZE, BLANK_BOX_SIZE));
                boxPanel.setOpaque(false);
                boxPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        removeLetter(boxIndex);
                    }
                });
                answerPanel.add(boxPanel);
            }

            answerPanel.revalidate();
        } else {
            // Tối ưu: Chỉ repaint các component hiện có (cùng độ dài từ)
            for (Component comp : answerPanel.getComponents()) {
                comp.repaint();
            }
        }

        answerPanel.repaint();
    }

    private void updateLettersPanel() {
        // Tối ưu: Kiểm tra nếu chỉ cần update trạng thái, không cần rebuild
        if (lettersPanel.getComponentCount() == scrambledLetters.length) {
            // Chỉ update từng component hiện có thay vì rebuild
            Component[] components = lettersPanel.getComponents();
            for (int i = 0; i < components.length && i < scrambledLetters.length; i++) {
                if (components[i] instanceof JButton) {
                    JButton btn = (JButton) components[i];
                    btn.setEnabled(!usedLetters[i]);
                    btn.setCursor(usedLetters[i] ? new Cursor(Cursor.DEFAULT_CURSOR) : new Cursor(Cursor.HAND_CURSOR));
                    btn.repaint();
                }
            }
            return;
        }

        // Rebuild chỉ khi cần thiết (khác số lượng chữ)
        lettersPanel.removeAll();

        for (int i = 0; i < scrambledLetters.length; i++) {
            final int index = i;
            final boolean isUsed = usedLetters[i];

            JButton letterBtn = new JButton(scrambledLetters[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    Color fillColor;
                    Color borderColor;

                    if (isUsed) {
                        if (getModel().isArmed()) {
                            fillColor = new Color(80, 80, 120);
                        } else if (getModel().isRollover()) {
                            fillColor = new Color(90, 90, 130);
                        } else {
                            fillColor = COLOR_LETTER_USED;
                        }
                        borderColor = COLOR_BORDER_USED;
                    } else {
                        if (getModel().isArmed()) {
                            fillColor = new Color(100, 150, 255);
                        } else if (getModel().isRollover()) {
                            fillColor = COLOR_LETTER_HOVER;
                        } else {
                            fillColor = COLOR_LETTER_ACTIVE;
                        }
                        borderColor = COLOR_BORDER_LETTER;
                    }

                    g2.setColor(fillColor);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setColor(borderColor);
                    g2.setStroke(STROKE_LETTER);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                    super.paintComponent(g);
                }
            };

            letterBtn.setPreferredSize(new Dimension(LETTER_BOX_SIZE, LETTER_BOX_SIZE));
            letterBtn.setFont(FONT_MEDIUM);
            letterBtn.setForeground(Color.WHITE);
            letterBtn.setBorder(BorderFactory.createEmptyBorder());
            letterBtn.setFocusPainted(false);
            letterBtn.setContentAreaFilled(false);
            letterBtn.setOpaque(false);
            letterBtn.setCursor(isUsed ? new Cursor(Cursor.DEFAULT_CURSOR) : new Cursor(Cursor.HAND_CURSOR));
            letterBtn.setEnabled(!isUsed);

            if (!isUsed) {
                letterBtn.addActionListener(e -> selectLetter(index));
            }

            lettersPanel.add(letterBtn);
        }

        lettersPanel.revalidate();
        lettersPanel.repaint();
    }

    private void selectLetter(int index) {
        if (gameEnded) {
            return;
        }

        // Tìm vị trí trống đầu tiên
        int emptyPos = -1;
        for (int i = 0; i < answerLetters.length; i++) {
            if (answerLetters[i].isEmpty()) {
                emptyPos = i;
                break;
            }
        }

        if (emptyPos != -1) {
            answerLetters[emptyPos] = scrambledLetters[index];
            usedLetters[index] = true;

            // Tối ưu: Chỉ repaint thay vì rebuild
            updateAnswerPanel();
            updateLettersPanel();

            checkAllLettersUsed();
        }
    }

    private void removeLetter(int index) {
        if (gameEnded) {
            return;
        }

        // Tối ưu: Chỉ xử lý nếu có ký tự tại vị trí
        if (index >= 0 && index < answerLetters.length && !answerLetters[index].isEmpty()) {
            String letterToRemove = answerLetters[index];

            // Tìm chỉ số ký tự trong mảng scrambledLetters để unmark
            for (int i = scrambledLetters.length - 1; i >= 0; i--) {
                if (usedLetters[i] && scrambledLetters[i].equals(letterToRemove)) {
                    usedLetters[i] = false;
                    break;
                }
            }

            // Xoá chữ tại vị trí
            answerLetters[index] = "";
            updateAnswerPanel();
            updateLettersPanel();
        }
    }

    private void calcPointRound() {
        StringBuilder userAnswer = new StringBuilder();
        // Tạo câu trả lời từ answerLetters
        for (int i = 0; i < answerLetters.length; i++) {
            userAnswer.append(answerLetters[i]);
        }

        // Tính thời gian đã dùng (tính bằng giây)
        int timeCompleted = roundStartTime - timeRemaining;
        // Xác định status: CORRECT hoặc WRONG
        boolean isCorrect = userAnswer.toString().equals(correctWord);
        currentRoundStatus = isCorrect ? "CORRECT" : "WRONG";
        // Tính điểm tạm thời cho UI (server sẽ tính lại chính xác)
        if (isCorrect) {
            int pointsEarned = 1; // Tạm tính 100 điểm cho đúng
            playerScoreLabel += " + " + pointsEarned;
            playerScore += pointsEarned;
        } else {
            playerScoreLabel += " + 0";
        }
        playerScorePanel.repaint();
        // Gửi answer lên server (nếu không phải chơi với AI)
        if (currentMatch != null && currentPlayer != null && opponentPlayer != null) {
            if (!opponentPlayer.getUsername().equals("ai")) {
                int matchId = currentMatch.getId();
                int userId = currentPlayer.getId();

                // Gửi với roundNumber thay vì roundId
                JSONObject request = new JSONObject();
                request.put("action", "SUBMIT_USER_ANSWER");

                JSONObject data = new JSONObject();
                data.put("matchId", matchId);
                data.put("userId", userId);
                data.put("roundNumber", currentRound); // Gửi roundNumber
                data.put("timeCompleted", timeCompleted);
                data.put("status", currentRoundStatus); // CORRECT hoặc WRONG

                request.put("data", data);
                network.GameClient.getInstance().sendMessage(request);

                System.out.println("📤 Sent answer to server: status=" + currentRoundStatus +
                        ", time=" + timeCompleted + "s, round=" + currentRound);
            }
        }
    }

    private void checkAllLettersUsed() {
        // Kiểm tra xem tất cả chữ cái đã được dùng chưa
        boolean allUsed = true;
        for (boolean used : usedLetters) {
            if (!used) {
                allUsed = false;
                break;
            }
        }

        if (allUsed) {
            roundEnded = true;
            if (gameTimer != null) {
                gameTimer.stop();
            }
            calcPointRound();

            // === Hiển thị đáp án đúng trước khi qua round mới ===
            for (int i = 0; i < correctWord.length(); i++) {
                if (answerLetters[i].equals(String.valueOf(correctWord.charAt(i)))) {
                    showResultColors[i] = 1;
                } else {
                    showResultColors[i] = -1;
                }
                answerLetters[i] = String.valueOf(correctWord.charAt(i));
            }
            updateAnswerPanel(); // Cập nhật giao diện để hiển thị đáp án đúng

            // Chờ 0.5 giây rồi sang round mới
            Timer delayTimer = new Timer(1000, e -> {
                resetRound();
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }

    private void resetRound() {

        playerScoreLabel = String.valueOf(playerScore);

        playerScorePanel.repaint();

        Arrays.fill(showResultColors, 0); // Reset màu hiển thị kết quả

        if (currentRound < TOTAL_ROUNDS) {
            // Chuẩn bị round tiếp theo
            currentRound++;
            roundLabel.setText("Round: " + currentRound + "/" + TOTAL_ROUNDS);
            timeRemaining = TIME_PER_ROUND;
            timerLabel.setText(timeRemaining + "s");
            timerLabel.setForeground(new Color(255, 100, 100));
            progressBar.setValue(TIME_PER_ROUND);
            progressBar.setForeground(new Color(255, 165, 0));
            correctWord = currentMatch.getRounds().get(currentRound - 1).getWord().getWord();
            hint = currentMatch.getRounds().get(currentRound - 1).getWord().getHint();
            hintLabel.setText(hint);
            initGame();
            updateAnswerPanel();
            updateLettersPanel();
            roundEnded = false; // Unlock game for next round
            startTimer(); // Start timer mới cho round tiếp
        } else {
            // Game kết thúc
            endGame();
        }
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        panel.setBackground(new Color(75, 0, 130));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));

        progressBar = new JProgressBar(0, TIME_PER_ROUND);
        progressBar.setValue(TIME_PER_ROUND);
        progressBar.setStringPainted(false);
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        progressBar.setForeground(new Color(255, 165, 0));
        progressBar.setBackground(new Color(50, 50, 100));
        progressBar.setPreferredSize(new Dimension(0, 6));

        panel.add(progressBar, BorderLayout.CENTER);

        return panel;
    }

    private void startTimer() {
        gameTimer = new Timer(1000, e -> {
            // Nếu round đã kết thúc (người chơi đã fill hết), không chạy timer nữa
            if (roundEnded) {
                return;
            }

            if (timeRemaining > 0) {
                timeRemaining--;
                progressBar.setValue(timeRemaining);
                timerLabel.setText(timeRemaining + "s");

                if (timeRemaining <= 5) {
                    timerLabel.setForeground(new Color(255, 50, 50));
                    progressBar.setForeground(new Color(255, 50, 50));
                } else {
                    timerLabel.setForeground(new Color(255, 100, 100));
                    progressBar.setForeground(new Color(255, 165, 0));
                }
            } else {
                roundEnded = true;
                if (gameTimer != null) {
                    gameTimer.stop();
                }
                currentRoundStatus = "TIMEOUT";
                int timeCompleted = roundStartTime; // Hết thời gian

                // Gửi TIMEOUT lên server
                if (currentMatch != null && currentPlayer != null && opponentPlayer != null) {
                    if (!opponentPlayer.getUsername().equals("ai")) {
                        JSONObject request = new JSONObject();
                        request.put("action", "SUBMIT_USER_ANSWER");

                        JSONObject data = new JSONObject();
                        data.put("matchId", currentMatch.getId());
                        data.put("userId", currentPlayer.getId());
                        data.put("roundNumber", currentRound);
                        data.put("timeCompleted", timeCompleted);
                        data.put("status", "TIMEOUT");

                        request.put("data", data);
                        network.GameClient.getInstance().sendMessage(request);

                        System.out.println("📤 Sent TIMEOUT to server for round " + currentRound);
                    }
                }

                // Hiển thị đáp án đúng
                for (int i = 0; i < correctWord.length(); i++) {
                    if (answerLetters[i].equals(String.valueOf(correctWord.charAt(i)))) {
                        showResultColors[i] = 1;
                    } else {
                        showResultColors[i] = -1;
                    }
                    answerLetters[i] = String.valueOf(correctWord.charAt(i));
                }
                updateAnswerPanel();

                Timer delayTimer = new Timer(1000, ev -> {
                    resetRound();
                });
                delayTimer.setRepeats(false);
                delayTimer.start();
            }
        });
        gameTimer.start();
    }

    private void endGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }

        gameEnded = true;

        // Update match with final scores (sử dụng point từ Playing objects)
        if (currentMatch != null) {
            currentMatch.getPlayer1().setPoint(playerScore);
            currentMatch.getPlayer2().setPoint(opponentScore);
        }

        if (onGameEnd != null) {
            onGameEnd.run();
        }
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public Match getMatch() {
        return currentMatch;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getOpponentPlayer() {
        return opponentPlayer;
    }

    public void setOnGameEndCallback(Runnable callback) {
        this.onGameEnd = callback;
    }
}

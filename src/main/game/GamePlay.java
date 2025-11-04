package main.game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import java.util.Arrays;
import java.util.Collections;

public class GamePlay extends JPanel {
    private static final int TOTAL_ROUNDS = 10;
    private static final int TIME_PER_ROUND = 15;
    private static final int BLANK_BOX_SIZE = 60;
    private static final int BLANK_BOX_SPACING = 15;
    private static final int LETTER_BOX_SIZE = 60;
    private static final int LETTER_BOX_SPACING = 10;

    private int playerScore = 0;
    private int opponentScore = 0;
    private int currentRound = 1;
    private int timeRemaining = TIME_PER_ROUND;
    private Timer gameTimer;
    private boolean roundEnded = false; // Track xem round đã kết thúc
    private boolean gameEnded = false; // Track xem game đã kết thúc
    
    private String playerName = "Player";
    private String opponentName = "AI";
    
    private String[] answerLetters; // Ô trống để điền
    private String[] scrambledLetters; // Ô ký tự
    private String correctWord = "EFFECT";
    private boolean[] usedLetters;

    private JPanel answerPanel;
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

    private void initGame() {
        // Chuẩn bị các ô trống
        answerLetters = new String[correctWord.length()];
        Arrays.fill(answerLetters, "");
        
        // Chuẩn bị ký tự xáo trộn
        scrambledLetters = correctWord.split("");
        java.util.List<String> list = Arrays.asList(scrambledLetters);
        Collections.shuffle(list);
        scrambledLetters = list.toArray(new String[0]);
        
        usedLetters = new boolean[scrambledLetters.length];
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
                String scoreText = String.valueOf(playerScore);
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
                String scoreText = String.valueOf(opponentScore);
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

        // Question text
        JLabel questionLabel = new JLabel("the result of a particular influence");
        questionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        questionLabel.setForeground(new Color(200, 200, 255));
        questionLabel.setBounds(150, 40, 700, 40);
        panel.add(questionLabel);

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
        answerPanel.removeAll();
        
        for (int i = 0; i < correctWord.length(); i++) {
            final int boxIndex = i;
            JPanel boxPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2.setColor(new Color(100, 0, 150));
                    g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5}, 0));
                    g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);
                    
                    String text = boxIndex < answerLetters.length ? answerLetters[boxIndex] : "";
                    if (text != null && !text.isEmpty()) {
                        g2.setColor(Color.WHITE);
                        g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
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
        answerPanel.repaint();
    }

    private void updateLettersPanel() {
        lettersPanel.removeAll();

        for (int i = 0; i < scrambledLetters.length; i++) {
            final int index = i;
            final boolean isUsed = usedLetters[i];
            
            JButton letterBtn = new JButton(scrambledLetters[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Màu tùy vào trạng thái used/enabled
                    Color fillColor;
                    Color borderColor;
                    
                    if (isUsed) {
                        // Đã dùng → màu xám nhạt
                        if (getModel().isArmed()) {
                            fillColor = new Color(80, 80, 120);
                        } else if (getModel().isRollover()) {
                            fillColor = new Color(90, 90, 130);
                        } else {
                            fillColor = new Color(70, 70, 110);
                        }
                        borderColor = new Color(100, 100, 150);
                    } else {
                        // Chưa dùng → màu xanh sáng
                        if (getModel().isArmed()) {
                            fillColor = new Color(100, 150, 255);
                        } else if (getModel().isRollover()) {
                            fillColor = new Color(120, 170, 255);
                        } else {
                            fillColor = new Color(100, 100, 255);
                        }
                        borderColor = new Color(200, 200, 255);
                    }
                    
                    g2.setColor(fillColor);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setColor(borderColor);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                    
                    super.paintComponent(g);
                }
            };
            
            letterBtn.setPreferredSize(new Dimension(LETTER_BOX_SIZE, LETTER_BOX_SIZE));
            letterBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
            letterBtn.setForeground(Color.WHITE);
            letterBtn.setBorder(BorderFactory.createEmptyBorder());
            letterBtn.setFocusPainted(false);
            letterBtn.setContentAreaFilled(false);
            letterBtn.setOpaque(false);
            letterBtn.setCursor(isUsed ? new Cursor(Cursor.DEFAULT_CURSOR) : new Cursor(Cursor.HAND_CURSOR));
            letterBtn.setEnabled(!isUsed); // Disable nếu đã dùng
            
            if (!isUsed) {
                letterBtn.addActionListener(e -> selectLetter(index));
            }
            
            lettersPanel.add(letterBtn);
        }

        lettersPanel.revalidate();
        lettersPanel.repaint();
    }

    private void selectLetter(int index) {
        // Kiểm tra xem game đã kết thúc chưa
        if (gameEnded) {
            return;
        }
        
        // Tìm vị trí trống đầu tiên trong answerLetters
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
            
            updateAnswerPanel();
            updateLettersPanel();
            
            // Kiểm tra xem hết chữ cái chưa
            checkAllLettersUsed();
        }
    }

    private void removeLetter(int index) {
        // Kiểm tra xem game đã kết thúc chưa
        if (gameEnded) {
            return;
        }
        
        // index là vị trí trong answerLetters
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
            // Đánh dấu round đã kết thúc để timer không gọi lại
            roundEnded = true;
            if (gameTimer != null) {
                gameTimer.stop();
            }

            // Chờ 500ms rồi chuyển sang round tiếp
            Timer delayTimer = new Timer(500, e -> {
                resetRound();
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }

    private void resetRound() {
        // Cộng điểm: mỗi ký tự đúng = +10đ
        int correctCount = 0;
        for (int i = 0; i < correctWord.length(); i++) {
            if (answerLetters[i].equals(String.valueOf(correctWord.charAt(i)))) {
                correctCount++;
            }
        }
        
        // Cộng điểm cho player dựa trên số ký tự đúng
        int pointsEarned = correctCount * 10;
        playerScore += pointsEarned;

        playerScorePanel.repaint();

        if (currentRound < TOTAL_ROUNDS) {
            // Chuẩn bị round tiếp theo
            currentRound++;
            roundLabel.setText("Round: " + currentRound + "/" + TOTAL_ROUNDS);
            timeRemaining = TIME_PER_ROUND;
            timerLabel.setText(timeRemaining + "s");
            timerLabel.setForeground(new Color(255, 100, 100));
            progressBar.setValue(TIME_PER_ROUND);
            progressBar.setForeground(new Color(255, 165, 0));
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
                gameTimer.stop();
                // Nếu hết time mà chưa fill hết chữ cái
                if (!roundEnded) {
                    roundEnded = true;
                    Timer nextRoundTimer = new Timer(100, ev -> {
                        roundEnded = false;
                        resetRound();
                    });
                    nextRoundTimer.setRepeats(false);
                    nextRoundTimer.start();
                }
            }
        });
        gameTimer.start();
    }

    private void endGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        gameEnded = true;

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

    public void setOnGameEndCallback(Runnable callback) {
        this.onGameEnd = callback;
    }
}

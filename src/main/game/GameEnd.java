package main.game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import model.Match;
import model.Player;

public class GameEnd extends JPanel {
    private int playerScore;
    private int opponentScore;
    private String playerName;
    private String opponentName;
    private Runnable onHome;
    private Runnable onReplay;

    public GameEnd(int playerScore, int opponentScore, String playerName, String opponentName) {
        this.playerScore = playerScore;
        this.opponentScore = opponentScore;
        this.playerName = playerName;
        this.opponentName = opponentName;

        setPreferredSize(new Dimension(1000, 700));
        setBackground(new Color(75, 0, 130));
        setLayout(null);

        initializeComponents();
    }

    public GameEnd (Match match, Player player) {
        // Xác định player nào là opponent dựa vào player truyền vào
        if (match.getPlayer1().getPlayer().getId() == player.getId()) {
            // player là player1
            this.playerScore = match.getPlayer1().getPoint();
            this.opponentScore = match.getPlayer2().getPoint();
            this.playerName = player.getFullName();
            this.opponentName = match.getPlayer2().getPlayer().getFullName();
        } else {
            // player là player2
            this.playerScore = match.getPlayer2().getPoint();
            this.opponentScore = match.getPlayer1().getPoint();
            this.playerName = player.getFullName();
            this.opponentName = match.getPlayer1().getPlayer().getFullName();
        }

        setPreferredSize(new Dimension(1000, 700));
        setBackground(new Color(75, 0, 130));
        setLayout(null);

        initializeComponents();
    }

    private void initializeComponents() {
        // Title
        JLabel titleLabel = new JLabel("Trò chơi kết thúc!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 50, 1000, 70);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        // Result message
        String result;
        Color resultColor;
        if (playerScore > opponentScore) {
            result = "🎉 Bạn thắng! 🎉";
            resultColor = new Color(0, 255, 100);
        } else if (playerScore < opponentScore) {
            result = "😢 Bạn thua! 😢";
            resultColor = new Color(255, 100, 100);
        } else {
            result = "🤝 Hòa! 🤝";
            resultColor = new Color(255, 200, 100);
        }

        JLabel resultLabel = new JLabel(result);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        resultLabel.setForeground(resultColor);
        resultLabel.setBounds(0, 140, 1000, 60);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(resultLabel);

        // Scores panel
        JPanel scoresPanel = new JPanel();
        scoresPanel.setLayout(new GridBagLayout());
        scoresPanel.setBackground(new Color(75, 0, 130));
        scoresPanel.setOpaque(false);
        scoresPanel.setBounds(200, 250, 600, 150);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        // Player score
        JLabel playerNameLabel = new JLabel(playerName);
        playerNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        playerNameLabel.setForeground(new Color(200, 200, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        scoresPanel.add(playerNameLabel, gbc);

        JLabel playerScoreLabel = new JLabel(String.valueOf(playerScore));
        playerScoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        playerScoreLabel.setForeground(new Color(100, 200, 255));
        gbc.gridx = 0;
        gbc.gridy = 1;
        scoresPanel.add(playerScoreLabel, gbc);

        // VS label
        JLabel vsLabel = new JLabel("VS");
        vsLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        vsLabel.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        scoresPanel.add(vsLabel, gbc);

        // Opponent score
        JLabel opponentNameLabel = new JLabel(opponentName);
        opponentNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        opponentNameLabel.setForeground(new Color(200, 200, 255));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        scoresPanel.add(opponentNameLabel, gbc);

        JLabel opponentScoreLabel = new JLabel(String.valueOf(opponentScore));
        opponentScoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        opponentScoreLabel.setForeground(new Color(255, 150, 100));
        gbc.gridx = 2;
        gbc.gridy = 1;
        scoresPanel.add(opponentScoreLabel, gbc);

        add(scoresPanel);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonsPanel.setBackground(new Color(75, 0, 130));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBounds(150, 480, 700, 120);

        // Home button
        JButton homeBtn = createButton("🏠 Trang chủ", new Color(100, 149, 237), 280, 80);
        homeBtn.addActionListener(e -> {
            if (onHome != null) {
                onHome.run();
            }
        });
        buttonsPanel.add(homeBtn);

        // Replay button
        JButton replayBtn = createButton("🔄 Chơi lại", new Color(255, 165, 0), 280, 80);
        replayBtn.addActionListener(e -> {
            if (onReplay != null) {
                onReplay.run();
            }
        });
        buttonsPanel.add(replayBtn);

        add(buttonsPanel);
    }

    private JButton createButton(String text, Color bgColor, int width, int height) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color drawColor = bgColor;
                if (getModel().isArmed()) {
                    int r = Math.max(bgColor.getRed() - 20, 0);
                    int g_val = Math.max(bgColor.getGreen() - 20, 0);
                    int b = Math.max(bgColor.getBlue() - 20, 0);
                    drawColor = new Color(r, g_val, b);
                } else if (getModel().isRollover()) {
                    int r = Math.min(bgColor.getRed() + 30, 255);
                    int g_val = Math.min(bgColor.getGreen() + 30, 255);
                    int b = Math.min(bgColor.getBlue() + 30, 255);
                    drawColor = new Color(r, g_val, b);
                }
                
                g2.setColor(drawColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(180, 180, 230));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                super.paintComponent(g);
            }
        };
        
        btn.setPreferredSize(new Dimension(width, height));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.repaint();
            }
        });
        
        return btn;
    }

    public void setOnHomeCallback(Runnable callback) {
        this.onHome = callback;
    }

    public void setOnReplayCallback(Runnable callback) {
        this.onReplay = callback;
    }
}

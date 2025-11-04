package main.game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

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
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        buttonsPanel.setBackground(new Color(75, 0, 130));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBounds(200, 480, 600, 120);

        // Home button
        JButton homeBtn = createIconButton("🏠", "Home");
        homeBtn.addActionListener(e -> {
            if (onHome != null) {
                onHome.run();
            }
        });
        buttonsPanel.add(homeBtn);

        // Replay button
        JButton replayBtn = createIconButton("🔄", "Chơi lại");
        replayBtn.addActionListener(e -> {
            if (onReplay != null) {
                onReplay.run();
            }
        });
        buttonsPanel.add(replayBtn);

        add(buttonsPanel);
    }

    private JButton createIconButton(String icon, String label) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isArmed()) {
                    g2.setColor(new Color(100, 150, 255));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(120, 170, 255));
                } else {
                    g2.setColor(new Color(100, 100, 255));
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(200, 200, 255));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                super.paintComponent(g);
            }
        };

        btn.setPreferredSize(new Dimension(120, 120));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 40));
        btn.setText(icon);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setToolTipText(label);
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

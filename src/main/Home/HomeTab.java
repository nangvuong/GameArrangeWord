package main.Home;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.util.List;
import model.Player;

public class HomeTab extends JPanel {

    private GameStartCallback gameStartCallback;
    private ChallengeCallback challengeCallback;
    private AcceptChallengeCallback acceptChallengeCallback;
    private List<Player> playerList;
    private JPanel playerListPanel;
    private JPanel centerContentPanel;
    private String currentPlayerUsername;

    // Colors
    private final Color LIGHT_PURPLE = new Color(120, 60, 160);
    private final Color DARK_PURPLE = new Color(75, 0, 130);
    private final Color LIGHT_BG = new Color(245, 242, 250);

    public interface GameStartCallback {
        void onGameStart();
    }

    public interface ChallengeCallback {
        void onChallenge(Player targetPlayer);
    }

    public interface AcceptChallengeCallback {
        void onAcceptChallenge(Player challenger);
    }

    public HomeTab() {
        this(new Player(0, "Player", "player", "pass", 0, 0, 0, 0));
    }

    public HomeTab(Player currentPlayer) {
        this.currentPlayerUsername = currentPlayer.getFullName();
        this.playerList = new ArrayList<>();

        // Mock data
        initializeMockPlayers();

        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);

        // Left panel - Player list
        JPanel leftPanel = createLeftPanel();

        // Center panel - Content
        centerContentPanel = createCenterPanel();

        add(leftPanel, BorderLayout.WEST);
        add(centerContentPanel, BorderLayout.CENTER);
    }

    private void initializeMockPlayers() {
        // rating = tổng điểm qua tất cả games
        playerList.add(new Player(1, "Player A", "Player_A", "pass", 19, 20, 2850, 0));
        playerList.add(new Player(2, "Player B", "Player_B", "pass", 17, 20, 2720, 0));
        playerList.add(new Player(3, "Player C", "Player_C", "pass", 15, 20, 2650, -1));
        playerList.add(new Player(4, "Player D", "Player_D", "pass", 18, 20, 2900, 0));
        playerList.add(new Player(5, "Player E", "Player_E", "pass", 16, 20, 2600, 1));
        playerList.add(new Player(6, "Player F", "Player_F", "pass", 14, 20, 2400, -1));
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(280, 0));
        leftPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Title
        JLabel title = new JLabel("👥 Danh sách người chơi");
        title.setFont(new Font("SF Pro Display", Font.BOLD, 16));
        title.setForeground(DARK_PURPLE);

        // Player list
        playerListPanel = new JPanel();
        playerListPanel.setLayout(new BoxLayout(playerListPanel, BoxLayout.Y_AXIS));
        playerListPanel.setOpaque(false);

        for (Player player : playerList) {
            if (!player.getUsername().equals(currentPlayerUsername)) {
                playerListPanel.add(createPlayerCard(player));
                playerListPanel.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scrollPane = new JScrollPane(playerListPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);

        leftPanel.add(title, BorderLayout.NORTH);
        leftPanel.add(Box.createVerticalStrut(15), BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Separator
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setForeground(new Color(200, 200, 200));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(leftPanel, BorderLayout.CENTER);
        wrapper.add(separator, BorderLayout.EAST);
        wrapper.setBackground(Color.WHITE);

        return wrapper;
    }

    private JPanel createPlayerCard(Player player) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                boolean online = player.getStatus() >= 0; // 0 = Online, 1 = Playing, -1 = Offline
                g2d.setColor(online ? new Color(76, 175, 80, 30) : new Color(200, 200, 200, 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.setColor(online ? new Color(76, 175, 80) : new Color(150, 150, 150));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                super.paintComponent(g);
            }
        };
        card.setLayout(new BorderLayout(10, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        // Player info
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(player.getUsername());
        nameLabel.setFont(new Font("SF Pro Display", Font.BOLD, 13));
        nameLabel.setForeground(DARK_PURPLE);

        String statusIcon = "";
        switch (player.getStatus()) {
            case 0:
                statusIcon = "🟢";
                break;
            case 1:
                statusIcon = "🔴";
                break;
            case -1:
                statusIcon = "⚫";
                break;
        }

        JLabel scoreLabel = new JLabel("⭐ " + String.format("%d", player.getRating()) + " điểm | " + statusIcon + " "
                + player.getStatusString());
        scoreLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 11));
        scoreLabel.setForeground(new Color(120, 120, 120));

        infoPanel.add(nameLabel, BorderLayout.NORTH);
        infoPanel.add(scoreLabel, BorderLayout.SOUTH);

        // Challenge button
        JButton challengeBtn = createChallengeButton(player);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(challengeBtn, BorderLayout.EAST);

        return card;
    }

    private JButton createChallengeButton(Player player) {
        JButton button = new JButton("+");
        button.setFont(new Font("SF Pro Display", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(LIGHT_PURPLE);
        button.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(40, 40));

        // Chỉ cho phép thách đấu những player có status = 0 (Online)
        boolean canChallenge = player.getStatus() == 0;
        button.setEnabled(canChallenge);

        if (!canChallenge) {
            button.setBackground(new Color(180, 180, 180)); // Màu xám cho disabled
            button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            button.setToolTipText("Chỉ có thể thách đấu những người chơi đang online");
        }

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (canChallenge) {
                    button.setBackground(new Color(140, 80, 180));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (canChallenge) {
                    button.setBackground(LIGHT_PURPLE);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (canChallenge && challengeCallback != null) {
                    challengeCallback.onChallenge(player);
                    // Hiển thị giao diện chờ xác nhận
                    showWaitingForResponse(player.getUsername());
                }
            }
        });

        return button;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(LIGHT_BG);
        centerPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Welcome section
        JLabel greeting = new JLabel("Chào mừng bạn đến với Word Arrange! 🎮");
        greeting.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        greeting.setForeground(DARK_PURPLE);

        JLabel subtitle = new JLabel("Xáo trộn chữ cái, sắp xếp thành từ đúng, giành chiến thắng!");
        subtitle.setFont(new Font("SF Pro Display", Font.PLAIN, 15));
        subtitle.setForeground(new Color(100, 100, 100));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        topPanel.add(greeting);
        topPanel.add(Box.createVerticalStrut(8));
        topPanel.add(subtitle);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);

        // Play button
        JButton playBtn = createPlayButton("▶ Bắt đầu chơi");
        playBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameStartCallback != null) {
                    gameStartCallback.onGameStart();
                }
            }
        });
        bottomPanel.add(playBtn);

        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        return centerPanel;
    }

    private JButton createPlayButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(LIGHT_PURPLE);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(140, 80, 180));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(LIGHT_PURPLE);
            }
        });

        return btn;
    }

    // Callback methods
    public void setStartGameCallback(GameStartCallback callback) {
        this.gameStartCallback = callback;
    }

    public void setChallengeCallback(ChallengeCallback callback) {
        this.challengeCallback = callback;
    }

    public void setAcceptChallengeCallback(AcceptChallengeCallback callback) {
        this.acceptChallengeCallback = callback;
    }

    // Method to show challenge invitation
    public void showChallengeInvitation(Player challenger) {
        JPanel challengePanel = createChallengeInvitationPanel(challenger);
        centerContentPanel.removeAll();
        centerContentPanel.add(challengePanel, BorderLayout.CENTER);
        centerContentPanel.revalidate();
        centerContentPanel.repaint();
    }

    // Method to show waiting for response screen
    public void showWaitingForResponse(String targetPlayerUsername) {
        JPanel waitingPanel = createWaitingPanel(targetPlayerUsername);
        centerContentPanel.removeAll();
        centerContentPanel.add(waitingPanel, BorderLayout.CENTER);
        centerContentPanel.revalidate();
        centerContentPanel.repaint();
    }

    private JPanel createWaitingPanel(String targetPlayerUsername) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Waiting card
        JPanel waitingCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2d.setColor(new Color(LIGHT_PURPLE.getRed(), LIGHT_PURPLE.getGreen(), LIGHT_PURPLE.getBlue(), 20));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2d.setColor(LIGHT_PURPLE);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                super.paintComponent(g);
            }
        };
        waitingCard.setLayout(new BoxLayout(waitingCard, BoxLayout.Y_AXIS));
        waitingCard.setBackground(Color.WHITE);
        waitingCard.setBorder(new EmptyBorder(40, 40, 40, 40));
        waitingCard.setMaximumSize(new Dimension(600, 350));

        // Loading animation (rotating icon)
        JLabel loadingIcon = new JLabel("⏳");
        loadingIcon.setFont(new Font("SF Pro Display", Font.PLAIN, 80));
        loadingIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel message = new JLabel("Đang chờ phản hồi...");
        message.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        message.setForeground(DARK_PURPLE);
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel targetLabel = new JLabel("Bạn đã gửi lời mời cho: " + targetPlayerUsername);
        targetLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 16));
        targetLabel.setForeground(new Color(100, 100, 100));
        targetLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel waitingHint = new JLabel("Vui lòng chờ người chơi xác nhận lời mời...");
        waitingHint.setFont(new Font("SF Pro Display", Font.ITALIC, 13));
        waitingHint.setForeground(new Color(150, 150, 150));
        waitingHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Cancel button
        JButton cancelBtn = createActionButton("✕ Hủy lời mời", new Color(220, 20, 60));
        cancelBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showDefaultContent();
            }
        });

        waitingCard.add(loadingIcon);
        waitingCard.add(Box.createVerticalStrut(20));
        waitingCard.add(message);
        waitingCard.add(Box.createVerticalStrut(15));
        waitingCard.add(targetLabel);
        waitingCard.add(Box.createVerticalStrut(10));
        waitingCard.add(waitingHint);
        waitingCard.add(Box.createVerticalStrut(30));
        waitingCard.add(cancelBtn);

        // Add animated dots
        JLabel dotsLabel = new JLabel("● ● ●");
        dotsLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 20));
        dotsLabel.setForeground(LIGHT_PURPLE);
        dotsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        waitingCard.add(Box.createVerticalStrut(15));
        waitingCard.add(dotsLabel);

        // Animate the dots
        animateDots(dotsLabel);

        panel.add(Box.createVerticalGlue());
        panel.add(waitingCard);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void animateDots(JLabel dotsLabel) {
        Timer timer = new Timer(500, e -> {
            String text = dotsLabel.getText();
            String newText;
            if (text.equals("● ● ●")) {
                newText = "○ ● ●";
            } else if (text.equals("○ ● ●")) {
                newText = "○ ○ ●";
            } else if (text.equals("○ ○ ●")) {
                newText = "○ ○ ○";
            } else if (text.equals("○ ○ ○")) {
                newText = "● ○ ○";
            } else if (text.equals("● ○ ○")) {
                newText = "● ● ○";
            } else {
                newText = "● ● ●";
            }
            dotsLabel.setText(newText);
        });
        timer.setRepeats(true);
        timer.start();
    }

    private JPanel createChallengeInvitationPanel(Player challenger) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Invitation card
        JPanel invitationCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2d.setColor(new Color(LIGHT_PURPLE.getRed(), LIGHT_PURPLE.getGreen(), LIGHT_PURPLE.getBlue(), 20));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2d.setColor(LIGHT_PURPLE);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                super.paintComponent(g);
            }
        };
        invitationCard.setLayout(new BoxLayout(invitationCard, BoxLayout.Y_AXIS));
        invitationCard.setBackground(Color.WHITE);
        invitationCard.setBorder(new EmptyBorder(40, 40, 40, 40));
        invitationCard.setMaximumSize(new Dimension(600, 300));

        JLabel icon = new JLabel("⚔️");
        icon.setFont(new Font("SF Pro Display", Font.PLAIN, 60));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel message = new JLabel("Bạn được thách đấu!");
        message.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        message.setForeground(DARK_PURPLE);
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel challengerLabel = new JLabel("từ: " + challenger.getUsername());
        challengerLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 18));
        challengerLabel.setForeground(new Color(100, 100, 100));
        challengerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton acceptBtn = createActionButton("✓ Chấp nhận", new Color(76, 175, 80));
        acceptBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (acceptChallengeCallback != null) {
                    acceptChallengeCallback.onAcceptChallenge(challenger);
                }
                showDefaultContent();
            }
        });

        JButton declineBtn = createActionButton("✗ Từ chối", new Color(220, 20, 60));
        declineBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showDefaultContent();
            }
        });

        buttonPanel.add(acceptBtn);
        buttonPanel.add(declineBtn);

        invitationCard.add(icon);
        invitationCard.add(Box.createVerticalStrut(15));
        invitationCard.add(message);
        invitationCard.add(Box.createVerticalStrut(10));
        invitationCard.add(challengerLabel);
        invitationCard.add(Box.createVerticalStrut(25));
        invitationCard.add(buttonPanel);

        panel.add(Box.createVerticalGlue());
        panel.add(invitationCard);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color drawColor = bgColor;
                if (getModel().isArmed()) {
                    drawColor = new Color(Math.max(bgColor.getRed() - 20, 0),
                            Math.max(bgColor.getGreen() - 20, 0),
                            Math.max(bgColor.getBlue() - 20, 0));
                } else if (getModel().isRollover()) {
                    drawColor = new Color(Math.min(bgColor.getRed() + 30, 255),
                            Math.min(bgColor.getGreen() + 30, 255),
                            Math.min(bgColor.getBlue() + 30, 255));
                }

                g2d.setColor(drawColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                super.paintComponent(g);
            }
        };

        button.setFont(new Font("SF Pro Display", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(150, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }
        });

        return button;
    }

    public void updatePlayerList(List<Player> newPlayerList) {
        this.playerList = newPlayerList;
        playerListPanel.removeAll();

        for (Player player : playerList) {
            if (!player.getUsername().equals(currentPlayerUsername)) {
                playerListPanel.add(createPlayerCard(player));
                playerListPanel.add(Box.createVerticalStrut(10));
            }
        }

        playerListPanel.revalidate();
        playerListPanel.repaint();
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void showDefaultContent() {
        centerContentPanel.removeAll();
        centerContentPanel.add(createCenterPanel(), BorderLayout.CENTER);
        centerContentPanel.revalidate();
        centerContentPanel.repaint();
    }

    public void showWaitingForGameStart(String opponentName) {
        SwingUtilities.invokeLater(() -> {
            JPanel waitingPanel = new JPanel(new BorderLayout());
            waitingPanel.setBackground(Color.WHITE);
            waitingPanel.setBorder(new EmptyBorder(50, 30, 50, 30));

            JLabel titleLabel = new JLabel("🎮 Game sắp bắt đầu!", SwingConstants.CENTER);
            titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 24));
            titleLabel.setForeground(new Color(120, 60, 160));

            JLabel messageLabel = new JLabel("<html><center>Đang chuẩn bị game với <b>" + opponentName
                    + "</b><br>Vui lòng chờ trong giây lát...</center></html>", SwingConstants.CENTER);
            messageLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 16));
            messageLabel.setForeground(Color.GRAY);

            JPanel loadingPanel = new JPanel(new FlowLayout());
            loadingPanel.setOpaque(false);

            JLabel loadingLabel = new JLabel("●●●");
            loadingLabel.setFont(new Font("SF Pro Display", Font.BOLD, 20));
            loadingLabel.setForeground(new Color(100, 149, 237));
            loadingPanel.add(loadingLabel);

            waitingPanel.add(titleLabel, BorderLayout.NORTH);
            waitingPanel.add(messageLabel, BorderLayout.CENTER);
            waitingPanel.add(loadingPanel, BorderLayout.SOUTH);

            removeAll();
            add(waitingPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        });
    }
}

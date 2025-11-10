package main.Home;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Player;
import network.PlayerIO;
import network.Server;

public class Home extends JPanel {

    private JPanel tabContentPanel;
    private HomeTab homeTab;
    private TabButton homeTabBtn;
    private TabButton profileTabBtn;
    private TabButton historyTabBtn;
    private TabButton rankTabBtn;
    private LogoutCallback logoutCallback;
    private GameStartCallback gameStartCallback;
    private Player currentPlayer;
    private Player challengerPlayer;
    PlayerIO playerIO;

    public interface LogoutCallback {
        void onLogout();
    }

    public interface GameStartCallback {
        void onGameStart();
    }


    public Home(Player currentPlayer, Server server) {
        setLayout(new BorderLayout());
        playerIO = new PlayerIO(server);
        setBackground(new Color(240, 242, 245));
        this.currentPlayer = currentPlayer;

        JPanel headerPanel = createHeaderPanel();
        tabContentPanel = new JPanel(new CardLayout());
        tabContentPanel.setBackground(Color.WHITE);
        
        homeTab = new HomeTab(currentPlayer);
        homeTab.setStartGameCallback(() -> {
            if (gameStartCallback != null) {
                gameStartCallback.onGameStart();
            }
        });
        homeTab.setChallengeCallback((targetPlayer) -> {
            // Handle challenge - send to target player
            System.out.println(currentPlayer.getFullName() + " challenged " + targetPlayer.getUsername());
        });
        homeTab.setAcceptChallengeCallback((challenger) -> {
            // Handle accept challenge - start game with challenger
            System.out.println(currentPlayer.getFullName() + " accepted challenge from " + challenger.getUsername());
            challengerPlayer = challenger;
            if (gameStartCallback != null) {
                gameStartCallback.onGameStart();
            }
        });
        
        tabContentPanel.add(homeTab, "HOME");
        tabContentPanel.add(new ProfileTab(currentPlayer), "PROFILE");
        tabContentPanel.add(new HistoryTab(currentPlayer), "HISTORY");
        tabContentPanel.add(new RankTab(homeTab.getPlayerList(), currentPlayer), "RANK");

        add(headerPanel, BorderLayout.NORTH);
        add(tabContentPanel, BorderLayout.CENTER);
        showTab("HOME");
    }

    private JPanel createHeaderPanel() {
        JPanel mainHeaderPanel = new JPanel(new BorderLayout());
        mainHeaderPanel.setBackground(new Color(120, 60, 160)); // Tím nhạt
        mainHeaderPanel.setPreferredSize(new Dimension(0, 70));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(120, 60, 160)); // Tím nhạt
        headerPanel.setLayout(new GridBagLayout());
        headerPanel.setBorder(new EmptyBorder(12, 25, 12, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 20);

        // Title
        JLabel title = new JLabel("Word Arrange");
        title.setFont(new Font("SF Pro Display", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.weightx = 0;
        headerPanel.add(title, gbc);

        // Tabs
        JPanel tabsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        tabsPanel.setOpaque(false);

        homeTabBtn = createTabButton("🏠 Home");
        profileTabBtn = createTabButton("👤 Profile");
        historyTabBtn = createTabButton("📊 History");
        rankTabBtn = createTabButton("🏆 Rank");

        homeTabBtn.addActionListener(e -> { showTab("HOME"); updateTabButtons("HOME"); });
        profileTabBtn.addActionListener(e -> { showTab("PROFILE"); updateTabButtons("PROFILE"); });
        historyTabBtn.addActionListener(e -> { showTab("HISTORY"); updateTabButtons("HISTORY"); });
        rankTabBtn.addActionListener(e -> { showTab("RANK"); updateTabButtons("RANK"); });

        tabsPanel.add(homeTabBtn);
        tabsPanel.add(profileTabBtn);
        tabsPanel.add(historyTabBtn);
        tabsPanel.add(rankTabBtn);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        headerPanel.add(tabsPanel, gbc);

        // Logout button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        JButton logoutBtn = createLogoutButton("🚪 Logout");
        logoutBtn.addActionListener(e -> { 
            if (logoutCallback != null) {
                try {
                    playerIO.logout();
                } catch (IOException ex) {
                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                }
                logoutCallback.onLogout(); 
            }
        });
        rightPanel.add(logoutBtn);

        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 20, 0, 0);
        headerPanel.add(rightPanel, gbc);

        // Separator line
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(100, 40, 140)); // Tím đậm
        separator.setPreferredSize(new Dimension(0, 2));

        mainHeaderPanel.add(headerPanel, BorderLayout.CENTER);
        mainHeaderPanel.add(separator, BorderLayout.SOUTH);
        return mainHeaderPanel;
    }

    private TabButton createTabButton(String text) {
        TabButton btn = new TabButton(text);
        btn.setFont(new Font("SF Pro Display", Font.PLAIN, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(120, 60, 160)); // Tím nhạt
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(new Color(220, 200, 255)); }
            public void mouseExited(MouseEvent e) { btn.setForeground(Color.WHITE); }
        });
        return btn;
    }

    private JButton createLogoutButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SF Pro Display", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(255, 165, 0)); // Cam
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(255, 180, 40)); } // Cam sáng
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(255, 165, 0)); } // Cam
        });
        return btn;
    }

    private void updateTabButtons(String activeTab) {
        homeTabBtn.setActive(activeTab.equals("HOME"));
        profileTabBtn.setActive(activeTab.equals("PROFILE"));
        historyTabBtn.setActive(activeTab.equals("HISTORY"));
        rankTabBtn.setActive(activeTab.equals("RANK"));
    }

    private void showTab(String tabName) {
        CardLayout cl = (CardLayout) (tabContentPanel.getLayout());
        cl.show(tabContentPanel, tabName);
    }

    public void setLogoutCallback(LogoutCallback callback) {
        this.logoutCallback = callback;
    }

    public void setGameStartCallback(GameStartCallback callback) {
        this.gameStartCallback = callback;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getChallengerPlayer() {
        return challengerPlayer;
    }

    public void setChallengerPlayer(Player challenger) {
        this.challengerPlayer = challenger;
    }

    public void showChallengeInvitation(Player challenger) {
        homeTab.showChallengeInvitation(challenger);
    }

    public void sendChallengeInvitation(Player targetPlayer) {
        homeTab.showWaitingForResponse(targetPlayer.getUsername());
    }

    // Custom TabButton class with underline for active state
    private static class TabButton extends JButton {
        private boolean isActive = false;

        public TabButton(String text) {
            super(text);
        }

        public void setActive(boolean active) {
            this.isActive = active;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (isActive) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);
            }
        }
    }
}

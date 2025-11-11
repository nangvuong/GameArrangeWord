package main.Home;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Player;
import network.GameClient;
import network.PlayerIO;
import network.ServerMessageListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Home extends JPanel implements ServerMessageListener {

    private JPanel tabContentPanel;
    private HomeTab homeTab;
    private RankTab rankTab;
    private TabButton homeTabBtn;
    private TabButton rankTabBtn;
    private LogoutCallback logoutCallback;
    private GameStartCallback gameStartCallback;
    private Player currentPlayer;
    private Player challengerPlayer;
    private GameClient client;
    private int gameMatchId = 0;
    private JSONObject gameQuestion = null;
    private JSONObject gameSelf = null;
    private JSONObject gameOpponent = null;

    public int getGameMatchId() {
        return gameMatchId;
    }

    public JSONObject getGameQuestion() {
        return gameQuestion;
    }

    public JSONObject getGameSelf() {
        return gameSelf;
    }

    public JSONObject getGameOpponent() {
        return gameOpponent;
    }

    public interface LogoutCallback {
        void onLogout();
    }

    public interface GameStartCallback {
        void onGameStart();
    }

    public Home(Player currentPlayer) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));
        this.currentPlayer = currentPlayer;
        this.client = GameClient.getInstance();

        // Đăng ký listener để nhận messages từ server
        client.addMessageListener(this);

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
            // Gửi lời mời chơi đến server
            System.out.println(currentPlayer.getFullName() + " challenged " + targetPlayer.getUsername());
            PlayerIO.inviteUserToGame(targetPlayer.getUsername());
            homeTab.showWaitingForResponse(targetPlayer.getUsername());
        });
        homeTab.setAcceptChallengeCallback((challenger) -> {
            System.out.println(currentPlayer.getFullName() + " accepted challenge from " + challenger.getUsername());
            challengerPlayer = challenger;
            PlayerIO.respondToInvitation(challenger.getUsername(), true);

            // ← XÓA DÒNG NÀY: gameStartCallback.onGameStart();

            // Hiển thị thông báo chờ đợi
            // homeTab.showWaitingForGameStart(challenger.getNickname());
        });

        tabContentPanel.add(homeTab, "HOME");

        // Tạo RankTab và lưu reference
        rankTab = new RankTab(homeTab.getPlayerList(), currentPlayer);
        tabContentPanel.add(rankTab, "RANK");

        add(headerPanel, BorderLayout.NORTH);
        add(tabContentPanel, BorderLayout.CENTER);

        // Request danh sách online users khi vào Home
        PlayerIO.requestOnlineUsers();

        showTab("HOME");
    }

    @Override
    public void onMessageReceived(String action, JSONObject message) {
        switch (action) {
            case "GET_ONLINE_USERS_RESPONSE":
                handleOnlineUsersResponse(message);
                break;
            case "INVITE_USER_TO_GAME_REQUEST":
                handleInvitationReceived(message);
                break;
            case "INVITE_USER_TO_GAME_RESPONSE":
                handleInvitationResponse(message);
                break;
            case "START_GAME":
                handleStartGame(message);
                break;
            case "GET_RANKING_RESPONSE":
                handleRankingResponse(message);
                break;
            case "GAME_RESULT":
                handleGameResult(message);
                break;
            case "GAME_FINAL_RESULT":
                handleGameFinalResult(message);
                break;
            case "CONTINUE_NEXT_GAME":
                handleContinueNextGame(message);
                break;
        }
    }

    private void handleOnlineUsersResponse(JSONObject message) {
        JSONArray usersArray = message.optJSONArray("onlineUsers");
        if (usersArray != null) {
            List<Player> onlinePlayers = new ArrayList<>();
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userJson = usersArray.getJSONObject(i);

                // Không thêm chính mình vào list
                if (userJson.getString("username").equals(currentPlayer.getUsername())) {
                    continue;
                }

                Player player = new Player(
                        userJson.getInt("id"),
                        userJson.getString("nickname"),
                        userJson.getString("username"),
                        "",
                        userJson.getInt("totalWins"),
                        userJson.getInt("totalMatches"),
                        userJson.getInt("totalScore"),
                        userJson.getBoolean("isPlaying") ? 1 : 0);
                onlinePlayers.add(player);
            }

            // Update player list trong HomeTab
            homeTab.updatePlayerList(onlinePlayers);
            
            System.out.println("✅ Updated online users: " + onlinePlayers.size() + " players");
        }
    }

    private void handleInvitationReceived(JSONObject message) {
        String inviterUsername = message.optString("inviterUsername", "");
        String inviterNickname = message.optString("inviterNickname", "");

        // Tạo Player object cho người mời
        Player inviter = new Player(0, inviterNickname, inviterUsername, "", 0, 0, 0, 0);

        // Hiển thị invitation dialog
        homeTab.showChallengeInvitation(inviter);
        System.out.println("📨 Received invitation from: " + inviterUsername);
    }

    private void handleInvitationResponse(JSONObject message) {
        boolean accepted = message.optBoolean("accepted", false);
        String opponentNickname = message.optString("opponentNickname", "");

        if (accepted) {
            gameStartCallback.onGameStart();
        } else {
            System.out.println("❌ Invitation declined by " + opponentNickname);
            JOptionPane.showMessageDialog(this,
                    "Người chơi " + opponentNickname + " đã từ chối lời mời!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            homeTab.showDefaultContent();
        }
    }

    private void handleRankingResponse(JSONObject message) {
        JSONArray rankingArray = message.optJSONArray("ranking");
        if (rankingArray != null) {
            List<Player> rankedPlayers = new ArrayList<>();
            for (int i = 0; i < rankingArray.length(); i++) {
                JSONObject userJson = rankingArray.getJSONObject(i);
                Player player = new Player(
                        userJson.getInt("id"),
                        userJson.getString("nickname"),
                        userJson.getString("username"),
                        "",
                        userJson.getInt("totalWins"),
                        userJson.getInt("totalMatches"),
                        userJson.getInt("totalScore"),
                        0);
                rankedPlayers.add(player);
            }

            // Update RankTab với dữ liệu mới
            rankTab.updateRankList(rankedPlayers, currentPlayer);
            System.out.println("✅ Updated ranking: " + rankedPlayers.size() + " players");
        }
    }

    private JPanel createHeaderPanel() {
        JPanel mainHeaderPanel = new JPanel(new BorderLayout());
        mainHeaderPanel.setBackground(new Color(120, 60, 160));
        mainHeaderPanel.setPreferredSize(new Dimension(0, 70));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(120, 60, 160));
        headerPanel.setLayout(new GridBagLayout());
        headerPanel.setBorder(new EmptyBorder(12, 25, 12, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 20);

        JLabel title = new JLabel("Word Arrange");
        title.setFont(new Font("SF Pro Display", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.weightx = 0;
        headerPanel.add(title, gbc);

        JPanel tabsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        tabsPanel.setOpaque(false);

        homeTabBtn = createTabButton("🏠 Home");
        rankTabBtn = createTabButton("🏆 Rank");

        homeTabBtn.addActionListener(e -> {
            showTab("HOME");
            updateTabButtons("HOME");
        });
        rankTabBtn.addActionListener(e -> {
            showTab("RANK");
            updateTabButtons("RANK");
            // Request ranking khi click vào tab Rank
            PlayerIO.requestRanking();
        });

        tabsPanel.add(homeTabBtn);
        tabsPanel.add(rankTabBtn);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        headerPanel.add(tabsPanel, gbc);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        JButton logoutBtn = createLogoutButton("🚪 Logout");
        logoutBtn.addActionListener(e -> {
            if (logoutCallback != null) {
                PlayerIO.logout();
                client.removeMessageListener(this);
                logoutCallback.onLogout();
            }
        });
        rightPanel.add(logoutBtn);

        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 20, 0, 0);
        headerPanel.add(rightPanel, gbc);

        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(100, 40, 140));
        separator.setPreferredSize(new Dimension(0, 2));

        mainHeaderPanel.add(headerPanel, BorderLayout.CENTER);
        mainHeaderPanel.add(separator, BorderLayout.SOUTH);
        return mainHeaderPanel;
    }

    private TabButton createTabButton(String text) {
        TabButton btn = new TabButton(text);
        btn.setFont(new Font("SF Pro Display", Font.PLAIN, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(120, 60, 160));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(new Color(220, 200, 255));
            }

            public void mouseExited(MouseEvent e) {
                btn.setForeground(Color.WHITE);
            }
        });
        return btn;
    }

    private JButton createLogoutButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SF Pro Display", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(255, 165, 0));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(255, 180, 40));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(255, 165, 0));
            }
        });
        return btn;
    }

    private void updateTabButtons(String activeTab) {
        homeTabBtn.setActive(activeTab.equals("HOME"));
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

    public void handleStartGame(JSONObject message) {
        int matchId = message.optInt("matchId", 0);
        JSONObject questionJson = message.optJSONObject("question");
        JSONObject selfJson = message.optJSONObject("self");
        JSONObject opponentJson = message.optJSONObject("opponent");
        System.out.println("🎮 START_GAME received!");
        System.out.println("📝 MatchID: " + matchId);
        System.out.println("📝 Instruction: " + questionJson.optString("instruction", ""));
        // Bắt đầu game với data từ server
        if (gameStartCallback != null) {
            // Lưu game data vào Home để Main.java lấy
            this.gameMatchId = matchId;
            this.gameQuestion = questionJson;
            this.gameSelf = selfJson;
            this.gameOpponent = opponentJson;

            gameStartCallback.onGameStart();
        }
    }

    private void handleContinueNextGame(JSONObject message) {
        JSONObject questionJson = message.optJSONObject("question");

        System.out.println("🎮 CONTINUE_NEXT_GAME received!");
        System.out.println("📝 Next round instruction: " + questionJson.optString("instruction", ""));

        // Update question data cho round mới
        this.gameQuestion = questionJson;

        // Trigger game to load next round
        if (gameStartCallback != null) {
            gameStartCallback.onGameStart();
        }
    }

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

    private void handleGameResult(JSONObject message) {
        // Nhận kết quả từng round từ server
        int matchId = message.optInt("matchId", 0);
        int roundScore1 = message.optInt("roundScore1", 0);
        int roundScore2 = message.optInt("roundScore2", 0);
        String roundResult1 = message.optString("roundResult1", "");
        String roundResult2 = message.optString("roundResult2", "");

        JSONObject matchSummary = message.optJSONObject("matchSummary");

        System.out.println("🎮 Round result: " + roundResult1 + " (+" + roundScore1 + " pts)");
        System.out.println("📊 Match score: " + matchSummary.optString("score", ""));

        // TODO: Có thể hiển thị notification cho user về kết quả round
    }

    private void handleGameFinalResult(JSONObject message) {
        // Nhận kết quả cuối game từ server
        int matchId = message.optInt("matchId", 0);
        JSONObject matchSummary = message.optJSONObject("matchSummary");
        JSONObject player1 = message.optJSONObject("player1");
        JSONObject player2 = message.optJSONObject("player2");

        System.out.println("🏁 Game finished!");
        System.out.println("📊 Final score: " + matchSummary.optString("score", ""));

        // TODO: Có thể hiển thị kết quả cuối cùng cho user
    }
}
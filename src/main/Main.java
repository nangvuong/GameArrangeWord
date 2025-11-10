package main;

import javax.swing.*;

import org.json.JSONObject;

import com.formdev.flatlaf.FlatLightLaf;

import main.Home.Home;
import main.auth.Login;
import main.auth.Register;
import main.game.GamePlay;
import main.game.GameStart;
import main.game.SortingGamePlay;
import model.Player;
import model.Playing;
import model.Match;
import model.Round;
import model.Word;
import network.GameClient;
import main.game.GameEnd;

public class Main {
    private static JFrame window;
    private static Login loginPanelRef;
    private static Player currentPlayer;
    private static GameClient gameClient;
    private static int currentRoundNumber = 1;

    public static void main(String[] args) {
        // Áp dụng giao diện FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        gameClient = GameClient.getInstance();

        // Tạo cửa sổ chính
        window = new JFrame("Word Arrange");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Thêm WindowListener để đóng connection khi thoát
        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (gameClient.isConnected()) {
                    gameClient.disconnect();
                }
                System.exit(0);
            }
        });
        window.setResizable(false);
        window.setSize(1000, 700);
        window.setLocationRelativeTo(null);

        loginPanelRef = new Login((username) -> {
            currentPlayer = loginPanelRef.getPlayer();
            showHomeScreen(currentPlayer);
        });

        loginPanelRef.setRegisterCallback(() -> {
            final Register[] registerPanelRef = new Register[1];
            registerPanelRef[0] = new Register(() -> {
                currentPlayer = registerPanelRef[0].getNewPlayer();
                showHomeScreen(currentPlayer);
            });

            registerPanelRef[0].setBackCallback(() -> {
                loginPanelRef.resetForm();
                window.setContentPane(loginPanelRef);
                window.revalidate();
                window.repaint();
            });

            window.setContentPane(registerPanelRef[0]);
            window.revalidate();
            window.repaint();
        });

        window.add(loginPanelRef);
        window.setVisible(true);
    }

    private static void showHomeScreen(Player currentPlayer) {
        currentRoundNumber = 1;
        Home homePanel = new Home(currentPlayer);
        homePanel.setLogoutCallback(() -> {
            // Disconnect khi logout
            if (gameClient.isConnected()) {
                gameClient.disconnect();
            }
            loginPanelRef.resetForm();
            window.setContentPane(loginPanelRef);
            window.revalidate();
            window.repaint();
        });
        homePanel.setGameStartCallback(() -> {
            // Kiểm tra nếu có matchId từ server → vào game trực tiếp
            if (homePanel.getGameMatchId() > 0) {
                showGamePlay(homePanel); // ← Vào game TRỰC TIẾP, bỏ qua màn Start
            } else {
                System.out.println("vao day ma");
                showGameStart(homePanel); // ← Chỉ show màn Start khi practice mode
            }
        });
        window.setContentPane(homePanel);
        window.revalidate();
        window.repaint();
    }

    private static void showGameStart(Home homePanel) {
        GameStart gameStartPanel = new GameStart(() -> {
            showGamePlay(homePanel);
        });
        window.setContentPane(gameStartPanel);
        window.revalidate();
        window.repaint();
    }

    private static void showGamePlay(Home homePanel) {
        // Check if game data from server exists
        if (homePanel.getGameMatchId() > 0 && homePanel.getGameQuestion() != null) {
            // Real multiplayer game with data from server
            int matchId = homePanel.getGameMatchId();
            JSONObject questionData = homePanel.getGameQuestion();
            JSONObject self = homePanel.getGameSelf();
            JSONObject opponent = homePanel.getGameOpponent();

            System.out.println("🎮 Starting REAL multiplayer game!");

            SortingGamePlay gamePlayPanel = new SortingGamePlay(matchId, questionData, self, opponent,
                    currentRoundNumber);
            gamePlayPanel.setOnGameEndCallback(() -> {
                currentRoundNumber++; // Tăng round sau mỗi lần chơi xong
                // Chờ server gửi GAME_RESULT hoặc CONTINUE_NEXT_GAME
                showHome(homePanel);
            });

            window.setContentPane(gamePlayPanel);
            window.revalidate();
            window.repaint();
        } else {
            // Practice mode with AI (no server)
            System.out.println("🤖 Starting practice mode with AI");
            JOptionPane.showMessageDialog(window,
                    "Practice mode - Playing with AI\n(No server connection)",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            showHome(homePanel);
        }
    }

    private static void showGameEnd(GamePlay gamePlayPanel, String username, Home homePanel) {
        Match finishedMatch = gamePlayPanel.getMatch();
        Player currentPlayer = homePanel.getCurrentPlayer();

        GameEnd gameEndPanel = new GameEnd(finishedMatch, currentPlayer);

        // Home button callback - go back to Home tab
        gameEndPanel.setOnHomeCallback(() -> {
            showHome(homePanel);
        });

        // Replay button callback - show challenge invitation with opponent again
        gameEndPanel.setOnReplayCallback(() -> {
            // Get opponent from match
            Player opponent = finishedMatch.getPlayer2().getPlayer();
            if (!opponent.getUsername().equals("ai")) {
                // Real player - send challenge invitation and wait for response
                homePanel.sendChallengeInvitation(opponent);
                showHome(homePanel);
            } else {
                // AI opponent - just go back to home for now
                showHome(homePanel);
            }
        });

        window.setContentPane(gameEndPanel);
        window.revalidate();
        window.repaint();
    }

    private static void showHome(Home homePanel) {
        window.setContentPane(homePanel);
        window.revalidate();
        window.repaint();
    }
}

package main;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

import main.Home.Home;
import main.auth.Login;
import main.auth.Register;
import main.game.GamePlay;
import main.game.GameStart;
import model.Player;
import model.Playing;
import model.Match;
import model.Round;
import model.Word;
import main.game.GameEnd;

public class Main {
    private static JFrame window;
    private static Login loginPanelRef;
    private static Player currentPlayer;
    
    public static void main(String[] args) {
        // Áp dụng giao diện FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tạo cửa sổ chính
        window = new JFrame("Word Arrange");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        Home homePanel = new Home(currentPlayer);
        homePanel.setLogoutCallback(() -> {
            loginPanelRef.resetForm();
            window.setContentPane(loginPanelRef);
            window.revalidate();
            window.repaint();
        });
        homePanel.setGameStartCallback(() -> {
            showGameStart(homePanel);
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
        // Create Match with current player and opponent
        Player currentPlayer = homePanel.getCurrentPlayer();
        Player opponent;
        
        // Check if there's a challenger (from accepted challenge)
        if (homePanel.getChallengerPlayer() != null) {
            opponent = homePanel.getChallengerPlayer();
        } else {
            // Create AI opponent if no challenger
            opponent = new Player("AI", "ai", "password");
            opponent.setStatus(0); // Online
        }
        
        Playing player1Playing = new Playing(0, "ONGOING", currentPlayer);
        Playing player2Playing = new Playing(0, "ONGOING", opponent);
        Match match = new Match(player1Playing, player2Playing, new java.sql.Date(System.currentTimeMillis()));
        
        // Create 10 rounds with words
        java.util.List<Round> rounds = new java.util.ArrayList<>();
        String[][] wordData = {
            {"EFFECT", "result of influence"},
            {"PUZZLE", "mind-bending game"},
            {"DEVELOP", "to grow or improve"},
            {"PERFECT", "flawless, ideal"},
            {"TROUBLE", "difficulty, problem"},
            {"STRANGE", "unusual, odd"},
            {"CORRECT", "accurate, right"},
            {"MACHINE", "mechanical device"},
            {"QUALITY", "degree of excellence"},
            {"HARVEST", "gather crops"}
        };
        
        for (int i = 0; i < 10; i++) {
            Word word = new Word(wordData[i][0], wordData[i][1]);
            Round round = new Round(0, i + 1, word, 0, 0);
            rounds.add(round);
        }
        match.setRounds(rounds);
        
        // Create GamePlay with Match
        GamePlay gamePlayPanel = new GamePlay(match);
        gamePlayPanel.setOnGameEndCallback(() -> {
            showGameEnd(gamePlayPanel, homePanel.getCurrentPlayer().getUsername(), homePanel);
        });
        window.setContentPane(gamePlayPanel);
        window.revalidate();
        window.repaint();
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

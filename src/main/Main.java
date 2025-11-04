package main;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

import main.Home.Home;
import main.auth.Login;
import main.auth.Register;
import main.game.GamePlay;
import main.game.GameStart;
import main.game.GameEnd;

public class Main {
    private static JFrame window;
    private static Login loginPanelRef;
    private static String currentUsername;
    
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
            currentUsername = username;
            showHomeScreen(username);
        });

        loginPanelRef.setRegisterCallback(() -> {
            Register registerPanel = new Register(() -> {
                showHomeScreen(currentUsername);
            });

            registerPanel.setBackCallback(() -> {
                loginPanelRef.resetForm();
                window.setContentPane(loginPanelRef);
                window.revalidate();
                window.repaint();
            });

            window.setContentPane(registerPanel);
            window.revalidate();
            window.repaint();
        });

        window.add(loginPanelRef);
        window.setVisible(true);
    }

    private static void showHomeScreen(String username) {
        Home homePanel = new Home(username);
        homePanel.setLogoutCallback(() -> {
            loginPanelRef.resetForm();
            window.setContentPane(loginPanelRef);
            window.revalidate();
            window.repaint();
        });
        homePanel.setGameStartCallback(() -> {
            showGameStart(username, homePanel);
        });
        window.setContentPane(homePanel);
        window.revalidate();
        window.repaint();
    }

    private static void showGameStart(String username, Home homePanel) {
        GameStart gameStartPanel = new GameStart(() -> {
            showGamePlay(username, homePanel);
        });
        window.setContentPane(gameStartPanel);
        window.revalidate();
        window.repaint();
    }

    private static void showGamePlay(String username, Home homePanel) {
        GamePlay gamePlayPanel = new GamePlay(username, "AI");
        gamePlayPanel.setOnGameEndCallback(() -> {
            showGameEnd(gamePlayPanel, username, homePanel);
        });
        window.setContentPane(gamePlayPanel);
        window.revalidate();
        window.repaint();
    }

    private static void showGameEnd(GamePlay gamePlayPanel, String username, Home homePanel) {
        GameEnd gameEndPanel = new GameEnd(
            gamePlayPanel.getPlayerScore(),
            gamePlayPanel.getOpponentScore(),
            username,
            "AI"
        );
        gameEndPanel.setOnHomeCallback(() -> {
            showHomeScreen(username);
        });
        gameEndPanel.setOnReplayCallback(() -> {
            showGameStart(username, homePanel);
        });
        window.setContentPane(gameEndPanel);
        window.revalidate();
        window.repaint();
    }
}

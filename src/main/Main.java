package main;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

import main.Home.Home;
import main.auth.Login;
import main.auth.Register;
import main.game.GamePlay;
import main.game.GameStart;
import model.Player;
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
        GamePlay gamePlayPanel = new GamePlay(homePanel.getCurrentPlayer().getUsername(), "AI");
        gamePlayPanel.setOnGameEndCallback(() -> {
            showGameEnd(gamePlayPanel, homePanel.getCurrentPlayer().getUsername(), homePanel);
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
        window.setContentPane(gameEndPanel);
        window.revalidate();
        window.repaint();
    }
}

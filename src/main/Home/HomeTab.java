package main.Home;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class HomeTab extends JPanel {

    private GameStartCallback gameStartCallback;

    public interface GameStartCallback {
        void onGameStart();
    }

    public HomeTab() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Chào mừng bạn đến với Word Arrange!");
        title.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        title.setForeground(new Color(33, 33, 33));

        JButton startGameBtn = createGameButton("Bắt đầu chơi", new Color(120, 60, 160)); // Tím nhạt

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(title, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(startGameBtn);

        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createGameButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SF Pro Display", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameStartCallback != null) {
                    gameStartCallback.onGameStart();
                }
            }
        });

        return button;
    }

    public void setStartGameCallback(GameStartCallback callback) {
        this.gameStartCallback = callback;
    }
}

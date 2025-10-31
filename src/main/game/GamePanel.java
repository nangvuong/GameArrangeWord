package main.game;

import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel {
    public GamePanel() {
        setPreferredSize(new Dimension(600, 400));
        setBackground(new Color(230, 255, 240));
        setLayout(new BorderLayout());

        JLabel lbl = new JLabel("Chào mừng bạn đến Word Arrange!", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(new Color(0, 100, 50));

        add(lbl, BorderLayout.CENTER);
    }
}

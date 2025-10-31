package main;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import main.auth.Login;
import main.game.GamePanel;

public class Main {
    public static void main(String[] args) {
        // Áp dụng giao diện FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tạo cửa sổ chính
        JFrame window = new JFrame("Word Arrange");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // Tạo panel Login và callback khi đăng nhập thành công
        Login loginPanel = new Login(() -> {
            window.setContentPane(new GamePanel());
            window.revalidate();
        });

        window.add(loginPanel);
        window.setSize(900, 600); // Kích thước cho layout 2 cột
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}

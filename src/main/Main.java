package main;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

import main.Home.Home;
import main.auth.Login;
import main.auth.Register;

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
        window.setSize(1000, 700); // Kích thước cho layout
        window.setLocationRelativeTo(null);

        // Tạo Login panel (cần để final để dùng trong lambda)
        final Login[] loginPanelRef = new Login[1];
        
        loginPanelRef[0] = new Login(() -> {
            Home homePanel = new Home();
            homePanel.setLogoutCallback(() -> {
                loginPanelRef[0].resetForm();
                window.setContentPane(loginPanelRef[0]);
                window.revalidate();
                window.repaint();
            });
            window.setContentPane(homePanel);
            window.revalidate();
            window.repaint();
        });

        // Kết nối nút "Tạo tài khoản" của Login với Register
        loginPanelRef[0].setRegisterCallback(() -> {
            Register registerPanel = new Register(() -> {
                Home homePanel = new Home();
                homePanel.setLogoutCallback(() -> {
                    loginPanelRef[0].resetForm();
                    window.setContentPane(loginPanelRef[0]);
                    window.revalidate();
                    window.repaint();
                });
                window.setContentPane(homePanel);
                window.revalidate();
                window.repaint();
            });

            registerPanel.setBackCallback(() -> {
                loginPanelRef[0].resetForm();
                window.setContentPane(loginPanelRef[0]);
                window.revalidate();
                window.repaint();
            });

            window.setContentPane(registerPanel);
            window.revalidate();
            window.repaint();
        });

        window.add(loginPanelRef[0]);
        window.setVisible(true);
    }
}

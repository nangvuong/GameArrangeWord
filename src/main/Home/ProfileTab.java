package main.Home;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProfileTab extends JPanel {

    public ProfileTab() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Thông tin cá nhân");
        title.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        title.setForeground(new Color(33, 33, 33));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        infoPanel.add(createInfoRow("Tên đăng nhập:", "user_demo"));
        infoPanel.add(createInfoRow("Họ và tên:", "Nguyễn Văn A"));
        infoPanel.add(createInfoRow("Điểm cao nhất:", "1250"));
        infoPanel.add(createInfoRow("Tổng trò chơi:", "42"));
        infoPanel.add(createInfoRow("Tỷ lệ thắng:", "78.6%"));

        JButton editBtn = createGameButton("Chỉnh sửa thông tin", new Color(66, 133, 244));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(title, BorderLayout.NORTH);
        centerPanel.add(infoPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(editBtn);

        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        row.setOpaque(false);

        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(new Font("SF Pro Display", Font.BOLD, 14));
        labelLbl.setForeground(new Color(70, 70, 70));
        labelLbl.setPreferredSize(new Dimension(150, 30));

        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("SF Pro Display", Font.PLAIN, 14));
        valueLbl.setForeground(new Color(33, 33, 33));

        row.add(labelLbl);
        row.add(valueLbl);

        return row;
    }

    private JButton createGameButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SF Pro Display", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(150, 40));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }
}

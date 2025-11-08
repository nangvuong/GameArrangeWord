package main.Home;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Player;

public class ProfileTab extends JPanel {

    public ProfileTab() {
        this(null);
    }

    public ProfileTab(Player player) {
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

        if (player != null) {
            infoPanel.add(createInfoRow("Tên đăng nhập:", player.getUsername()));
            infoPanel.add(createInfoRow("Họ và tên:", player.getFullName()));
            infoPanel.add(createInfoRow("Tổng trò chơi:", String.valueOf(player.getTotalMatches())));
            infoPanel.add(createInfoRow("Tổng thắng:", String.valueOf(player.getTotalWins())));
            infoPanel.add(createInfoRow("Tỷ lệ thắng:", player.getTotalMatches() > 0 
                ? String.format("%.1f%%", (player.getTotalWins() * 100.0 / player.getTotalMatches())) 
                : "0%"));
            infoPanel.add(createInfoRow("Tổng điểm:", String.valueOf(player.getRating()) + " điểm"));
        } else {
            infoPanel.add(createInfoRow("Tên đăng nhập:", "user_demo"));
            infoPanel.add(createInfoRow("Họ và tên:", "Nguyễn Văn A"));
            infoPanel.add(createInfoRow("Điểm cao nhất:", "1250"));
            infoPanel.add(createInfoRow("Tổng trò chơi:", "42"));
            infoPanel.add(createInfoRow("Tỷ lệ thắng:", "78.6%"));
        }

        JButton editBtn = createGameButton("Chỉnh sửa thông tin", new Color(66, 133, 244));
        editBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (player != null) {
                    showEditDialog(player);
                }
            }
        });

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

    private void showEditDialog(Player player) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Chỉnh sửa thông tin");
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // Full name field
        JPanel fullNamePanel = new JPanel(new BorderLayout());
        fullNamePanel.setOpaque(false);
        fullNamePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        JLabel fullNameLabel = new JLabel("Họ và tên:");
        fullNameLabel.setPreferredSize(new Dimension(100, 30));
        JTextField fullNameField = new JTextField(player.getFullName());
        fullNamePanel.add(fullNameLabel, BorderLayout.WEST);
        fullNamePanel.add(fullNameField, BorderLayout.CENTER);
        
        contentPanel.add(fullNamePanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Password field
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        passwordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setPreferredSize(new Dimension(100, 30));
        JPasswordField passwordField = new JPasswordField(player.getPassword());
        passwordPanel.add(passwordLabel, BorderLayout.WEST);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        
        contentPanel.add(passwordPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton saveBtn = new JButton("Lưu");
        saveBtn.setPreferredSize(new Dimension(100, 35));
        saveBtn.setFont(new Font("SF Pro Display", Font.BOLD, 12));
        saveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                player.setFullName(fullNameField.getText());
                player.setPassword(new String(passwordField.getPassword()));
                dialog.dispose();
                // Refresh UI
                revalidate();
                repaint();
            }
        });

        JButton cancelBtn = new JButton("Hủy");
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        cancelBtn.setFont(new Font("SF Pro Display", Font.BOLD, 12));
        cancelBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(buttonPanel);

        dialog.add(contentPanel);
        dialog.setVisible(true);
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

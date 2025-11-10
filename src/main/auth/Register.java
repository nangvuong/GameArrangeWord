package main.auth;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Player;

public class Register extends JPanel {

    private Player newPlayer;

    public interface RegisterCallback {
        void onRegisterSuccess();
    }

    public interface BackCallback {
        void onBackClick();
    }

    private JTextField fullNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JLabel messageLabel;
    private RegisterCallback registerCallback;
    private BackCallback backCallback;

    public Register(RegisterCallback registerCallback) {
        this.registerCallback = registerCallback;

        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 255));
        setPreferredSize(new Dimension(900, 600));

        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(120, 60, 160));
        leftPanel.setPreferredSize(new Dimension(450, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JPanel spacerTop = new JPanel();
        spacerTop.setOpaque(false);
        gbc.gridy = 0;
        gbc.weighty = 0.4;
        leftPanel.add(spacerTop, gbc);
        gbc.weighty = 0;

        JLabel gameName = new JLabel("Word Arrange");
        gameName.setFont(new Font("SF Pro Display", Font.BOLD, 48));
        gameName.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        leftPanel.add(gameName, gbc);

        JLabel slogan = new JLabel("<html><center>Sắp xếp từ,<br>thử thách trí tuệ</center></html>");
        slogan.setFont(new Font("SF Pro Display", Font.PLAIN, 16));
        slogan.setForeground(new Color(200, 220, 255));
        slogan.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 20, 0, 20);
        leftPanel.add(slogan, gbc);

        JPanel spacerBottom = new JPanel();
        spacerBottom.setOpaque(false);
        gbc.gridy = 4;
        gbc.weighty = 0.6;
        leftPanel.add(spacerBottom, gbc);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(450, 600));
        rightPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 12, 0);

        JLabel formTitle = new JLabel("Tạo tài khoản");
        formTitle.setFont(new Font("SF Pro Display", Font.BOLD, 24));
        formTitle.setForeground(new Color(33, 33, 33));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0);
        rightPanel.add(formTitle, gbc);

        JLabel fullNameLabel = new JLabel("Họ và tên");
        fullNameLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        fullNameLabel.setForeground(new Color(70, 70, 70));
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 0, 6, 0);
        rightPanel.add(fullNameLabel, gbc);

        fullNameField = createStyledTextField();
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 12, 0);
        rightPanel.add(fullNameField, gbc);

        JLabel userLabel = new JLabel("👤 Tên đăng nhập");
        userLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        userLabel.setForeground(new Color(70, 70, 70));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 6, 0);
        rightPanel.add(userLabel, gbc);

        usernameField = createStyledTextField();
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 12, 0);
        rightPanel.add(usernameField, gbc);

        JLabel passLabel = new JLabel("🔒 Mật khẩu");
        passLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        passLabel.setForeground(new Color(70, 70, 70));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 6, 0);
        rightPanel.add(passLabel, gbc);

        passwordField = createStyledPasswordField();
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 12, 0);
        rightPanel.add(passwordField, gbc);

        JLabel confirmPassLabel = new JLabel("🔒 Xác nhận mật khẩu");
        confirmPassLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        confirmPassLabel.setForeground(new Color(70, 70, 70));
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 6, 0);
        rightPanel.add(confirmPassLabel, gbc);

        confirmPasswordField = createStyledPasswordField();
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 18, 0);
        rightPanel.add(confirmPasswordField, gbc);

        JButton registerButton = createStyledButton("Tạo tài khoản", new Color(120, 60, 160));
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(100, 40, 140));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(new Color(120, 60, 160));
            }
        });
        registerButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleRegister();
            }
        });
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 10, 0);
        rightPanel.add(registerButton, gbc);

        JButton backButton = new JButton("Quay lại") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(120, 60, 160));
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

                super.paintComponent(g);
            }
        };
        backButton.setFont(new Font("SF Pro Display", Font.BOLD, 14));
        backButton.setForeground(new Color(120, 60, 160));
        backButton.setBorder(new EmptyBorder(11, 20, 11, 20));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setPreferredSize(new Dimension(350, 42));

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(new Color(240, 240, 240));
                backButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(Color.WHITE);
                backButton.repaint();
            }
        });
        backButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (backCallback != null) {
                    backCallback.onBackClick();
                }
            }
        });
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, 0, 0);
        rightPanel.add(backButton, gbc);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 12));
        messageLabel.setForeground(new Color(244, 67, 54));
        gbc.gridy = 11;
        gbc.insets = new Insets(10, 0, 0, 0);
        rightPanel.add(messageLabel, gbc);

        JPanel spacerBottom = new JPanel();
        spacerBottom.setOpaque(false);
        gbc.gridy = 12;
        gbc.weighty = 1.0;
        rightPanel.add(spacerBottom, gbc);

        return rightPanel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SF Pro Display", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(new EmptyBorder(11, 20, 11, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(350, 42));
        return button;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("SF Pro Display", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(10, 14, 10, 14)));
        field.setPreferredSize(new Dimension(280, 38));
        field.setBackground(new Color(250, 250, 250));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("SF Pro Display", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(10, 14, 10, 14)));
        field.setPreferredSize(new Dimension(280, 38));
        field.setBackground(new Color(250, 250, 250));
        return field;
    }

    // ===== SỬA HÀM NÀY =====
    private void handleRegister() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("⚠️ Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("⚠️ Mật khẩu không khớp!");
            return;
        }

        if (password.length() < 6) {
            messageLabel.setText("⚠️ Mật khẩu phải tối thiểu 6 ký tự!");
            return;
        }

        messageLabel.setText("🔄 Đang tạo tài khoản...");
        messageLabel.setForeground(new Color(33, 150, 243));

        // Disable buttons
        Component[] components = getParent().getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                comp.setEnabled(false);
            }
        }

        // Chạy register trong background thread
        new Thread(() -> {
            try {
                // Gọi đúng method registerAccount từ PlayerAuth (trả về boolean)
                boolean success = network.PlayerAuth.registerAccount(username, password, fullName);

                SwingUtilities.invokeLater(() -> {
                    if (success) {
                        // Tạo Player object local sau khi đăng ký thành công
                        newPlayer = new Player(fullName, username, password);
                        newPlayer.setRating(0);

                        messageLabel.setText("✅ Đăng ký thành công!");
                        messageLabel.setForeground(new Color(76, 175, 80));
                        if (registerCallback != null) {
                            registerCallback.onRegisterSuccess();
                        }
                    } else {
                        messageLabel.setText("❌ Đăng ký thất bại! Tên đăng nhập có thể đã tồn tại.");
                        messageLabel.setForeground(new Color(244, 67, 54));
                        enableButtons();
                    }
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    messageLabel.setText("❌ " + e.getMessage());
                    messageLabel.setForeground(new Color(244, 67, 54));
                    enableButtons();
                });
            }
        }).start();
    }

    private void enableButtons() {
        Component[] components = getParent().getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                comp.setEnabled(true);
            }
        }
    }

    public void setBackCallback(BackCallback callback) {
        this.backCallback = callback;
    }

    public Player getNewPlayer() {
        return newPlayer;
    }
}
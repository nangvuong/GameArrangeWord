package main.auth;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Login extends JPanel {

    public interface LoginCallback {
        void onLoginSuccess(String username);
    }
    
    public interface RegisterCallback {
        void onRegisterClick();
    }

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private LoginCallback loginCallback;
    private RegisterCallback registerCallback;

    public Login(LoginCallback loginCallback) {
        this.loginCallback = loginCallback;
        
        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 255));
        setPreferredSize(new Dimension(900, 600)); // Kích thước cho 2 cột

        // Panel bên trái - Logo + Slogan
        JPanel leftPanel = createLeftPanel();
        
        // Panel bên phải - Form đăng nhập
        JPanel rightPanel = createRightPanel();

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(66, 133, 244));
        leftPanel.setPreferredSize(new Dimension(450, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Spacer trên
        JPanel spacerTop = new JPanel();
        spacerTop.setOpaque(false);
        gbc.gridy = 0;
        gbc.weighty = 0.4;
        leftPanel.add(spacerTop, gbc);
        gbc.weighty = 0;

        // Tên game
        JLabel gameName = new JLabel("Word Arrange");
        gameName.setFont(new Font("SF Pro Display", Font.BOLD, 48));
        gameName.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        leftPanel.add(gameName, gbc);

        // Slogan
        JLabel slogan = new JLabel("<html><center>Sắp xếp từ,<br>thử thách trí tuệ</center></html>");
        slogan.setFont(new Font("SF Pro Display", Font.PLAIN, 16));
        slogan.setForeground(new Color(200, 220, 255));
        slogan.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 20, 0, 20);
        leftPanel.add(slogan, gbc);

        // Spacer dưới
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
        rightPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Spacer trên
        JPanel spacerTop = new JPanel();
        spacerTop.setOpaque(false);
        gbc.gridy = 0;
        gbc.weighty = 0.5;
        rightPanel.add(spacerTop, gbc);
        gbc.weighty = 0;

        // Username label
        JLabel userLabel = new JLabel("👤 Tên đăng nhập");
        userLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        userLabel.setForeground(new Color(70, 70, 70));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 6, 0);
        rightPanel.add(userLabel, gbc);

        // Username field
        usernameField = createStyledTextField();
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 16, 0);
        rightPanel.add(usernameField, gbc);

        // Password label
        JLabel passLabel = new JLabel("🔒 Mật khẩu");
        passLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        passLabel.setForeground(new Color(70, 70, 70));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 6, 0);
        rightPanel.add(passLabel, gbc);

        // Password field
        passwordField = createStyledPasswordField();
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 20, 0);
        rightPanel.add(passwordField, gbc);

        // Nút Đăng nhập
        JButton loginButton = createStyledButton("Đăng Nhập", new Color(66, 133, 244));
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(52, 115, 214));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(66, 133, 244));
            }
        });
        loginButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleLogin();
            }
        });
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 12, 0);
        rightPanel.add(loginButton, gbc);

        // Nút Tạo tài khoản
        JButton registerButton = createStyledButton("Tạo tài khoản", new Color(100, 100, 100));
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(80, 80, 80));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(new Color(100, 100, 100));
            }
        });
        registerButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (registerCallback != null) {
                    registerCallback.onRegisterClick();
                }
            }
        });
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 0, 0);
        rightPanel.add(registerButton, gbc);

        // Message Label
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 12));
        messageLabel.setForeground(new Color(244, 67, 54));
        gbc.gridy = 7;
        gbc.insets = new Insets(15, 0, 0, 0);
        rightPanel.add(messageLabel, gbc);

        // Spacer dưới
        JPanel spacerBottom = new JPanel();
        spacerBottom.setOpaque(false);
        gbc.gridy = 8;
        gbc.weighty = 0.5;
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
                new EmptyBorder(10, 14, 10, 14)
        ));
        field.setPreferredSize(new Dimension(280, 38));
        field.setBackground(new Color(250, 250, 250));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("SF Pro Display", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(10, 14, 10, 14)
        ));
        field.setPreferredSize(new Dimension(280, 38));
        field.setBackground(new Color(250, 250, 250));
        return field;
    }

    private void handleLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            messageLabel.setText("⚠️ Vui lòng nhập đầy đủ thông tin!");
        } else {
            messageLabel.setText("");
            loginCallback.onLoginSuccess(user);
            setVisible(false);
        }
    }

    public void setRegisterCallback(RegisterCallback callback) {
        this.registerCallback = callback;
    }

    public void resetForm() {
        usernameField.setText("");
        passwordField.setText("");
        messageLabel.setText("");
        setVisible(true);
    }
}

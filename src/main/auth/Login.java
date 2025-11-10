package main.auth;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Player;

public class Login extends JPanel {

    private Player player;

    public interface LoginCallback {
        void onLoginSuccess(Player player);
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
        rightPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JPanel spacerTop = new JPanel();
        spacerTop.setOpaque(false);
        gbc.gridy = 0;
        gbc.weighty = 0.5;
        rightPanel.add(spacerTop, gbc);
        gbc.weighty = 0;

        JLabel userLabel = new JLabel("👤 Tên đăng nhập");
        userLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        userLabel.setForeground(new Color(70, 70, 70));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 6, 0);
        rightPanel.add(userLabel, gbc);

        usernameField = createStyledTextField();
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 16, 0);
        rightPanel.add(usernameField, gbc);

        JLabel passLabel = new JLabel("🔒 Mật khẩu");
        passLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        passLabel.setForeground(new Color(70, 70, 70));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 6, 0);
        rightPanel.add(passLabel, gbc);

        passwordField = createStyledPasswordField();
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 20, 0);
        rightPanel.add(passwordField, gbc);

        JButton loginButton = createStyledButton("Đăng Nhập", new Color(120, 60, 160));
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(100, 40, 140));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(120, 60, 160));
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

        JButton registerButton = new JButton("Tạo tài khoản") {
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
        registerButton.setFont(new Font("SF Pro Display", Font.BOLD, 14));
        registerButton.setForeground(new Color(120, 60, 160));
        registerButton.setBorder(new EmptyBorder(11, 20, 11, 20));
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setOpaque(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setPreferredSize(new Dimension(350, 42));
        
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(240, 240, 240));
                registerButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(Color.WHITE);
                registerButton.repaint();
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

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 12));
        messageLabel.setForeground(new Color(244, 67, 54));
        gbc.gridy = 7;
        gbc.insets = new Insets(15, 0, 0, 0);
        rightPanel.add(messageLabel, gbc);

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

    // ===== SỬA HÀM NÀY ===== 
    private void handleLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            messageLabel.setText("⚠️ Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        messageLabel.setText("🔄 Đang đăng nhập...");
        messageLabel.setForeground(new Color(33, 150, 243));
        
        // Disable button để tránh spam
        Component[] components = getParent().getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                comp.setEnabled(false);
            }
        }
        
        // Chạy login trong background thread
        new Thread(() -> {
            try {
                player = network.PlayerAuth.checkLogin(user, pass);
                
                // Chuyển về UI thread để update UI
                SwingUtilities.invokeLater(() -> {
                    if (player != null && loginCallback != null) {
                        messageLabel.setText("✅ Đăng nhập thành công!");
                        messageLabel.setForeground(new Color(76, 175, 80));
                        loginCallback.onLoginSuccess(player);
                    } else {
                        messageLabel.setText("❌ Tên đăng nhập hoặc mật khẩu không đúng!");
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

    public void setRegisterCallback(RegisterCallback callback) {
        this.registerCallback = callback;
    }

    public void resetForm() {
        usernameField.setText("");
        passwordField.setText("");
        messageLabel.setText("");
        messageLabel.setForeground(new Color(244, 67, 54));
        setVisible(true);
    }

    public Player getPlayer() {
        return player;
    }
}
package main.game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class GameStart extends JPanel {
    private Runnable onGameStart;

    public GameStart(Runnable onGameStart) {
        this.onGameStart = onGameStart;
        
        setPreferredSize(new Dimension(1000, 700));
        setBackground(new Color(75, 0, 130)); // Màu tím đậm
        setLayout(null);

        initializeComponents();
    }

    private void initializeComponents() {
        // Logo / Title
        JLabel lblTitle = new JLabel("Word Arrange", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 80));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 80, 1000, 120);
        add(lblTitle);

        // Nút Cách chơi
        JButton btnHowToPlay = createButton("❓ Cách chơi", new Color(100, 149, 237), 350, 250);
        btnHowToPlay.addActionListener(e -> showHowToPlay());
        add(btnHowToPlay);

        // Nút Play (vòng tròn)
        JButton btnPlay = createButton("▶", new Color(255, 165, 0), 350, 380);
        btnPlay.setFont(new Font("Segoe UI", Font.BOLD, 32));
        btnPlay.addActionListener(e -> {
            if (onGameStart != null) {
                onGameStart.run();
            }
        });
        add(btnPlay);
    }

    private JButton createButton(String text, Color bgColor, int x, int y) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color drawColor = bgColor;
                if (getModel().isArmed()) {
                    int r = Math.max(bgColor.getRed() - 20, 0);
                    int g_val = Math.max(bgColor.getGreen() - 20, 0);
                    int b = Math.max(bgColor.getBlue() - 20, 0);
                    drawColor = new Color(r, g_val, b);
                } else if (getModel().isRollover()) {
                    int r = Math.min(bgColor.getRed() + 30, 255);
                    int g_val = Math.min(bgColor.getGreen() + 30, 255);
                    int b = Math.min(bgColor.getBlue() + 30, 255);
                    drawColor = new Color(r, g_val, b);
                }
                
                g2.setColor(drawColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(180, 180, 230));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                super.paintComponent(g);
            }
        };
        
        btn.setBounds(x, y, 300, 80);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.repaint();
            }
        });
        
        return btn;
    }

    private void showHowToPlay() {
        String message = "<html><body style='font-family: Segoe UI; font-size: 14px; padding: 10px;'>" +
                "<h2 style='color: #4B0082; text-align: center;'>📖 Hướng dẫn chơi</h2>" +
                "<hr>" +
                "<p><b>1. 🔤 Ký tự bị xáo trộn</b><br>" +
                "   Bạn sẽ được cấp các ký tự bị xáo trộn</p>" +
                "<p><b>2. 🎯 Sắp xếp đúng</b><br>" +
                "   Sắp xếp lại các ký tự để tạo thành từ đúng</p>" +
                "<p><b>3. ⏱️ Thời gian</b><br>" +
                "   Mỗi round có 15 giây</p>" +
                "<p><b>4. 🔁 Tổng số round</b><br>" +
                "   Tổng cộng 10 round</p>" +
                "<p><b>5. ⭐ Tính điểm</b><br>" +
                "   Mỗi ký tự đúng + 10 điểm</p>" +
                "<p><b>6. 🏆 Người thắng</b><br>" +
                "   Người chơi có điểm cao hơn sẽ thắng!</p>" +
                "</body></html>";
        
        JOptionPane.showMessageDialog(this, message, "Cách chơi", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setGameStartCallback(Runnable callback) {
        this.onGameStart = callback;
    }
}

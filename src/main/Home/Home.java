package main.Home;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Home extends JPanel {

    private JPanel tabContentPanel;
    private TabButton homeTabBtn;
    private TabButton profileTabBtn;
    private TabButton historyTabBtn;
    private TabButton rankTabBtn;
    private LogoutCallback logoutCallback;

    public interface LogoutCallback {
        void onLogout();
    }

    public Home() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));

        JPanel headerPanel = createHeaderPanel();
        tabContentPanel = new JPanel(new CardLayout());
        tabContentPanel.setBackground(Color.WHITE);
        tabContentPanel.add(new HomeTab(), "HOME");
        tabContentPanel.add(new ProfileTab(), "PROFILE");
        tabContentPanel.add(new HistoryTab(), "HISTORY");
        tabContentPanel.add(new RankTab(), "RANK");

        add(headerPanel, BorderLayout.NORTH);
        add(tabContentPanel, BorderLayout.CENTER);
        showTab("HOME");
    }

    private JPanel createHeaderPanel() {
        JPanel mainHeaderPanel = new JPanel(new BorderLayout());
        mainHeaderPanel.setBackground(new Color(66, 133, 244));
        mainHeaderPanel.setPreferredSize(new Dimension(0, 70));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(66, 133, 244));
        headerPanel.setLayout(new GridBagLayout());
        headerPanel.setBorder(new EmptyBorder(12, 25, 12, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 20);

        // Title
        JLabel title = new JLabel("Word Arrange");
        title.setFont(new Font("SF Pro Display", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.weightx = 0;
        headerPanel.add(title, gbc);

        // Tabs
        JPanel tabsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        tabsPanel.setOpaque(false);

        homeTabBtn = createTabButton("🏠 Home");
        profileTabBtn = createTabButton("👤 Profile");
        historyTabBtn = createTabButton("📊 History");
        rankTabBtn = createTabButton("🏆 Rank");

        homeTabBtn.addActionListener(e -> { showTab("HOME"); updateTabButtons("HOME"); });
        profileTabBtn.addActionListener(e -> { showTab("PROFILE"); updateTabButtons("PROFILE"); });
        historyTabBtn.addActionListener(e -> { showTab("HISTORY"); updateTabButtons("HISTORY"); });
        rankTabBtn.addActionListener(e -> { showTab("RANK"); updateTabButtons("RANK"); });

        tabsPanel.add(homeTabBtn);
        tabsPanel.add(profileTabBtn);
        tabsPanel.add(historyTabBtn);
        tabsPanel.add(rankTabBtn);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        headerPanel.add(tabsPanel, gbc);

        // Logout button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        JButton logoutBtn = createLogoutButton("🚪 Logout");
        logoutBtn.addActionListener(e -> { if (logoutCallback != null) logoutCallback.onLogout(); });
        rightPanel.add(logoutBtn);

        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 20, 0, 0);
        headerPanel.add(rightPanel, gbc);

        // Separator line
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(33, 100, 200));
        separator.setPreferredSize(new Dimension(0, 2));

        mainHeaderPanel.add(headerPanel, BorderLayout.CENTER);
        mainHeaderPanel.add(separator, BorderLayout.SOUTH);
        return mainHeaderPanel;
    }

    private TabButton createTabButton(String text) {
        TabButton btn = new TabButton(text);
        btn.setFont(new Font("SF Pro Display", Font.PLAIN, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(66, 133, 244));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(new Color(200, 220, 255)); }
            public void mouseExited(MouseEvent e) { btn.setForeground(Color.WHITE); }
        });
        return btn;
    }

    private JButton createLogoutButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SF Pro Display", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(200, 16, 46));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(180, 10, 36)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(200, 16, 46)); }
        });
        return btn;
    }

    private void updateTabButtons(String activeTab) {
        homeTabBtn.setActive(activeTab.equals("HOME"));
        profileTabBtn.setActive(activeTab.equals("PROFILE"));
        historyTabBtn.setActive(activeTab.equals("HISTORY"));
        rankTabBtn.setActive(activeTab.equals("RANK"));
    }

    private void showTab(String tabName) {
        CardLayout cl = (CardLayout) (tabContentPanel.getLayout());
        cl.show(tabContentPanel, tabName);
    }

    public void setLogoutCallback(LogoutCallback callback) {
        this.logoutCallback = callback;
    }

    // Custom TabButton class with underline for active state
    private static class TabButton extends JButton {
        private boolean isActive = false;

        public TabButton(String text) {
            super(text);
        }

        public void setActive(boolean active) {
            this.isActive = active;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (isActive) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);
            }
        }
    }
}

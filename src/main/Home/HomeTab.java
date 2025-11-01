package main.Home;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class HomeTab extends JPanel {

    private DefaultListModel<PlayerInfo> playerListModel;
    private DefaultListModel<ChallengeInfo> challengeListModel;

    private static class PlayerInfo {
        String name;
        String status; // "online", "offline", "playing"

        PlayerInfo(String name, String status) {
            this.name = name;
            this.status = status;
        }
    }

    private static class ChallengeInfo {
        String name;

        ChallengeInfo(String name, String action) {
            this.name = name;
        }
    }

    public HomeTab() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(0, 0, 0, 0));

        // LEFT: Player list with search
        JPanel leftPanel = createLeftPanel();

        // CENTER: Divider
        JPanel centerDivider = new JPanel();
        centerDivider.setPreferredSize(new Dimension(1, 0));
        centerDivider.setBackground(new Color(220, 220, 220));

        // RIGHT: Challenge list
        JPanel rightPanel = createChallengeListPanel();

        add(leftPanel, BorderLayout.WEST);
        add(centerDivider, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(320, 0));
        leftPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        leftPanel.setBackground(new Color(255, 255, 255));

        // Title
        JLabel title = new JLabel("👥 Người chơi");
        title.setFont(new Font("SF Pro Display", Font.BOLD, 17));
        title.setForeground(new Color(30, 30, 30));
        leftPanel.add(title, BorderLayout.NORTH);

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.setBorder(new EmptyBorder(14, 0, 16, 0));

        JTextField searchField = new JTextField("🔍 Tìm kiếm...");
        searchField.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        searchField.setForeground(new Color(160, 160, 160));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                new EmptyBorder(9, 14, 9, 14)
        ));
        searchField.setPreferredSize(new Dimension(0, 38));
        searchField.setBackground(new Color(248, 248, 248));
        
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("🔍 Tìm kiếm...")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(30, 30, 30));
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("🔍 Tìm kiếm...");
                    searchField.setForeground(new Color(160, 160, 160));
                }
            }
        });
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        leftPanel.add(searchPanel, BorderLayout.NORTH);

        // Player list
        playerListModel = new DefaultListModel<>();
        addSamplePlayers();

        JList<PlayerInfo> playerList = new JList<>(playerListModel);
        playerList.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playerList.setBackground(new Color(250, 250, 250));
        playerList.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        playerList.setCellRenderer(new PlayerListCellRenderer());

        playerList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = playerList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    PlayerInfo selectedPlayer = playerListModel.getElementAt(index);
                    showChallengeForm(selectedPlayer.name);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(playerList);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        leftPanel.add(scrollPane, BorderLayout.CENTER);

        return leftPanel;
    }

    private void addSamplePlayers() {
        playerListModel.addElement(new PlayerInfo("Nguyễn Văn A", "online"));
        playerListModel.addElement(new PlayerInfo("Trần Thị B", "offline"));
        playerListModel.addElement(new PlayerInfo("Lê Văn C", "playing"));
        playerListModel.addElement(new PlayerInfo("Phạm Thị D", "online"));
        playerListModel.addElement(new PlayerInfo("Hoàng Văn E", "offline"));
        playerListModel.addElement(new PlayerInfo("Đinh Thị F", "playing"));
    }

    private JPanel createChallengeListPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(320, 0));
        rightPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        rightPanel.setBackground(new Color(255, 255, 255));

        JLabel title = new JLabel("⚔️ Thách đấu nhận được");
        title.setFont(new Font("SF Pro Display", Font.BOLD, 17));
        title.setForeground(new Color(30, 30, 30));
        rightPanel.add(title, BorderLayout.NORTH);

        // Challenge list
        challengeListModel = new DefaultListModel<>();
        addSampleChallenges();

        JList<ChallengeInfo> challengeList = new JList<>(challengeListModel);
        challengeList.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        challengeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        challengeList.setBackground(new Color(250, 250, 250));
        challengeList.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        challengeList.setCellRenderer(new ChallengeListCellRenderer());

        JScrollPane scrollPane = new JScrollPane(challengeList);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JPanel listWithMargin = new JPanel(new BorderLayout());
        listWithMargin.setOpaque(false);
        listWithMargin.setBorder(new EmptyBorder(14, 0, 0, 0));
        listWithMargin.add(scrollPane, BorderLayout.CENTER);

        rightPanel.add(listWithMargin, BorderLayout.CENTER);

        return rightPanel;
    }

    private void addSampleChallenges() {
        challengeListModel.addElement(new ChallengeInfo("Nguyễn Văn A", "thách đấu bạn"));
        challengeListModel.addElement(new ChallengeInfo("Lê Văn C", "thách đấu bạn"));
    }

    private void showChallengeForm(String playerName) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Gửi thách đấu");
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(320, 160);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        dialog.setResizable(false);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new GridBagLayout());
        dialogPanel.setBackground(Color.WHITE);
        dialogPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel messageLabel = new JLabel("<html>Bạn muốn gửi thách đấu đến<br><b>" + playerName + "</b>?</html>");
        messageLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(33, 33, 33));
        gbc.gridy = 0;
        gbc.weightx = 1;
        dialogPanel.add(messageLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton confirmBtn = new JButton("Gửi thách đấu");
        confirmBtn.setFont(new Font("SF Pro Display", Font.BOLD, 12));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setBackground(new Color(66, 133, 244));
        confirmBtn.setPreferredSize(new Dimension(120, 32));
        confirmBtn.setFocusPainted(false);
        confirmBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                confirmBtn.setBackground(new Color(52, 115, 214));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                confirmBtn.setBackground(new Color(66, 133, 244));
            }
        });
        confirmBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog, "Thách đấu đã được gửi!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        JButton cancelBtn = new JButton("Hủy");
        cancelBtn.setFont(new Font("SF Pro Display", Font.BOLD, 12));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(new Color(100, 100, 100));
        cancelBtn.setPreferredSize(new Dimension(80, 32));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                cancelBtn.setBackground(new Color(80, 80, 80));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cancelBtn.setBackground(new Color(100, 100, 100));
            }
        });
        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(confirmBtn);
        buttonPanel.add(cancelBtn);

        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        dialogPanel.add(buttonPanel, gbc);

        dialog.add(dialogPanel);
        dialog.setVisible(true);
    }

    private static class PlayerListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
            label.setOpaque(true);

            PlayerInfo player = (PlayerInfo) value;
            String statusEmoji = "🟢"; // online - xanh
            if ("offline".equals(player.status)) {
                statusEmoji = "🔴"; // offline - đỏ
            } else if ("playing".equals(player.status)) {
                statusEmoji = "🟡"; // playing - vàng
            }

            label.setText(statusEmoji + " " + player.name);

            if (isSelected) {
                label.setBackground(new Color(66, 133, 244));
                label.setForeground(Color.WHITE);
                label.setBorder(new EmptyBorder(11, 14, 11, 14));
            } else {
                label.setBackground(new Color(248, 248, 248));
                label.setForeground(new Color(40, 40, 40));
                label.setBorder(new EmptyBorder(11, 14, 11, 14));
            }

            return label;
        }
    }

    private static class ChallengeListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setOpaque(true);
            itemPanel.setBorder(new EmptyBorder(10, 14, 10, 12));

            ChallengeInfo challenge = (ChallengeInfo) value;

            JLabel nameLabel = new JLabel(challenge.name);
            nameLabel.setFont(new Font("SF Pro Display", Font.BOLD, 13));
            itemPanel.add(nameLabel, BorderLayout.WEST);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
            buttonPanel.setOpaque(false);

            JButton acceptBtn = new JButton("✓");
            acceptBtn.setFont(new Font("SF Pro Display", Font.BOLD, 12));
            acceptBtn.setForeground(Color.WHITE);
            acceptBtn.setBackground(new Color(76, 175, 80));
            acceptBtn.setPreferredSize(new Dimension(28, 28));
            acceptBtn.setFocusPainted(false);
            acceptBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            acceptBtn.setBorder(BorderFactory.createEmptyBorder());
            acceptBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    acceptBtn.setBackground(new Color(60, 150, 65));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    acceptBtn.setBackground(new Color(76, 175, 80));
                }
            });

            JButton declineBtn = new JButton("✕");
            declineBtn.setFont(new Font("SF Pro Display", Font.BOLD, 12));
            declineBtn.setForeground(Color.WHITE);
            declineBtn.setBackground(new Color(244, 67, 54));
            declineBtn.setPreferredSize(new Dimension(28, 28));
            declineBtn.setFocusPainted(false);
            declineBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            declineBtn.setBorder(BorderFactory.createEmptyBorder());
            declineBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    declineBtn.setBackground(new Color(204, 46, 35));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    declineBtn.setBackground(new Color(244, 67, 54));
                }
            });

            buttonPanel.add(acceptBtn);
            buttonPanel.add(declineBtn);

            itemPanel.add(buttonPanel, BorderLayout.EAST);

            if (isSelected) {
                itemPanel.setBackground(new Color(66, 133, 244));
                nameLabel.setForeground(Color.WHITE);
            } else {
                itemPanel.setBackground(new Color(248, 248, 248));
                nameLabel.setForeground(new Color(40, 40, 40));
            }

            return itemPanel;
        }
    }
}

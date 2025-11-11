package main.Home;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Player;
import java.util.ArrayList;
import java.util.List;

public class RankTab extends JPanel {

    public RankTab() {
        this(null, null);
    }

    public RankTab(List<Player> playerList, Player currentPlayer) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Bảng xếp hạng");
        title.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        title.setForeground(new Color(33, 33, 33));

        JPanel rankPanel = new JPanel();
        rankPanel.setLayout(new BoxLayout(rankPanel, BoxLayout.Y_AXIS));
        rankPanel.setOpaque(false);
        rankPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Hiển thị danh sách xếp hạng
        if (playerList != null && !playerList.isEmpty()) {
            // Sắp xếp theo rating giảm dần
            List<Player> sortedPlayers = new ArrayList<>(playerList);
            sortedPlayers.sort((p1, p2) -> Integer.compare(p2.getRating(), p1.getRating()));
            
            for (int i = 0; i < sortedPlayers.size(); i++) {
                Player player = sortedPlayers.get(i);
                Color rankColor = getRankColor(i + 1);
                boolean isCurrentPlayer = currentPlayer != null && currentPlayer.getId() == player.getId();
                String nameDisplay = player.getUsername() + (isCurrentPlayer ? " (Bạn)" : "");
                
                rankPanel.add(createRankItem(String.valueOf(i + 1), nameDisplay, 
                    player.getRating() + " điểm", rankColor, isCurrentPlayer));
                
                if (i < sortedPlayers.size() - 1) {
                    rankPanel.add(Box.createVerticalStrut(10));
                }
            }
        } else {
            // Demo data nếu không có player list
            rankPanel.add(createRankItem("1", "Lê Minh Huy", "2850 điểm", new Color(255, 215, 0), false));
            rankPanel.add(Box.createVerticalStrut(10));
            rankPanel.add(createRankItem("2", "Trần Thị Linh", "2720 điểm", new Color(192, 192, 192), false));
            rankPanel.add(Box.createVerticalStrut(10));
            rankPanel.add(createRankItem("3", "Phạm Văn Dũng", "2650 điểm", new Color(205, 127, 50), false));
            rankPanel.add(Box.createVerticalStrut(10));
            rankPanel.add(createRankItem("4", "Nguyễn Văn A (Bạn)", "1250 điểm", new Color(66, 133, 244), true));
            rankPanel.add(Box.createVerticalStrut(10));
            rankPanel.add(createRankItem("5", "Võ Thanh Tùng", "1100 điểm", new Color(128, 128, 128), false));
        }

        JScrollPane scrollPane = new JScrollPane(rankPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private Color getRankColor(int rank) {
        switch (rank) {
            case 1:
                return new Color(255, 215, 0); // Gold
            case 2:
                return new Color(192, 192, 192); // Silver
            case 3:
                return new Color(205, 127, 50); // Bronze
            default:
                return new Color(128, 128, 128); // Gray
        }
    }

    private JPanel createRankItem(String rank, String name, String score, Color rankColor, boolean isCurrentPlayer) {
        JPanel item = new JPanel(new BorderLayout());
        item.setOpaque(false);
        
        Color bgColor = isCurrentPlayer ? new Color(230, 240, 255) : Color.WHITE;
        item.setBackground(bgColor);
        
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(12, 16, 12, 16)
        ));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel rankLabel = new JLabel("# " + rank);
        rankLabel.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        rankLabel.setForeground(rankColor);
        rankLabel.setPreferredSize(new Dimension(60, 50));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SF Pro Display", Font.BOLD, 14));
        nameLabel.setForeground(new Color(33, 33, 33));

        JLabel scoreLabel = new JLabel(score);
        scoreLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        scoreLabel.setForeground(new Color(128, 128, 128));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(nameLabel);
        centerPanel.add(scoreLabel);

        item.add(rankLabel, BorderLayout.WEST);
        item.add(centerPanel, BorderLayout.CENTER);

        return item;
    }

    public void updateRankList(List<Player> playerList, Player currentPlayer) {
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.add(new RankTab(playerList, currentPlayer), BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
}

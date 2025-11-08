package main.Home;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Player;

public class HistoryTab extends JPanel {

    public HistoryTab() {
        this(null);
    }

    public HistoryTab(Player player) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Lịch sử trò chơi");
        title.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        title.setForeground(new Color(33, 33, 33));

        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        historyPanel.setOpaque(false);
        historyPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Hiển thị lịch sử (demo data)
        // TODO: Kết nối với database để hiển thị lịch sử thực tế
        historyPanel.add(createHistoryItem("Ngày 01/11/2025", "950 điểm", "Thắng", new Color(76, 175, 80)));
        historyPanel.add(Box.createVerticalStrut(10));
        historyPanel.add(createHistoryItem("Ngày 31/10/2025", "1200 điểm", "Thắng", new Color(76, 175, 80)));
        historyPanel.add(Box.createVerticalStrut(10));
        historyPanel.add(createHistoryItem("Ngày 30/10/2025", "850 điểm", "Thua", new Color(244, 67, 54)));
        historyPanel.add(Box.createVerticalStrut(10));
        historyPanel.add(createHistoryItem("Ngày 29/10/2025", "1100 điểm", "Thắng", new Color(76, 175, 80)));

        JScrollPane scrollPane = new JScrollPane(historyPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHistoryItem(String date, String score, String result, Color resultColor) {
        JPanel item = new JPanel(new BorderLayout());
        item.setOpaque(false);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(12, 16, 12, 16)
        ));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        dateLabel.setForeground(new Color(128, 128, 128));

        JLabel scoreLabel = new JLabel(score);
        scoreLabel.setFont(new Font("SF Pro Display", Font.BOLD, 14));
        scoreLabel.setForeground(new Color(33, 33, 33));

        JLabel resultLabel = new JLabel(result);
        resultLabel.setFont(new Font("SF Pro Display", Font.BOLD, 13));
        resultLabel.setForeground(resultColor);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.add(dateLabel);
        leftPanel.add(scoreLabel);

        item.add(leftPanel, BorderLayout.WEST);
        item.add(resultLabel, BorderLayout.EAST);

        return item;
    }
}

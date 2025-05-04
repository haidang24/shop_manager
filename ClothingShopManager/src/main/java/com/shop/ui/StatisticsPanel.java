
package main.java.com.shop.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class StatisticsPanel extends JPanel {
    public StatisticsPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(250, 251, 252));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title panel with gradient background
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(25, 118, 210), w, h, new Color(66, 165, 245));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("THỐNG KÊ DOANH THU", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        titlePanel.add(title, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);

        // Stats panel with modern styling
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create styled stat cards
        statsPanel.add(createStatCard("📊", "Tổng số hóa đơn", "50", new Color(52, 152, 219)));
        statsPanel.add(createStatCard("💵", "Tổng doanh thu", "75,000,000 VND", new Color(46, 204, 113)));
        statsPanel.add(createStatCard("🔥", "Sản phẩm bán chạy", "Áo thun", new Color(230, 126, 34)));
        statsPanel.add(createStatCard("👥", "Khách hàng VIP", "Nguyễn Văn A", new Color(155, 89, 182)));

        // Add shadow border to stats panel
        JPanel shadowPanel = new JPanel(new BorderLayout());
        shadowPanel.setBorder(BorderFactory.createCompoundBorder(
                new ShadowBorder(),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        shadowPanel.add(statsPanel);
        shadowPanel.setOpaque(false);

        add(shadowPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String icon, String label, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new ShadowBorder(),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        iconLabel.setForeground(color);
        card.add(iconLabel, BorderLayout.WEST);

        // Label and value - Changed text colors to black
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.BLACK); // Changed to black

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(Color.BLACK); // Changed to black

        textPanel.add(titleLabel);
        textPanel.add(valueLabel);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private class ShadowBorder extends AbstractBorder {
        private int shadowSize = 3;

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color shadowColor = new Color(0, 0, 0, 30);
            g2.setColor(shadowColor);

            for (int i = 0; i < shadowSize; i++) {
                g2.setColor(new Color(shadowColor.getRed(),
                        shadowColor.getGreen(),
                        shadowColor.getBlue(),
                        shadowColor.getAlpha() / (i + 1)));
                g2.drawRoundRect(x + i, y + i, width - i * 2, height - i * 2, 10, 10);
            }

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = shadowSize;
            return insets;
        }
    }
}

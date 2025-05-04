package com.shopmanager.ui;

import com.shopmanager.ui.components.BasePanel;
import com.shopmanager.utils.DatabaseConnection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
// JFreeChart imports
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class HomePanel extends BasePanel {
    private JPanel statsPanel;
    private JPanel chartsPanel;
    private Map<String, Color> cardColors = new HashMap<>();
    private DecimalFormat currencyFormat = new DecimalFormat("#,###");

    public HomePanel() {
        super();
        // Set up the color scheme for stat cards
        cardColors.put("products", new Color(41, 128, 185)); // Blue
        cardColors.put("orders", new Color(46, 204, 113)); // Green
        cardColors.put("customers", new Color(155, 89, 182)); // Purple
        cardColors.put("revenue", new Color(230, 126, 34)); // Orange

        initComponents();
        loadStats();
    }

    private void initComponents() {
        // Create a modern layout with panels
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(25, 25, 25, 25));
        setBackground(new Color(245, 247, 250));

        // Header with welcome message
        JPanel headerPanel = createWelcomePanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main container with shadow effect
        JPanel mainContainer = new JPanel(new BorderLayout(20, 20));
        mainContainer.setBackground(new Color(245, 247, 250));

        // Stats Panel with modern cards
        statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(new Color(245, 247, 250));
        statsPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Create stat cards with enhanced visual
        statsPanel.add(createModernStatCard("Tổng Sản Phẩm", "0", cardColors.get("products"), "products"));
        statsPanel.add(createModernStatCard("Tổng Đơn Hàng", "0", cardColors.get("orders"), "orders"));
        statsPanel.add(createModernStatCard("Tổng Khách Hàng", "0", cardColors.get("customers"), "customers"));
        statsPanel.add(createModernStatCard("Doanh Thu", "0 VNĐ", cardColors.get("revenue"), "revenue"));

        // Charts Panel with two charts side by side
        chartsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        chartsPanel.setBackground(new Color(245, 247, 250));

        // Create placeholder for charts that will be populated in loadStats()
        JPanel orderChart = createOrderBarChart(new HashMap<>());
        JPanel categoryChart = createCategoryPieChart(new HashMap<>());

        // Add charts to panel
        chartsPanel.add(orderChart);
        chartsPanel.add(categoryChart);

        // Add components to main container
        mainContainer.add(statsPanel, BorderLayout.NORTH);
        mainContainer.add(chartsPanel, BorderLayout.CENTER);

        // Add some recent activity/analytics section at the bottom
        JPanel analyticsPanel = createAnalyticsPanel();
        mainContainer.add(analyticsPanel, BorderLayout.SOUTH);

        add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout(10, 10));
        welcomePanel.setBackground(new Color(245, 247, 250));
        welcomePanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Tổng Quan Cửa Hàng");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(52, 73, 94));

        JLabel subtitleLabel = new JLabel("Chào mừng bạn quay trở lại! Đây là tổng quan hoạt động của cửa hàng.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(127, 140, 141));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(new Color(245, 247, 250));
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(subtitleLabel, BorderLayout.CENTER);

        welcomePanel.add(textPanel, BorderLayout.WEST);

        // Date panel on the right
        JLabel dateLabel = new JLabel(
                new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy").format(new java.util.Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dateLabel.setForeground(new Color(52, 73, 94));
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        welcomePanel.add(dateLabel, BorderLayout.EAST);

        return welcomePanel;
    }

    private JPanel createModernStatCard(String title, String value, Color color, String type) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Set name for later identification
        card.setName(type);

        // Create a rounded panel for icon
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));

                // Draw custom icon instead of using emoji
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(2f));

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int iconSize = 20;

                if (type.equals("products")) {
                    // Draw a box/package icon
                    g2d.drawRect(centerX - iconSize / 2, centerY - iconSize / 2, iconSize, iconSize);
                    g2d.drawLine(centerX - iconSize / 2, centerY, centerX + iconSize / 2, centerY);
                    g2d.drawLine(centerX, centerY - iconSize / 2, centerX, centerY + iconSize / 2);
                } else if (type.equals("orders")) {
                    // Draw a document/clipboard icon
                    g2d.drawRect(centerX - iconSize / 2, centerY - iconSize / 2, iconSize, iconSize);
                    g2d.drawLine(centerX - iconSize / 3, centerY - iconSize / 4, centerX + iconSize / 3,
                            centerY - iconSize / 4);
                    g2d.drawLine(centerX - iconSize / 3, centerY, centerX + iconSize / 3, centerY);
                    g2d.drawLine(centerX - iconSize / 3, centerY + iconSize / 4, centerX + iconSize / 3,
                            centerY + iconSize / 4);
                } else if (type.equals("customers")) {
                    // Draw a person icon
                    g2d.drawOval(centerX - iconSize / 4, centerY - iconSize / 2, iconSize / 2, iconSize / 2); // Head
                    g2d.drawArc(centerX - iconSize / 2, centerY - iconSize / 4, iconSize, iconSize, 0, 180); // Body
                } else if (type.equals("revenue")) {
                    // Draw a money/coin icon
                    g2d.drawOval(centerX - iconSize / 2, centerY - iconSize / 2, iconSize, iconSize);
                    g2d.drawString("₫", centerX - 4, centerY + 5);
                }

                g2d.dispose();
            }
        };
        iconPanel.setPreferredSize(new Dimension(50, 50));
        iconPanel.setLayout(new BorderLayout());
        iconPanel.setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(127, 140, 141));

        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);

        // Layout components
        JPanel textPanel = new JPanel(new BorderLayout(5, 5));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(valueLabel, BorderLayout.CENTER);

        // Add trend indicator (for demonstration)
        JLabel trendLabel = new JLabel("↑ 5.2%");
        trendLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trendLabel.setForeground(new Color(46, 204, 113));
        textPanel.add(trendLabel, BorderLayout.SOUTH);

        // Add all to card
        card.add(iconPanel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createOrderBarChart(Map<String, Integer> orderData) {
        // Create chart panel with white background and padding
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(Color.WHITE);
        chartContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create chart title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Đơn Hàng Theo Tháng");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));

        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Create initial dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Add placeholder data if no real data yet
        if (orderData.isEmpty()) {
            // Placeholder data
            dataset.addValue(12, "Đơn hàng", "T1");
            dataset.addValue(18, "Đơn hàng", "T2");
            dataset.addValue(24, "Đơn hàng", "T3");
            dataset.addValue(16, "Đơn hàng", "T4");
            dataset.addValue(30, "Đơn hàng", "T5");
            dataset.addValue(28, "Đơn hàng", "T6");
        } else {
            // Real data will be added here later
            for (Map.Entry<String, Integer> entry : orderData.entrySet()) {
                dataset.addValue(entry.getValue(), "Đơn hàng", entry.getKey());
            }
        }

        // Create chart
        JFreeChart barChart = ChartFactory.createBarChart(
                "",
                "",
                "",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false);

        // Customize the chart
        barChart.setBackgroundPaint(Color.WHITE);

        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setRangeGridlinePaint(new Color(230, 230, 230));

        // Customize the bar renderer
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(41, 128, 185));
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);

        // Create chart panel
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(450, 300));
        chartPanel.setBackground(Color.WHITE);

        // Add to container
        chartContainer.add(titlePanel, BorderLayout.NORTH);
        chartContainer.add(chartPanel, BorderLayout.CENTER);

        return chartContainer;
    }

    private JPanel createCategoryPieChart(Map<String, Integer> categoryData) {
        // Create chart panel with white background and padding
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(Color.WHITE);
        chartContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create chart title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Phân Bố Sản Phẩm Theo Danh Mục");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));

        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Create dataset
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Add placeholder data if no real data yet
        if (categoryData.isEmpty()) {
            // Placeholder data
            dataset.setValue("Áo", 35);
            dataset.setValue("Quần", 25);
            dataset.setValue("Váy", 20);
            dataset.setValue("Phụ kiện", 15);
            dataset.setValue("Giày dép", 5);
        } else {
            // Real data will be added here later
            for (Map.Entry<String, Integer> entry : categoryData.entrySet()) {
                dataset.setValue(entry.getKey(), entry.getValue());
            }
        }

        // Create chart
        JFreeChart pieChart = ChartFactory.createPieChart(
                "",
                dataset,
                false,
                true,
                false);

        // Customize the chart
        pieChart.setBackgroundPaint(Color.WHITE);

        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setShadowPaint(null);
        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 180));

        // Set section colors
        plot.setSectionPaint("Áo", new Color(41, 128, 185));
        plot.setSectionPaint("Quần", new Color(46, 204, 113));
        plot.setSectionPaint("Váy", new Color(155, 89, 182));
        plot.setSectionPaint("Phụ kiện", new Color(230, 126, 34));
        plot.setSectionPaint("Giày dép", new Color(52, 152, 219));

        // Create chart panel
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setPreferredSize(new Dimension(450, 300));
        chartPanel.setBackground(Color.WHITE);

        // Add to container
        chartContainer.add(titlePanel, BorderLayout.NORTH);
        chartContainer.add(chartPanel, BorderLayout.CENTER);

        return chartContainer;
    }

    private JPanel createAnalyticsPanel() {
        JPanel analyticsContainer = new JPanel(new BorderLayout(15, 15));
        analyticsContainer.setBackground(new Color(245, 247, 250));
        analyticsContainer.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Panel to hold the quick info cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        cardsPanel.setBackground(new Color(245, 247, 250));

        // Recent orders info card
        JPanel recentOrdersCard = new JPanel(new BorderLayout(10, 10));
        recentOrdersCard.setBackground(Color.WHITE);
        recentOrdersCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel recentOrdersTitle = new JLabel("Đơn Hàng Gần Đây");
        recentOrdersTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        recentOrdersTitle.setForeground(new Color(52, 73, 94));

        // This would typically be a JList or similar, simplified for demo
        JPanel ordersList = new JPanel(new GridLayout(3, 1, 0, 10));
        ordersList.setBackground(Color.WHITE);

        ordersList.add(createInfoRow("Đơn hàng #1234", "Nguyễn Văn A", "1,250,000 VNĐ", new Color(46, 204, 113)));
        ordersList.add(createInfoRow("Đơn hàng #1233", "Trần Thị B", "850,000 VNĐ", new Color(46, 204, 113)));
        ordersList.add(createInfoRow("Đơn hàng #1232", "Lê Văn C", "2,100,000 VNĐ", new Color(46, 204, 113)));

        recentOrdersCard.add(recentOrdersTitle, BorderLayout.NORTH);
        recentOrdersCard.add(ordersList, BorderLayout.CENTER);

        // Low stock info card
        JPanel lowStockCard = new JPanel(new BorderLayout(10, 10));
        lowStockCard.setBackground(Color.WHITE);
        lowStockCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lowStockTitle = new JLabel("Sản Phẩm Sắp Hết Hàng");
        lowStockTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lowStockTitle.setForeground(new Color(52, 73, 94));

        // This would typically be a JList or similar, simplified for demo
        JPanel stockList = new JPanel(new GridLayout(3, 1, 0, 10));
        stockList.setBackground(Color.WHITE);

        stockList.add(createInfoRow("Áo thun nam", "Kho: 5", "Cần nhập thêm", new Color(231, 76, 60)));
        stockList.add(createInfoRow("Quần jean nữ", "Kho: 3", "Cần nhập thêm", new Color(231, 76, 60)));
        stockList.add(createInfoRow("Váy hoa", "Kho: 4", "Cần nhập thêm", new Color(231, 76, 60)));

        lowStockCard.add(lowStockTitle, BorderLayout.NORTH);
        lowStockCard.add(stockList, BorderLayout.CENTER);

        // Add cards to panel
        cardsPanel.add(recentOrdersCard);
        cardsPanel.add(lowStockCard);

        analyticsContainer.add(cardsPanel, BorderLayout.CENTER);

        return analyticsContainer;
    }

    private JPanel createInfoRow(String title, String subtitle, String info, Color statusColor) {
        JPanel rowPanel = new JPanel(new BorderLayout(15, 0));
        rowPanel.setBackground(Color.WHITE);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(52, 73, 94));

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(127, 140, 141));

        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        JLabel infoLabel = new JLabel(info);
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoLabel.setForeground(statusColor);
        infoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        rowPanel.add(textPanel, BorderLayout.WEST);
        rowPanel.add(infoLabel, BorderLayout.EAST);

        return rowPanel;
    }

    private void loadStats() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Load total products
            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM products")) {
                if (rs.next()) {
                    updateStatCard("products", String.valueOf(rs.getInt(1)));
                }
            }

            // Load total orders
            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM orders")) {
                if (rs.next()) {
                    updateStatCard("orders", String.valueOf(rs.getInt(1)));
                }
            }

            // Load total customers
            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM customers")) {
                if (rs.next()) {
                    updateStatCard("customers", String.valueOf(rs.getInt(1)));
                }
            }

            // Load total revenue
            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT SUM(total_amount) FROM orders")) {
                if (rs.next()) {
                    double revenue = rs.getDouble(1);
                    updateStatCard("revenue", String.format("%,.0f VNĐ", revenue));
                }
            }

            // Load order data by month (will be implemented further)

            // Load product distribution by category (will be implemented further)

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading statistics: " + e.getMessage());
        }
    }

    private void updateStatCard(String type, String value) {
        for (Component comp : statsPanel.getComponents()) {
            if (comp instanceof JPanel && comp.getName() != null && comp.getName().equals(type)) {
                JPanel card = (JPanel) comp;
                Component textPanelComp = ((BorderLayout) card.getLayout()).getLayoutComponent(BorderLayout.CENTER);
                if (textPanelComp instanceof JPanel) {
                    JPanel textPanel = (JPanel) textPanelComp;
                    Component valueLabelComp = ((BorderLayout) textPanel.getLayout())
                            .getLayoutComponent(BorderLayout.CENTER);
                    if (valueLabelComp instanceof JLabel) {
                        JLabel valueLabel = (JLabel) valueLabelComp;
                        valueLabel.setText(value);
                    }
                }
                break;
            }
        }
    }
}
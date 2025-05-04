package com.shopmanager.ui;

import com.shopmanager.utils.DatabaseConnection;
import com.shopmanager.ui.components.BasePanel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class StatisticsPanel extends BasePanel {
    private DecimalFormat currencyFormat = new DecimalFormat("#,###");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/yyyy");

    public StatisticsPanel() {
        super();
        initComponents();
    }

    private void initComponents() {
        // Create modern layout
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));

        // Header with title and description
        JPanel headerPanel = new JPanel(new BorderLayout(10, 5));
        headerPanel.setBackground(new Color(245, 247, 250));
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Thống Kê và Báo Cáo");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));

        JLabel subtitleLabel = new JLabel("Tổng quan báo cáo doanh thu, sản phẩm bán chạy và phân tích chi tiết");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(127, 140, 141));

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);

        // Date filter for top panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(new Color(245, 247, 250));

        String[] timeRanges = { "7 ngày qua", "30 ngày qua", "3 tháng qua", "6 tháng qua", "Năm nay", "Tất cả" };
        JComboBox<String> timeRangeComboBox = new JComboBox<>(timeRanges);
        timeRangeComboBox.setPreferredSize(new Dimension(150, 30));
        JButton refreshButton = createButton("Làm mới", primaryColor);

        filterPanel.add(new JLabel("Khoảng thời gian: "));
        filterPanel.add(timeRangeComboBox);
        filterPanel.add(refreshButton);

        headerPanel.add(filterPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Main content panel with charts
        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));

        // Revenue chart (top left)
        JPanel revenueChartPanel = createRevenueChart();

        // Category chart (top right)
        JPanel categoryChartPanel = createCategoryChart();

        // Products chart (bottom left)
        JPanel topProductsPanel = createTopProductsChart();

        // Daily stats chart (bottom right)
        JPanel dailyStatsPanel = createDailyStatsPanel();

        // Add all panels to main grid
        mainPanel.add(revenueChartPanel);
        mainPanel.add(categoryChartPanel);
        mainPanel.add(topProductsPanel);
        mainPanel.add(dailyStatsPanel);

        add(mainPanel, BorderLayout.CENTER);

        // Add action listener to refresh button
        refreshButton.addActionListener(e -> refreshCharts((String) timeRangeComboBox.getSelectedItem()));
    }

    private JPanel createRevenueChart() {
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(Color.WHITE);
        chartContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Doanh Thu Theo Tháng");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));

        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Create dataset with dummy data for now
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Last 6 months
        LocalDate now = LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            String monthName = month.format(dateFormatter);
            double revenue = Math.random() * 50000000 + 10000000;
            dataset.addValue(revenue, "Doanh thu", monthName);
        }

        // Create chart
        JFreeChart lineChart = ChartFactory.createLineChart(
                "",
                "Tháng",
                "Doanh Thu (VNĐ)",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false);

        // Customize the chart
        lineChart.setBackgroundPaint(Color.WHITE);

        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setRangeGridlinePaint(new Color(230, 230, 230));

        // Rotate category labels for better readability
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        // Customize line renderer
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(41, 128, 185));
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setDefaultShapesVisible(true);
        renderer.setSeriesShapesVisible(0, true);

        // Create chart panel
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setBackground(Color.WHITE);

        // Add components to container
        chartContainer.add(titlePanel, BorderLayout.NORTH);
        chartContainer.add(chartPanel, BorderLayout.CENTER);

        return chartContainer;
    }

    private JPanel createCategoryChart() {
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(Color.WHITE);
        chartContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Doanh Thu Theo Danh Mục");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));

        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Create dataset
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Add dummy data
        dataset.setValue("Áo", 35);
        dataset.setValue("Quần", 25);
        dataset.setValue("Váy", 20);
        dataset.setValue("Phụ kiện", 15);
        dataset.setValue("Giày dép", 5);

        // Create chart
        JFreeChart pieChart = ChartFactory.createPieChart(
                "",
                dataset,
                true,
                true,
                false);

        // Customize the chart
        pieChart.setBackgroundPaint(Color.WHITE);

        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setShadowPaint(null);
        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 200));

        // Set nice colors
        plot.setSectionPaint("Áo", new Color(41, 128, 185));
        plot.setSectionPaint("Quần", new Color(46, 204, 113));
        plot.setSectionPaint("Váy", new Color(155, 89, 182));
        plot.setSectionPaint("Phụ kiện", new Color(230, 126, 34));
        plot.setSectionPaint("Giày dép", new Color(52, 152, 219));

        // Create chart panel
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setBackground(Color.WHITE);

        // Add components to container
        chartContainer.add(titlePanel, BorderLayout.NORTH);
        chartContainer.add(chartPanel, BorderLayout.CENTER);

        return chartContainer;
    }

    private JPanel createTopProductsChart() {
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(Color.WHITE);
        chartContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Top 5 Sản Phẩm Bán Chạy");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));

        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Create dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Add dummy data
        dataset.addValue(150, "Số lượng", "Áo thun nam");
        dataset.addValue(120, "Số lượng", "Quần jean nữ");
        dataset.addValue(100, "Số lượng", "Váy hoa");
        dataset.addValue(80, "Số lượng", "Áo khoác");
        dataset.addValue(70, "Số lượng", "Quần short");

        // Create chart
        JFreeChart barChart = ChartFactory.createBarChart(
                "",
                "",
                "Số lượng bán ra",
                dataset,
                PlotOrientation.HORIZONTAL,
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
        renderer.setSeriesPaint(0, new Color(52, 152, 219));
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);

        // Create chart panel
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setBackground(Color.WHITE);

        // Add components to container
        chartContainer.add(titlePanel, BorderLayout.NORTH);
        chartContainer.add(chartPanel, BorderLayout.CENTER);

        return chartContainer;
    }

    private JPanel createDailyStatsPanel() {
        JPanel statsContainer = new JPanel(new BorderLayout());
        statsContainer.setBackground(Color.WHITE);
        statsContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Tổng Quan Ngày Hôm Nay");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));

        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Stats cards in a grid
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        statsPanel.setBackground(Color.WHITE);

        // Create 4 stat cards
        statsPanel.add(createStatCard("Đơn hàng hôm nay", "12", new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Doanh thu hôm nay", "4,500,000 VNĐ", new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Khách hàng mới", "5", new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Sản phẩm bán ra", "28", new Color(230, 126, 34)));

        // Add components to container
        statsContainer.add(titlePanel, BorderLayout.NORTH);
        statsContainer.add(statsPanel, BorderLayout.CENTER);

        return statsContainer;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true));

        // Create color bar at top
        JPanel colorBar = new JPanel();
        colorBar.setBackground(color);
        colorBar.setPreferredSize(new Dimension(0, 5));
        card.add(colorBar, BorderLayout.NORTH);

        // Create content panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(127, 140, 141));

        // Value with larger, colored font
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);

        contentPanel.add(titleLabel);
        contentPanel.add(valueLabel);

        card.add(contentPanel, BorderLayout.CENTER);
        return card;
    }

    private void refreshCharts(String timeRange) {
        // Dummy refresh function - would normally update chart data based on time range
        JOptionPane.showMessageDialog(this,
                "Đang cập nhật dữ liệu thống kê cho giai đoạn: " + timeRange,
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}

package main.java.com.shop.ui;

import javax.swing.*;
import java.awt.*;

public class InvoicePanel extends JPanel {
    private JTable table;
    private JTextField tfCustomer, tfDate, tfTotal;

    public InvoicePanel() {
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

        JLabel title = new JLabel("QUẢN LÝ HÓA ĐƠN", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        titlePanel.add(title, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        tfCustomer = new JTextField();
        tfDate = new JTextField();
        tfTotal = new JTextField();
        formPanel.add(new JLabel("Khách hàng:"));
        formPanel.add(tfCustomer);
        formPanel.add(new JLabel());

        formPanel.add(new JLabel("Ngày:"));
        formPanel.add(tfDate);
        formPanel.add(new JLabel("Tổng tiền:"));
        formPanel.add(tfTotal);

        add(formPanel, BorderLayout.SOUTH);

        String[] columns = { "Mã HD", "Khách hàng", "Ngày", "Tổng tiền" };
        Object[][] data = {
                { "HD001", "Nguyễn Văn A", "2024-05-01", 450000 },
                { "HD002", "Trần Thị B", "2024-05-02", 300000 }
        };
        table = new JTable(data, columns);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}

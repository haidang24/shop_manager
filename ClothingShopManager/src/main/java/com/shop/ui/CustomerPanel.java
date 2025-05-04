
package main.java.com.shop.ui;

import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JPanel {
    private JTable table;
    private JTextField tfName, tfPhone, tfEmail;

    public CustomerPanel() {
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

        JLabel title = new JLabel("QUẢN LÝ KHÁCH HÀNG", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        titlePanel.add(title, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        tfName = new JTextField();
        tfPhone = new JTextField();
        tfEmail = new JTextField();
        formPanel.add(new JLabel("Tên KH:"));
        formPanel.add(tfName);
        formPanel.add(new JLabel());

        formPanel.add(new JLabel("SĐT:"));
        formPanel.add(tfPhone);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(tfEmail);

        add(formPanel, BorderLayout.SOUTH);

        String[] columns = { "ID", "Tên", "SĐT", "Email" };
        Object[][] data = {
                { "C001", "Nguyễn Văn A", "0901234567", "a@gmail.com" },
                { "C002", "Trần Thị B", "0912345678", "b@yahoo.com" }
        };
        table = new JTable(data, columns);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}

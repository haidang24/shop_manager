package com.shopmanager.ui.components;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.shopmanager.utils.SessionManager;

public class BasePanel extends JPanel {
    protected Color primaryColor = new Color(41, 128, 185);
    protected Color secondaryColor = new Color(44, 62, 80);
    protected Color accentColor = new Color(46, 204, 113);
    protected Color backgroundColor = new Color(236, 240, 241);
    protected Color textColor = new Color(52, 73, 94);

    public BasePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(backgroundColor);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    protected JPanel createHeaderPanel(String title) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(backgroundColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(textColor);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        return headerPanel;
    }

    protected JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    protected JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 35));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Đổi sang màu nền rõ ràng và màu chữ đậm
        Color borderColor = new Color(203, 213, 225);
        Color focusBorderColor = primaryColor;
        Color bgColor = Color.WHITE; // Sử dụng màu trắng hoàn toàn cho nền

        // Sử dụng border đơn giản hơn để tránh xung đột
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Thiết lập thuộc tính hiển thị rõ ràng
        textField.setOpaque(true);
        textField.setBackground(bgColor);
        textField.setForeground(new Color(0, 0, 0)); // Đen hoàn toàn
        textField.setCaretColor(primaryColor);

        // Add focus effect
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(focusBorderColor, 2, true),
                        BorderFactory.createEmptyBorder(4, 9, 4, 9)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderColor, 1, true),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            }
        });

        // Add hover effect with mouse listener
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!textField.hasFocus()) {
                    Color hoverBorderColor = new Color(165, 180, 200);
                    textField.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(hoverBorderColor, 1, true),
                            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!textField.hasFocus()) {
                    textField.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(borderColor, 1, true),
                            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
                }
            }
        });

        return textField;
    }

    // Custom class for rounded borders
    static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        public RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    // Custom border class that fills the background with rounded corners
    static class FilledRoundedBorder extends AbstractBorder {
        private final Color borderColor;
        private final Color backgroundColor;
        private final int radius;

        public FilledRoundedBorder(Color borderColor, Color backgroundColor, int radius) {
            this.borderColor = borderColor;
            this.backgroundColor = backgroundColor;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Paint the background
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(x, y, width - 1, height - 1, radius, radius);

            // Paint the border
            g2d.setColor(borderColor);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);

            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }
    }

    protected JTable createTable() {
        JTable table = new JTable();
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setShowGrid(true);
        table.setGridColor(new Color(189, 195, 199));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(secondaryColor);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(primaryColor);
        table.setSelectionForeground(Color.WHITE);
        return table;
    }

    protected JScrollPane wrapInScrollPane(JComponent component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(backgroundColor);
        return scrollPane;
    }

    protected void addFormField(JPanel panel, String labelText, JComponent component,
            GridBagConstraints gbc, int gridy, int gridx) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(textColor);

        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(label, gbc);

        gbc.gridx = gridx + 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(component, gbc);

        // Reset weightx
        gbc.weightx = 0.0;
    }

    /**
     * Check if the current user has admin permissions
     * 
     * @return true if the user is an admin, false otherwise
     */
    protected boolean isUserAdmin() {
        return SessionManager.isAdmin();
    }

    /**
     * Show an error message if the user doesn't have admin permissions
     * 
     * @return true if the user has admin permissions, false otherwise
     */
    protected boolean checkAdminPermission() {
        if (!isUserAdmin()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Bạn không có quyền thực hiện chức năng này!",
                    "Lỗi phân quyền",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
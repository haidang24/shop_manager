
package main.java.com.shop.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class ProductPanel extends JPanel {
    private JTable table;
    private JTextField tfName, tfType, tfPrice, tfQuantity;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private JTextField tfSearch;
    private Color primaryColor = new Color(25, 118, 210); // Deeper blue
    private Color secondaryColor = new Color(66, 165, 245); // Lighter blue
    private Color accentColor = new Color(255, 111, 0); // Vibrant orange
    private Color bgLight = new Color(250, 251, 252); // Softer white
    private Color bgDark = new Color(238, 242, 246); // Subtle gray
    private Color textDark = new Color(33, 33, 33); // Near black
    private Color textLight = new Color(158, 158, 158); // Medium gray

    public ProductPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(bgLight);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel tiêu đề với gradient
        JPanel titlePanel = createGradientPanel(primaryColor, secondaryColor);
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("QUẢN LÝ SẢN PHẨM", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        // Thay đổi cách xử lý icon
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/resources/product_icon.png"));
            title.setIcon(icon);
            title.setIconTextGap(15);
        } catch (Exception e) {
            // Nếu không tìm thấy icon, chỉ hiển thị text
            title.setText("👕 QUẢN LÝ SẢN PHẨM");
        }

        titlePanel.add(title, BorderLayout.CENTER);

        // Panel tìm kiếm - Chỉnh màu nền và chữ
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        tfSearch = new JTextField(15);
        tfSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        tfSearch.setForeground(Color.BLACK); // Đổi màu chữ thành đen
        tfSearch.setBackground(Color.WHITE); // Đổi màu nền thành trắng
        tfSearch.setCaretColor(Color.BLACK); // Đổi màu con trỏ thành đen

        // Placeholder cho ô tìm kiếm với màu xám nhạt
        tfSearch.setText("Tìm kiếm sản phẩm...");
        tfSearch.setForeground(new Color(128, 128, 128)); // Màu chữ placeholder xám

        tfSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tfSearch.getText().equals("Tìm kiếm sản phẩm...")) {
                    tfSearch.setText("");
                    tfSearch.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (tfSearch.getText().isEmpty()) {
                    tfSearch.setText("Tìm kiếm sản phẩm...");
                    tfSearch.setForeground(new Color(255, 255, 255, 180));
                }
            }
        });

        // Sửa lại phần xử lý icon search
        btnSearch = createIconButton("Tìm", new Color(41, 128, 185));
        try {
            ImageIcon searchIcon = new ImageIcon(getClass().getResource("/resources/search_icon.png"));
            btnSearch.setIcon(searchIcon);
        } catch (Exception e) {
            btnSearch.setText("🔍");
        }

        searchPanel.add(tfSearch);
        searchPanel.add(btnSearch);
        titlePanel.add(searchPanel, BorderLayout.SOUTH);

        add(titlePanel, BorderLayout.NORTH);

        // Panel chứa bảng với hiệu ứng đổ bóng
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(bgLight);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                new ShadowBorder(),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        // Tạo bảng với style hiện đại
        String[] columns = { "Mã", "Tên sản phẩm", "Loại", "Giá (VNĐ)", "Số lượng" };
        Object[][] data = {
                { "P001", "Áo thun nam cổ tròn", "Áo thun", 150000, 20 },
                { "P002", "Quần jean nam slim fit", "Quần jean", 300000, 15 },
                { "P003", "Áo sơ mi nữ tay dài", "Áo sơ mi", 250000, 12 },
                { "P004", "Váy liền thân dáng xòe", "Váy", 350000, 8 },
                { "P005", "Áo khoác denim unisex", "Áo khoác", 450000, 10 },
                { "P006", "Quần tây nam công sở", "Quần tây", 280000, 18 }
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp trên bảng
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(52, 152, 219, 100));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        // Tùy chỉnh header của bảng
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, secondaryColor));

        // Tùy chỉnh renderer cho các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Mã
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer); // Giá
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Số lượng

        // Thiết lập độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(80); // Mã
        table.getColumnModel().getColumn(1).setPreferredWidth(250); // Tên
        table.getColumnModel().getColumn(2).setPreferredWidth(120); // Loại
        table.getColumnModel().getColumn(3).setPreferredWidth(120); // Giá
        table.getColumnModel().getColumn(4).setPreferredWidth(80); // Số lượng

        // Thêm hiệu ứng hover cho dòng
        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                if (row >= 0) {
                    if (table.getSelectedRow() != row) {
                        table.setRowSelectionInterval(row, row);
                    }
                } else {
                    table.clearSelection();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Form nhập liệu với hiệu ứng đổ bóng
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(bgLight);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new ShadowBorder(),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20))));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Tạo các label và text field với style hiện đại
        JLabel lblName = createStyledLabel("Tên sản phẩm:");
        JLabel lblType = createStyledLabel("Loại sản phẩm:");
        JLabel lblPrice = createStyledLabel("Giá (VNĐ):");
        JLabel lblQuantity = createStyledLabel("Số lượng:");

        tfName = createStyledTextField(15);
        tfType = createStyledTextField(15);
        tfPrice = createStyledTextField(15);
        tfQuantity = createStyledTextField(15);

        // Thêm các thành phần vào form
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(lblName, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(tfName, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(lblType, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(tfType, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(lblPrice, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(tfPrice, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(lblQuantity, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(tfQuantity, gbc);

        // Panel chứa các nút với hiệu ứng hover
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        btnAdd = createAnimatedButton("Thêm mới", new Color(46, 204, 113));
        btnUpdate = createAnimatedButton("Cập nhật", new Color(52, 152, 219));
        btnDelete = createAnimatedButton("Xóa", new Color(231, 76, 60));
        btnClear = createAnimatedButton("Làm mới", new Color(149, 165, 166));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(20, 8, 8, 8);
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.SOUTH);

        // Thêm hiệu ứng khi chọn dòng trong bảng
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                tfName.setText(table.getValueAt(row, 1).toString());
                tfType.setText(table.getValueAt(row, 2).toString());
                tfPrice.setText(table.getValueAt(row, 3).toString());
                tfQuantity.setText(table.getValueAt(row, 4).toString());
            }
        });

        // Thêm action cho nút làm mới
        btnClear.addActionListener(e -> {
            tfName.setText("");
            tfType.setText("");
            tfPrice.setText("");
            tfQuantity.setText("");
            table.clearSelection();
        });
    }

    // Panel với nền gradient
    private JPanel createGradientPanel(Color color1, Color color2) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(70, 70, 70));
        return label;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));

        // Thêm hiệu ứng focus
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, accentColor),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
            }
        });

        return textField;
    }

    private JButton createAnimatedButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Thêm hiệu ứng hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(getDarkerColor(bgColor));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(getLighterColor(bgColor));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(getDarkerColor(bgColor));
            }
        });

        return button;
    }

    private JButton createIconButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 0));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private Color getDarkerColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        return Color.getHSBColor(hsb[0], hsb[1], Math.max(0, hsb[2] - 0.1f));
    }

    private Color getLighterColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        return Color.getHSBColor(hsb[0], hsb[1], Math.min(1.0f, hsb[2] + 0.1f));
    }

    // Lớp tạo hiệu ứng đổ bóng
    private class ShadowBorder extends AbstractBorder {
        private int shadowSize = 5;

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color shadowColor = new Color(0, 0, 0, 50);
            g2.setColor(shadowColor);

            // Vẽ bóng đổ
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


package main.java.com.shop.ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;

public class EmployeePanel extends JPanel {
    private JTable table;
    private JTextField tfName, tfRole, tfPhone, tfEmail;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private Color primaryColor = new Color(25, 118, 210);
    private Color secondaryColor = new Color(66, 165, 245);
    private Color accentColor = new Color(255, 111, 0);
    private Color bgLight = new Color(250, 251, 252);
    private Color cardBg = new Color(255, 255, 255);

    public EmployeePanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(bgLight);

        // Card panel for content
        JPanel cardPanel = new JPanel(new BorderLayout(20, 20));
        cardPanel.setBackground(cardBg);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Title
        JLabel title = new JLabel("Quản Lý Nhân Viên", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(primaryColor);
        cardPanel.add(title, BorderLayout.NORTH);

        // Table
        String[] columns = { "Mã NV", "Tên nhân viên", "Chức vụ", "Số điện thoại", "Email" };
        Object[][] data = {
                { "E001", "Nguyễn Văn B", "Quản lý", "0901234567", "b.nguyen@email.com" },
                { "E002", "Trần Thị C", "Bán hàng", "0912345678", "c.tran@email.com" },
                { "E003", "Lê Văn D", "Kho", "0987654321", "d.le@email.com" }
        };
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(new Color(33, 33, 33));
        table.setSelectionBackground(new Color(66, 165, 245, 80));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 36));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, secondaryColor));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        cardPanel.add(scrollPane, BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblName = new JLabel("Tên nhân viên:");
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfName = new JTextField(15);

        JLabel lblRole = new JLabel("Chức vụ:");
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfRole = new JTextField(15);

        JLabel lblPhone = new JLabel("Số điện thoại:");
        lblPhone.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfPhone = new JTextField(15);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfEmail = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblName, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(tfName, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        formPanel.add(lblRole, gbc);
        gbc.gridx = 3;
        gbc.gridy = 0;
        formPanel.add(tfRole, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblPhone, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(tfPhone, gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        formPanel.add(lblEmail, gbc);
        gbc.gridx = 3;
        gbc.gridy = 1;
        formPanel.add(tfEmail, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        btnAdd = new JButton("Thêm mới");
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa");
        btnClear = new JButton("Làm mới");

        JButton[] buttons = { btnAdd, btnUpdate, btnDelete, btnClear };
        Color[] btnColors = {
                new Color(46, 204, 113),
                new Color(52, 152, 219),
                new Color(231, 76, 60),
                new Color(149, 165, 166)
        };
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setFont(new Font("Segoe UI", Font.BOLD, 14));
            buttons[i].setBackground(btnColors[i]);
            buttons[i].setForeground(Color.BLACK);
            buttons[i].setFocusPainted(false);
            buttons[i].setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
            buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttonPanel.add(buttons[i]);
        }

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(18, 8, 8, 8);
        formPanel.add(buttonPanel, gbc);

        cardPanel.add(formPanel, BorderLayout.SOUTH);

        add(cardPanel, BorderLayout.CENTER);

        // Table row selection fills form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                tfName.setText(table.getValueAt(row, 1).toString());
                tfRole.setText(table.getValueAt(row, 2).toString());
                tfPhone.setText(table.getValueAt(row, 3).toString());
                tfEmail.setText(table.getValueAt(row, 4).toString());
            }
        });

        // Clear button resets form
        btnClear.addActionListener(e -> {
            tfName.setText("");
            tfRole.setText("");
            tfPhone.setText("");
            tfEmail.setText("");
            table.clearSelection();
        });
    }
}

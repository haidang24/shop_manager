package com.shopmanager.ui;

import com.shopmanager.models.Order;
import com.shopmanager.utils.DatabaseConnection;
import com.shopmanager.ui.components.BasePanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class OrderPanel extends BasePanel {
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public OrderPanel() {
        super();
        initComponents();
        loadOrderData();
    }

    private void initComponents() {
        // Header with action buttons
        JPanel headerPanel = createHeaderPanel("Quản Lý Đơn Hàng");

        // Add action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(backgroundColor);

        JButton addButton = createButton("Thêm đơn hàng", accentColor);
        JButton refreshButton = createButton("Làm mới", primaryColor);

        actionPanel.add(addButton);
        actionPanel.add(refreshButton);
        headerPanel.add(actionPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(backgroundColor);

        // Add search panel at the top
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(backgroundColor);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JTextField searchField = createTextField();
        searchField.setPreferredSize(new Dimension(300, 35));
        JButton searchButton = createButton("Tìm kiếm", primaryColor);

        searchPanel.add(new JLabel("Tìm kiếm: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Table panel
        String[] columns = { "ID", "Khách hàng", "Ngày đặt", "Tổng tiền", "Trạng thái" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        orderTable = createTable();
        orderTable.setModel(tableModel);

        // Set column widths
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        orderTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        orderTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        // Add table and action buttons panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(backgroundColor);
        tablePanel.add(wrapInScrollPane(orderTable), BorderLayout.CENTER);

        // Create buttons panel at the bottom
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(backgroundColor);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton viewButton = createButton("Xem chi tiết", primaryColor);
        JButton editButton = createButton("Sửa", new Color(52, 152, 219));
        JButton deleteButton = createButton("Xóa", new Color(231, 76, 60));

        buttonsPanel.add(viewButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);

        tablePanel.add(buttonsPanel, BorderLayout.SOUTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners to buttons
        addButton.addActionListener(e -> showAddOrderDialog());
        refreshButton.addActionListener(e -> loadOrderData());
        viewButton.addActionListener(e -> viewOrderDetails());
        editButton.addActionListener(e -> editOrder());
        deleteButton.addActionListener(e -> deleteOrder());
        searchButton.addActionListener(e -> searchOrders(searchField.getText()));
    }

    private void loadOrderData() {
        // Clear existing data
        tableModel.setRowCount(0);

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT o.id, c.name, o.order_date, o.total_amount, o.status " +
                                "FROM orders o " +
                                "JOIN customers c ON o.customer_id = c.id " +
                                "ORDER BY o.id DESC")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String customerName = rs.getString("name");
                java.util.Date orderDate = rs.getDate("order_date");
                double totalAmount = rs.getDouble("total_amount");
                String status = rs.getString("status");

                // Format the values
                String formattedDate = dateFormat.format(orderDate);
                String formattedAmount = String.format("%,.0f VNĐ", totalAmount);

                tableModel.addRow(new Object[] {
                        id, customerName, formattedDate, formattedAmount, status
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu đơn hàng: " + e.getMessage(),
                    "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchOrders(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadOrderData();
            return;
        }

        tableModel.setRowCount(0);

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT o.id, c.name, o.order_date, o.total_amount, o.status " +
                                "FROM orders o " +
                                "JOIN customers c ON o.customer_id = c.id " +
                                "WHERE c.name LIKE ? OR o.id LIKE ? OR o.status LIKE ? " +
                                "ORDER BY o.id DESC")) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String customerName = rs.getString("name");
                    java.util.Date orderDate = rs.getDate("order_date");
                    double totalAmount = rs.getDouble("total_amount");
                    String status = rs.getString("status");

                    // Format the values
                    String formattedDate = dateFormat.format(orderDate);
                    String formattedAmount = String.format("%,.0f VNĐ", totalAmount);

                    tableModel.addRow(new Object[] {
                            id, customerName, formattedDate, formattedAmount, status
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tìm kiếm đơn hàng: " + e.getMessage(),
                    "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddOrderDialog() {
        JOptionPane.showMessageDialog(this,
                "Chức năng thêm đơn hàng đang được phát triển.",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewOrderDetails() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một đơn hàng để xem chi tiết.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int orderId = (int) tableModel.getValueAt(selectedRow, 0);
        JOptionPane.showMessageDialog(this,
                "Đang xem chi tiết đơn hàng #" + orderId,
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một đơn hàng để chỉnh sửa.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int orderId = (int) tableModel.getValueAt(selectedRow, 0);
        JOptionPane.showMessageDialog(this,
                "Chức năng chỉnh sửa đơn hàng #" + orderId + " đang được phát triển.",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một đơn hàng để xóa.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int orderId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa đơn hàng #" + orderId + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "Chức năng xóa đơn hàng đang được phát triển.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
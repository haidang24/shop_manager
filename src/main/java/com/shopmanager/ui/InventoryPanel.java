package com.shopmanager.ui;

import com.shopmanager.models.InventoryTransaction;
import com.shopmanager.utils.DatabaseConnection;
import com.shopmanager.ui.components.BasePanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class InventoryPanel extends BasePanel {
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public InventoryPanel() {
        super();
        initComponents();
        loadInventoryData();
    }

    private void initComponents() {
        // Header with actions
        JPanel headerPanel = createHeaderPanel("Quản Lý Nhập Hàng");

        // Add action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(backgroundColor);

        JButton addButton = createButton("Thêm phiếu nhập", accentColor);
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

        // Add filters
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        filterPanel.setBackground(backgroundColor);

        JLabel typeLabel = new JLabel("Loại giao dịch:");
        String[] types = { "Tất cả", "Nhập kho", "Xuất kho", "Trả hàng", "Kiểm kê" };
        JComboBox<String> typeComboBox = new JComboBox<>(types);
        typeComboBox.setPreferredSize(new Dimension(150, 35));

        filterPanel.add(typeLabel);
        filterPanel.add(typeComboBox);

        // Combine search and filter panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(backgroundColor);
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(filterPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Table panel
        String[] columns = { "ID", "Sản phẩm", "Nhà cung cấp", "Loại", "Số lượng", "Đơn giá", "Thành tiền", "Ngày" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        inventoryTable = createTable();
        inventoryTable.setModel(tableModel);

        // Set column widths
        inventoryTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        inventoryTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        inventoryTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        inventoryTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        inventoryTable.getColumnModel().getColumn(4).setPreferredWidth(70);
        inventoryTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        inventoryTable.getColumnModel().getColumn(6).setPreferredWidth(120);
        inventoryTable.getColumnModel().getColumn(7).setPreferredWidth(100);

        // Table container
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(backgroundColor);
        tablePanel.add(wrapInScrollPane(inventoryTable), BorderLayout.CENTER);

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

        // Add action listeners
        addButton.addActionListener(e -> showAddInventoryDialog());
        refreshButton.addActionListener(e -> loadInventoryData());
        viewButton.addActionListener(e -> viewInventoryDetails());
        editButton.addActionListener(e -> editInventory());
        deleteButton.addActionListener(e -> deleteInventory());
        searchButton.addActionListener(e -> searchInventory(searchField.getText()));
        typeComboBox.addActionListener(e -> filterByType((String) typeComboBox.getSelectedItem()));
    }

    private void loadInventoryData() {
        // Clear existing data
        tableModel.setRowCount(0);

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT it.id, p.name AS product_name, s.name AS supplier_name, " +
                                "it.transaction_type, it.quantity, it.unit_price, it.quantity * it.unit_price AS total_amount, "
                                +
                                "it.transaction_date " +
                                "FROM inventory_transactions it " +
                                "JOIN products p ON it.product_id = p.id " +
                                "JOIN suppliers s ON it.supplier_id = s.id " +
                                "ORDER BY it.transaction_date DESC")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String productName = rs.getString("product_name");
                String supplierName = rs.getString("supplier_name");
                String transactionType = rs.getString("transaction_type");
                int quantity = rs.getInt("quantity");
                double unitPrice = rs.getDouble("unit_price");
                double totalAmount = rs.getDouble("total_amount");
                java.util.Date transactionDate = rs.getDate("transaction_date");

                // Format values
                String formattedUnitPrice = String.format("%,.0f VNĐ", unitPrice);
                String formattedTotal = String.format("%,.0f VNĐ", totalAmount);
                String formattedDate = dateFormat.format(transactionDate);

                tableModel.addRow(new Object[] {
                        id, productName, supplierName, transactionType, quantity, formattedUnitPrice, formattedTotal,
                        formattedDate
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu nhập hàng: " + e.getMessage(),
                    "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchInventory(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadInventoryData();
            return;
        }

        tableModel.setRowCount(0);

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT it.id, p.name AS product_name, s.name AS supplier_name, " +
                                "it.transaction_type, it.quantity, it.unit_price, it.quantity * it.unit_price AS total_amount, "
                                +
                                "it.transaction_date " +
                                "FROM inventory_transactions it " +
                                "JOIN products p ON it.product_id = p.id " +
                                "JOIN suppliers s ON it.supplier_id = s.id " +
                                "WHERE p.name LIKE ? OR s.name LIKE ? OR it.transaction_type LIKE ? " +
                                "ORDER BY it.transaction_date DESC")) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String productName = rs.getString("product_name");
                    String supplierName = rs.getString("supplier_name");
                    String transactionType = rs.getString("transaction_type");
                    int quantity = rs.getInt("quantity");
                    double unitPrice = rs.getDouble("unit_price");
                    double totalAmount = rs.getDouble("total_amount");
                    java.util.Date transactionDate = rs.getDate("transaction_date");

                    // Format values
                    String formattedUnitPrice = String.format("%,.0f VNĐ", unitPrice);
                    String formattedTotal = String.format("%,.0f VNĐ", totalAmount);
                    String formattedDate = dateFormat.format(transactionDate);

                    tableModel.addRow(new Object[] {
                            id, productName, supplierName, transactionType, quantity, formattedUnitPrice,
                            formattedTotal, formattedDate
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tìm kiếm dữ liệu nhập hàng: " + e.getMessage(),
                    "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterByType(String type) {
        if (type.equals("Tất cả")) {
            loadInventoryData();
            return;
        }

        tableModel.setRowCount(0);

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT it.id, p.name AS product_name, s.name AS supplier_name, " +
                                "it.transaction_type, it.quantity, it.unit_price, it.quantity * it.unit_price AS total_amount, "
                                +
                                "it.transaction_date " +
                                "FROM inventory_transactions it " +
                                "JOIN products p ON it.product_id = p.id " +
                                "JOIN suppliers s ON it.supplier_id = s.id " +
                                "WHERE it.transaction_type = ? " +
                                "ORDER BY it.transaction_date DESC")) {

            pstmt.setString(1, type);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String productName = rs.getString("product_name");
                    String supplierName = rs.getString("supplier_name");
                    String transactionType = rs.getString("transaction_type");
                    int quantity = rs.getInt("quantity");
                    double unitPrice = rs.getDouble("unit_price");
                    double totalAmount = rs.getDouble("total_amount");
                    java.util.Date transactionDate = rs.getDate("transaction_date");

                    // Format values
                    String formattedUnitPrice = String.format("%,.0f VNĐ", unitPrice);
                    String formattedTotal = String.format("%,.0f VNĐ", totalAmount);
                    String formattedDate = dateFormat.format(transactionDate);

                    tableModel.addRow(new Object[] {
                            id, productName, supplierName, transactionType, quantity, formattedUnitPrice,
                            formattedTotal, formattedDate
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lọc dữ liệu nhập hàng: " + e.getMessage(),
                    "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddInventoryDialog() {
        JOptionPane.showMessageDialog(this,
                "Chức năng thêm phiếu nhập hàng đang được phát triển.",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewInventoryDetails() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một phiếu nhập để xem chi tiết.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        JOptionPane.showMessageDialog(this,
                "Đang xem chi tiết phiếu nhập #" + id,
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editInventory() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một phiếu nhập để chỉnh sửa.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        JOptionPane.showMessageDialog(this,
                "Chức năng sửa phiếu nhập #" + id + " đang được phát triển.",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteInventory() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một phiếu nhập để xóa.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa phiếu nhập #" + id + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "Chức năng xóa phiếu nhập đang được phát triển.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
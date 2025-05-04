package com.shopmanager.ui;

import com.shopmanager.models.Supplier;
import com.shopmanager.utils.DatabaseConnection;
import com.shopmanager.ui.components.BasePanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SupplierPanel extends BasePanel {
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private JTextField txtName, txtContact, txtPhone, txtEmail, txtAddress;

    public SupplierPanel() {
        super();
        initComponents();
        loadSupplierData();
    }

    private void initComponents() {
        // Header with action buttons
        JPanel headerPanel = createHeaderPanel("Quản Lý Nhà Cung Cấp");

        // Add action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(backgroundColor);

        JButton addButton = createButton("Thêm nhà cung cấp", accentColor);
        JButton refreshButton = createButton("Làm mới", primaryColor);

        actionPanel.add(addButton);
        actionPanel.add(refreshButton);
        headerPanel.add(actionPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Main panel with split layout
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

        // Split panel for table and form
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(650);
        splitPane.setDividerSize(1);
        splitPane.setBackground(backgroundColor);

        // Table panel (left side)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(backgroundColor);

        // Table model
        String[] columns = { "ID", "Tên nhà cung cấp", "Người liên hệ", "Điện thoại", "Email", "Địa chỉ" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        supplierTable = createTable();
        supplierTable.setModel(tableModel);

        // Set column widths
        supplierTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        supplierTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        supplierTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        supplierTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        supplierTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        supplierTable.getColumnModel().getColumn(5).setPreferredWidth(200);

        tablePanel.add(wrapInScrollPane(supplierTable), BorderLayout.CENTER);

        // Create buttons panel at the bottom
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(backgroundColor);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton editButton = createButton("Sửa", new Color(52, 152, 219));
        JButton deleteButton = createButton("Xóa", new Color(231, 76, 60));

        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);

        tablePanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Form panel (right side)
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(backgroundColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel formFieldsPanel = new JPanel(new GridBagLayout());
        formFieldsPanel.setBackground(Color.WHITE);
        formFieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form fields
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 10, 5);

        JLabel formTitle = new JLabel("Thông tin nhà cung cấp");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 20, 5);
        formFieldsPanel.add(formTitle, gbc);

        // Reset for other components
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 10, 5);

        // Add form fields
        txtName = addFormField(formFieldsPanel, "Tên nhà cung cấp:", 1, gbc);
        txtContact = addFormField(formFieldsPanel, "Người liên hệ:", 2, gbc);
        txtPhone = addFormField(formFieldsPanel, "Điện thoại:", 3, gbc);
        txtEmail = addFormField(formFieldsPanel, "Email:", 4, gbc);
        txtAddress = addFormField(formFieldsPanel, "Địa chỉ:", 5, gbc);

        // Form buttons
        JPanel formButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formButtonsPanel.setBackground(Color.WHITE);

        JButton saveButton = createButton("Lưu", accentColor);
        JButton clearButton = createButton("Xóa form", new Color(189, 195, 199));

        formButtonsPanel.add(saveButton);
        formButtonsPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 10, 5);
        formFieldsPanel.add(formButtonsPanel, gbc);

        formPanel.add(formFieldsPanel, BorderLayout.NORTH);

        // Add components to split pane
        splitPane.setLeftComponent(tablePanel);
        splitPane.setRightComponent(formPanel);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(e -> clearForm());
        refreshButton.addActionListener(e -> loadSupplierData());
        editButton.addActionListener(e -> editSupplier());
        deleteButton.addActionListener(e -> deleteSupplier());
        saveButton.addActionListener(e -> saveSupplier());
        clearButton.addActionListener(e -> clearForm());
        searchButton.addActionListener(e -> searchSuppliers(searchField.getText()));
        supplierTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = supplierTable.getSelectedRow();
                if (selectedRow != -1) {
                    loadSupplierToForm(selectedRow);
                }
            }
        });
    }

    private JTextField addFormField(JPanel panel, String labelText, int row, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);

        JTextField textField = createTextField();
        gbc.gridx = 1;
        panel.add(textField, gbc);

        return textField;
    }

    private void loadSupplierData() {
        // Clear existing data
        tableModel.setRowCount(0);

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT id, name, contact_person, phone, email, address " +
                                "FROM suppliers " +
                                "ORDER BY id")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String contactPerson = rs.getString("contact_person");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String address = rs.getString("address");

                tableModel.addRow(new Object[] {
                        id, name, contactPerson, phone, email, address
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu nhà cung cấp: " + e.getMessage(),
                    "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchSuppliers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadSupplierData();
            return;
        }

        tableModel.setRowCount(0);

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT id, name, contact_person, phone, email, address " +
                                "FROM suppliers " +
                                "WHERE name LIKE ? OR contact_person LIKE ? OR phone LIKE ? OR email LIKE ? " +
                                "ORDER BY id")) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String contactPerson = rs.getString("contact_person");
                    String phone = rs.getString("phone");
                    String email = rs.getString("email");
                    String address = rs.getString("address");

                    tableModel.addRow(new Object[] {
                            id, name, contactPerson, phone, email, address
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tìm kiếm nhà cung cấp: " + e.getMessage(),
                    "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSupplierToForm(int selectedRow) {
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String contactPerson = (String) tableModel.getValueAt(selectedRow, 2);
        String phone = (String) tableModel.getValueAt(selectedRow, 3);
        String email = (String) tableModel.getValueAt(selectedRow, 4);
        String address = (String) tableModel.getValueAt(selectedRow, 5);

        txtName.setText(name);
        txtContact.setText(contactPerson);
        txtPhone.setText(phone);
        txtEmail.setText(email);
        txtAddress.setText(address);
    }

    private void clearForm() {
        txtName.setText("");
        txtContact.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        supplierTable.clearSelection();
    }

    private void saveSupplier() {
        // Validate inputs
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tên nhà cung cấp không được để trống!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtName.requestFocus();
            return;
        }

        int selectedRow = supplierTable.getSelectedRow();

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (selectedRow == -1) {
                // Insert new supplier
                try (PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO suppliers (name, contact_person, phone, email, address) " +
                                "VALUES (?, ?, ?, ?, ?)")) {

                    pstmt.setString(1, txtName.getText().trim());
                    pstmt.setString(2, txtContact.getText().trim());
                    pstmt.setString(3, txtPhone.getText().trim());
                    pstmt.setString(4, txtEmail.getText().trim());
                    pstmt.setString(5, txtAddress.getText().trim());

                    int affected = pstmt.executeUpdate();
                    if (affected > 0) {
                        JOptionPane.showMessageDialog(this,
                                "Thêm nhà cung cấp thành công!",
                                "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                        loadSupplierData();
                    }
                }
            } else {
                // Update existing supplier
                int id = (int) tableModel.getValueAt(selectedRow, 0);

                try (PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE suppliers SET name=?, contact_person=?, phone=?, email=?, address=? " +
                                "WHERE id=?")) {

                    pstmt.setString(1, txtName.getText().trim());
                    pstmt.setString(2, txtContact.getText().trim());
                    pstmt.setString(3, txtPhone.getText().trim());
                    pstmt.setString(4, txtEmail.getText().trim());
                    pstmt.setString(5, txtAddress.getText().trim());
                    pstmt.setInt(6, id);

                    int affected = pstmt.executeUpdate();
                    if (affected > 0) {
                        JOptionPane.showMessageDialog(this,
                                "Cập nhật nhà cung cấp thành công!",
                                "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                        loadSupplierData();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lưu nhà cung cấp: " + e.getMessage(),
                    "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một nhà cung cấp để chỉnh sửa.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        loadSupplierToForm(selectedRow);
    }

    private void deleteSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một nhà cung cấp để xóa.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa nhà cung cấp '" + name + "'?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement("DELETE FROM suppliers WHERE id=?")) {

                pstmt.setInt(1, id);
                int affected = pstmt.executeUpdate();

                if (affected > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Xóa nhà cung cấp thành công!",
                            "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    loadSupplierData();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi xóa nhà cung cấp: " + e.getMessage(),
                        "Lỗi Database", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
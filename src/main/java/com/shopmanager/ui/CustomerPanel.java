package com.shopmanager.ui;

import com.shopmanager.models.Customer;
import com.shopmanager.utils.DatabaseConnection;
import com.shopmanager.ui.components.BasePanel;
import com.shopmanager.utils.ExcelExporter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.Desktop;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class CustomerPanel extends BasePanel {
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField txtName, txtPhone, txtEmail;
    private JTextArea txtAddress;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnExportExcel;
    private JTextField txtSearch;

    public CustomerPanel() {
        super();
        initComponents();
        loadCustomerData();
    }

    private void initComponents() {
        // Header Panel with Search
        JPanel headerPanel = createHeaderPanel("Quản Lý Khách Hàng");

        // Add search field to header
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(backgroundColor);
        txtSearch = createTextField();
        txtSearch.setPreferredSize(new Dimension(250, 35));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm khách hàng...");

        JButton btnSearch = createButton("Tìm Kiếm", primaryColor);
        btnSearch.setPreferredSize(new Dimension(100, 35));

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(backgroundColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create form fields
        txtName = createTextField();
        txtPhone = createTextField();
        txtEmail = createTextField();
        txtAddress = new JTextArea(3, 20);
        txtAddress.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        txtAddress.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        JScrollPane addressScroll = new JScrollPane(txtAddress);
        addressScroll.setPreferredSize(new Dimension(200, 80));

        // Add form fields
        addFormField(formPanel, "Tên khách hàng:", txtName, gbc, 0, 0);
        addFormField(formPanel, "Số điện thoại:", txtPhone, gbc, 0, 1);
        addFormField(formPanel, "Email:", txtEmail, gbc, 0, 2);

        // For addressScroll, we need special handling since it spans multiple columns
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel addressLabel = new JLabel("Địa chỉ:");
        addressLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addressLabel.setForeground(textColor);
        formPanel.add(addressLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 5;
        formPanel.add(addressScroll, gbc);
        gbc.gridwidth = 1;

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(backgroundColor);

        btnAdd = createButton("Thêm", accentColor);
        btnUpdate = createButton("Cập nhật", primaryColor);
        btnDelete = createButton("Xóa", new Color(231, 76, 60));
        btnClear = createButton("Làm mới", new Color(149, 165, 166));

        // Thêm nút xuất Excel
        btnExportExcel = createButton("Xuất Excel", new Color(46, 139, 87));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnExportExcel);

        // Combine form and button panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(backgroundColor);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Table
        String[] columns = { "ID", "Tên khách hàng", "Số điện thoại", "Email", "Địa chỉ" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        customerTable = createTable();
        customerTable.setModel(tableModel);

        // Main Layout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(backgroundColor);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(wrapInScrollPane(customerTable), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners
        setupActionListeners();
        setupSearch();
    }

    private void setupSearch() {
        txtSearch.addActionListener(e -> performSearch());
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        String searchText = txtSearch.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        customerTable.setRowSorter(sorter);

        if (searchText.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    private void setupActionListeners() {
        btnAdd.addActionListener(e -> addCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnClear.addActionListener(e -> clearFields());

        // Thêm action listener cho nút xuất Excel
        btnExportExcel.addActionListener(e -> exportToExcel());

        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = customerTable.getSelectedRow();
                if (row != -1) {
                    loadCustomerToFields(row);
                }
            }
        });
    }

    private void loadCustomerData() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM customers ORDER BY id DESC")) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("phone"));
                row.add(rs.getString("email"));
                row.add(rs.getString("address"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + e.getMessage());
        }
    }

    private void addCustomer() {
        if (!validateInputs())
            return;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO customers (name, phone, email, address) VALUES (?, ?, ?, ?)")) {

            setStatementParameters(pstmt);
            pstmt.executeUpdate();
            loadCustomerData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Khách hàng đã được thêm thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding customer: " + e.getMessage());
        }
    }

    private void updateCustomer() {
        if (!validateInputs())
            return;

        int row = customerTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng để cập nhật!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE customers SET name=?, phone=?, email=?, address=? WHERE id=?")) {

            setStatementParameters(pstmt);
            pstmt.setInt(5, (Integer) tableModel.getValueAt(row, 0));

            pstmt.executeUpdate();
            loadCustomerData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Khách hàng đã được cập nhật thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating customer: " + e.getMessage());
        }
    }

    private void deleteCustomer() {
        int row = customerTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa khách hàng này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement("DELETE FROM customers WHERE id=?")) {

                pstmt.setInt(1, (Integer) tableModel.getValueAt(row, 0));
                pstmt.executeUpdate();
                loadCustomerData();
                clearFields();
                JOptionPane.showMessageDialog(this, "Khách hàng đã được xóa thành công!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting customer: " + e.getMessage());
            }
        }
    }

    private boolean validateInputs() {
        if (txtName.getText().trim().isEmpty() || txtPhone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền tên và số điện thoại!");
            return false;
        }

        String phone = txtPhone.getText().trim();
        if (!phone.matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!");
            return false;
        }

        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!");
            return false;
        }

        return true;
    }

    private void setStatementParameters(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, txtName.getText().trim());
        pstmt.setString(2, txtPhone.getText().trim());
        pstmt.setString(3, txtEmail.getText().trim());
        pstmt.setString(4, txtAddress.getText().trim());
    }

    private void loadCustomerToFields(int row) {
        if (row != -1) {
            row = customerTable.convertRowIndexToModel(row);
            txtName.setText((String) tableModel.getValueAt(row, 1));
            txtPhone.setText((String) tableModel.getValueAt(row, 2));
            txtEmail.setText((String) tableModel.getValueAt(row, 3));
            txtAddress.setText((String) tableModel.getValueAt(row, 4));
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        customerTable.clearSelection();
    }

    private void exportToExcel() {
        // Kiểm tra nếu không có dữ liệu
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Không có dữ liệu để xuất!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Chọn vị trí lưu file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file Excel");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String directoryPath = fileChooser.getSelectedFile().getPath();
            String fileName = directoryPath + File.separator + "DanhSachKhachHang";

            // Xuất file Excel
            boolean success = ExcelExporter.exportToExcel(
                    customerTable,
                    fileName,
                    "Danh sách khách hàng",
                    "DANH SÁCH KHÁCH HÀNG");

            if (success) {
                int option = JOptionPane.showConfirmDialog(
                        this,
                        "Xuất file Excel thành công!\nBạn có muốn mở thư mục chứa file không?",
                        "Xuất Excel thành công",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    try {
                        Desktop.getDesktop().open(fileChooser.getSelectedFile());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                this,
                                "Không thể mở thư mục: " + ex.getMessage(),
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
}
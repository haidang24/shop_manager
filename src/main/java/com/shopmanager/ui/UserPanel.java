package com.shopmanager.ui;

import com.shopmanager.models.User;
import com.shopmanager.utils.DatabaseConnection;
import com.shopmanager.utils.FormatUtils;
import com.shopmanager.utils.Constants;
import com.shopmanager.ui.components.BasePanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class UserPanel extends BasePanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField txtUsername, txtFullName, txtPassword;
    private JComboBox<String> cmbRole;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTextField txtSearch;

    public UserPanel() {
        super();
        initComponents();
        loadUserData();
    }

    private void initComponents() {
        // Header Panel with Search
        JPanel headerPanel = createHeaderPanel("Quản Lý Người Dùng");

        // Add search field to header
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(backgroundColor);
        txtSearch = createTextField();
        txtSearch.setPreferredSize(new Dimension(250, 35));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm người dùng...");

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
        txtUsername = createTextField();
        txtFullName = createTextField();
        txtPassword = createTextField();

        cmbRole = new JComboBox<>(Constants.UserRole.getAllRoles());
        cmbRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbRole.setPreferredSize(new Dimension(200, 35));

        // Add form fields
        addFormField(formPanel, "Tên đăng nhập:", txtUsername, gbc, 0, 0);
        addFormField(formPanel, "Mật khẩu:", txtPassword, gbc, 0, 1);
        addFormField(formPanel, "Họ và tên:", txtFullName, gbc, 1, 0);
        addFormField(formPanel, "Vai trò:", cmbRole, gbc, 1, 1);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(backgroundColor);

        btnAdd = createButton("Thêm", accentColor);
        btnUpdate = createButton("Cập nhật", primaryColor);
        btnDelete = createButton("Xóa", new Color(231, 76, 60));
        btnClear = createButton("Làm mới", new Color(149, 165, 166));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        // Combine form and button panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(backgroundColor);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Table
        String[] columns = { "ID", "Tên đăng nhập", "Họ tên", "Vai trò", "Đăng nhập lần cuối" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = createTable();
        userTable.setModel(tableModel);

        // Main Layout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(backgroundColor);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(wrapInScrollPane(userTable), BorderLayout.CENTER);

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
        userTable.setRowSorter(sorter);

        if (searchText.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    private void setupActionListeners() {
        btnAdd.addActionListener(e -> addUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnClear.addActionListener(e -> clearFields());

        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = userTable.getSelectedRow();
                if (row != -1) {
                    loadUserToFields(row);
                }
            }
        });
    }

    private void loadUserData() {
        // Check if user has admin permissions
        if (!checkAdminPermission()) {
            return;
        }

        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM users ORDER BY id")) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("username"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("role"));

                Timestamp lastLogin = rs.getTimestamp("last_login");
                row.add(lastLogin != null ? FormatUtils.formatDateTime(lastLogin) : "Chưa đăng nhập");

                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage());
        }
    }

    private void addUser() {
        // Check if user has admin permissions
        if (!checkAdminPermission()) {
            return;
        }

        if (!validateInputs())
            return;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO users (username, password, full_name, role) VALUES (?, ?, ?, ?)")) {

            setStatementParameters(pstmt);
            pstmt.executeUpdate();
            loadUserData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Người dùng đã được thêm thành công!");
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại, vui lòng chọn tên khác!");
            } else {
                JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage());
            }
        }
    }

    private void updateUser() {
        // Check if user has admin permissions
        if (!checkAdminPermission()) {
            return;
        }

        if (!validateInputs())
            return;

        int row = userTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng để cập nhật!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE users SET username=?, password=?, full_name=?, role=? WHERE id=?")) {

            setStatementParameters(pstmt);
            pstmt.setInt(5, (Integer) tableModel.getValueAt(row, 0));

            pstmt.executeUpdate();
            loadUserData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Người dùng đã được cập nhật thành công!");
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại, vui lòng chọn tên khác!");
            } else {
                JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage());
            }
        }
    }

    private void deleteUser() {
        // Check if user has admin permissions
        if (!checkAdminPermission()) {
            return;
        }

        int row = userTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng để xóa!");
            return;
        }

        int userId = (Integer) tableModel.getValueAt(row, 0);

        // Don't allow deleting your own account
        if (userId == com.shopmanager.utils.SessionManager.getCurrentUserId()) {
            JOptionPane.showMessageDialog(this, "Không thể xóa tài khoản của chính bạn!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa người dùng này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE id=?")) {

                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
                loadUserData();
                clearFields();
                JOptionPane.showMessageDialog(this, "Người dùng đã được xóa thành công!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage());
            }
        }
    }

    private boolean validateInputs() {
        if (txtUsername.getText().trim().isEmpty() ||
                txtPassword.getText().trim().isEmpty() ||
                txtFullName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
            return false;
        }

        if (txtUsername.getText().trim().length() < 3) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập phải có ít nhất 3 ký tự!");
            return false;
        }

        if (txtPassword.getText().trim().length() < 4) {
            JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 4 ký tự!");
            return false;
        }

        return true;
    }

    private void setStatementParameters(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, txtUsername.getText().trim());
        pstmt.setString(2, txtPassword.getText().trim());
        pstmt.setString(3, txtFullName.getText().trim());
        pstmt.setString(4, (String) cmbRole.getSelectedItem());
    }

    private void loadUserToFields(int row) {
        if (row != -1) {
            row = userTable.convertRowIndexToModel(row);
            txtUsername.setText((String) tableModel.getValueAt(row, 1));
            // We don't load the password for security reasons
            txtPassword.setText("");
            txtFullName.setText((String) tableModel.getValueAt(row, 2));
            cmbRole.setSelectedItem((String) tableModel.getValueAt(row, 3));
        }
    }

    private void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtFullName.setText("");
        cmbRole.setSelectedIndex(0);
        userTable.clearSelection();
    }
}
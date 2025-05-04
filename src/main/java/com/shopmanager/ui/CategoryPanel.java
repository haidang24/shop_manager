package com.shopmanager.ui;

import com.shopmanager.models.Category;
import com.shopmanager.utils.DatabaseConnection;
import com.shopmanager.ui.components.BasePanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class CategoryPanel extends BasePanel {
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTextField txtName;
    private JTextArea txtDescription;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTextField txtSearch;

    public CategoryPanel() {
        super();
        initComponents();
        loadCategoryData();
    }

    private void initComponents() {
        // Header Panel with Search
        JPanel headerPanel = createHeaderPanel("Quản Lý Danh Mục");

        // Add search field to header
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(backgroundColor);
        txtSearch = createTextField();
        txtSearch.setPreferredSize(new Dimension(250, 35));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm danh mục...");

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
        txtDescription = new JTextArea(3, 20);
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        JScrollPane descriptionScroll = new JScrollPane(txtDescription);
        descriptionScroll.setPreferredSize(new Dimension(300, 80));

        // Add form fields
        addFormField(formPanel, "Tên danh mục:", txtName, gbc, 0, 0);
        addFormField(formPanel, "Mô tả:", descriptionScroll, gbc, 1, 0);

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
        String[] columns = { "ID", "Tên danh mục", "Mô tả" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        categoryTable = createTable();
        categoryTable.setModel(tableModel);

        // Main Layout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(backgroundColor);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(wrapInScrollPane(categoryTable), BorderLayout.CENTER);

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
        categoryTable.setRowSorter(sorter);

        if (searchText.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    private void setupActionListeners() {
        btnAdd.addActionListener(e -> addCategory());
        btnUpdate.addActionListener(e -> updateCategory());
        btnDelete.addActionListener(e -> deleteCategory());
        btnClear.addActionListener(e -> clearFields());

        categoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = categoryTable.getSelectedRow();
                if (row != -1) {
                    loadCategoryToFields(row);
                }
            }
        });
    }

    private void loadCategoryData() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM categories ORDER BY id DESC")) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("description"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage());
        }
    }

    private void addCategory() {
        if (!validateInputs())
            return;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO categories (name, description) VALUES (?, ?)")) {

            setStatementParameters(pstmt);
            pstmt.executeUpdate();
            loadCategoryData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Danh mục đã được thêm thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding category: " + e.getMessage());
        }
    }

    private void updateCategory() {
        if (!validateInputs())
            return;

        int row = categoryTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục để cập nhật!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE categories SET name=?, description=? WHERE id=?")) {

            setStatementParameters(pstmt);
            pstmt.setInt(3, (Integer) tableModel.getValueAt(row, 0));

            pstmt.executeUpdate();
            loadCategoryData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Danh mục đã được cập nhật thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating category: " + e.getMessage());
        }
    }

    private void deleteCategory() {
        int row = categoryTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa danh mục này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement("DELETE FROM categories WHERE id=?")) {

                pstmt.setInt(1, (Integer) tableModel.getValueAt(row, 0));
                pstmt.executeUpdate();
                loadCategoryData();
                clearFields();
                JOptionPane.showMessageDialog(this, "Danh mục đã được xóa thành công!");
            } catch (SQLException e) {
                if (e.getMessage().contains("foreign key constraint")) {
                    JOptionPane.showMessageDialog(this,
                            "Không thể xóa danh mục này vì đang có sản phẩm thuộc danh mục!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting category: " + e.getMessage());
                }
            }
        }
    }

    private boolean validateInputs() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên danh mục!");
            return false;
        }
        return true;
    }

    private void setStatementParameters(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, txtName.getText().trim());
        pstmt.setString(2, txtDescription.getText().trim());
    }

    private void loadCategoryToFields(int row) {
        if (row != -1) {
            row = categoryTable.convertRowIndexToModel(row);
            txtName.setText((String) tableModel.getValueAt(row, 1));
            txtDescription.setText((String) tableModel.getValueAt(row, 2));
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtDescription.setText("");
        categoryTable.clearSelection();
    }
}
package com.shop.ui;

import com.shop.dao.ProductDAO;
import com.shop.dao.ProductDAOImpl;
import com.shop.models.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private final ProductDAO productDAO;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;

    public MainWindow() {
        productDAO = new ProductDAOImpl();
        initializeUI();
        loadProducts();
    }

    private void initializeUI() {
        setTitle("Clothing Shop Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create top panel for search and controls
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchTypeCombo = new JComboBox<>(new String[] { "Name", "Category" });
        JButton searchButton = new JButton("Search");
        JButton addButton = new JButton("Add Product");

        searchButton.addActionListener(this::performSearch);
        addButton.addActionListener(e -> showAddProductDialog());

        topPanel.add(new JLabel("Search by:"));
        topPanel.add(searchTypeCombo);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(addButton);

        // Create table
        String[] columns = { "ID", "Name", "Category", "Price", "Quantity", "Size", "Color" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);

        // Create bottom panel for actions
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        editButton.addActionListener(e -> editSelectedProduct());
        deleteButton.addActionListener(e -> deleteSelectedProduct());

        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);

        // Add components to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        List<Product> products = productDAO.getAll();
        for (Product product : products) {
            Object[] row = {
                    product.getId(),
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getQuantity(),
                    product.getSize(),
                    product.getColor()
            };
            tableModel.addRow(row);
        }
    }

    private void performSearch(ActionEvent e) {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadProducts();
            return;
        }

        List<Product> results;
        if (searchTypeCombo.getSelectedItem().equals("Name")) {
            results = productDAO.searchByName(searchText);
        } else {
            results = productDAO.searchByCategory(searchText);
        }

        tableModel.setRowCount(0);
        for (Product product : results) {
            Object[] row = {
                    product.getId(),
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getQuantity(),
                    product.getSize(),
                    product.getColor()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddProductDialog() {
        ProductDialog dialog = new ProductDialog(this, "Add New Product", null);
        dialog.setVisible(true);
        if (dialog.isProductSaved()) {
            loadProducts();
        }
    }

    private void editSelectedProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to edit", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        Product product = productDAO.getById(productId);
        if (product != null) {
            ProductDialog dialog = new ProductDialog(this, "Edit Product", product);
            dialog.setVisible(true);
            if (dialog.isProductSaved()) {
                loadProducts();
            }
        }
    }

    private void deleteSelectedProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this product?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            productDAO.delete(productId);
            loadProducts();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainWindow().setVisible(true);
        });
    }
}
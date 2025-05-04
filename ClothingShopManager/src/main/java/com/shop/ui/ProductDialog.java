package com.shop.ui;

import com.shop.dao.ProductDAO;
import com.shop.dao.ProductDAOImpl;
import com.shop.models.Product;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ProductDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private final ProductDAO productDAO;
    private final Product productToEdit;
    private boolean productSaved = false;

    // Form fields
    private JTextField nameField;
    private JTextField categoryField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField sizeField;
    private JTextField colorField;

    public ProductDialog(Frame owner, String title, Product product) {
        super(owner, title, true);
        this.productDAO = new ProductDAOImpl();
        this.productToEdit = product;
        initializeUI();
        if (product != null) {
            populateFields();
        }
    }

    private void initializeUI() {
        setSize(400, 300);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add form fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        categoryField = new JTextField(20);
        formPanel.add(categoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        priceField = new JTextField(20);
        formPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        quantityField = new JTextField(20);
        formPanel.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Size:"), gbc);
        gbc.gridx = 1;
        sizeField = new JTextField(20);
        formPanel.add(sizeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Color:"), gbc);
        gbc.gridx = 1;
        colorField = new JTextField(20);
        formPanel.add(colorField, gbc);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(this::saveProduct);
        cancelButton.addActionListener(e -> dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        // Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void populateFields() {
        nameField.setText(productToEdit.getName());
        categoryField.setText(productToEdit.getCategory());
        priceField.setText(String.valueOf(productToEdit.getPrice()));
        quantityField.setText(String.valueOf(productToEdit.getQuantity()));
        sizeField.setText(productToEdit.getSize());
        colorField.setText(productToEdit.getColor());
    }

    private void saveProduct(ActionEvent e) {
        try {
            // Validate input
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());
            String size = sizeField.getText().trim();
            String color = colorField.getText().trim();

            if (name.isEmpty() || category.isEmpty() || size.isEmpty() || color.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "All fields are required",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (price <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Price must be greater than 0",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (quantity < 0) {
                JOptionPane.showMessageDialog(this,
                        "Quantity cannot be negative",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product product;
            if (productToEdit == null) {
                product = new Product(0, name, category, price, quantity, size, color);
                productDAO.add(product);
            } else {
                product = new Product(productToEdit.getId(), name, category, price, quantity, size, color);
                productDAO.update(product);
            }

            productSaved = true;
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid number format for price or quantity",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isProductSaved() {
        return productSaved;
    }
}
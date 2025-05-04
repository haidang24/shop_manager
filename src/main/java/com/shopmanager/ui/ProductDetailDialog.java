package com.shopmanager.ui;

import com.shopmanager.models.Product;
import com.shopmanager.utils.FormatUtils;
import com.shopmanager.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Dialog hiển thị chi tiết sản phẩm bao gồm ảnh
 */
public class ProductDetailDialog extends JDialog {
    private Product product;
    private JLabel imgProduct;
    private JLabel lblName, lblCategory, lblPrice, lblQuantity, lblSize, lblColor;
    private JTextArea txtDescription;

    /**
     * Tạo dialog hiển thị chi tiết sản phẩm
     * 
     * @param parent  Component cha
     * @param product Sản phẩm cần hiển thị
     */
    public ProductDetailDialog(JFrame parent, Product product) {
        super(parent, "Chi tiết sản phẩm", true);
        this.product = product;

        setupUI();
        loadProductData();

        setSize(700, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void setupUI() {
        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Panel ảnh sản phẩm (bên trái)
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);

        imgProduct = new JLabel();
        imgProduct.setPreferredSize(new Dimension(250, 300));
        imgProduct.setHorizontalAlignment(JLabel.CENTER);
        imgProduct.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        imagePanel.add(imgProduct, BorderLayout.CENTER);

        // Panel thông tin sản phẩm (bên phải)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        // Tên sản phẩm
        lblName = new JLabel();
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Giá sản phẩm
        lblPrice = new JLabel();
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPrice.setForeground(new Color(41, 128, 185));
        lblPrice.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Thông tin chi tiết
        JPanel detailsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        detailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel catLabel = new JLabel("Danh mục:");
        catLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCategory = new JLabel();
        lblCategory.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel quantityLabel = new JLabel("Số lượng:");
        quantityLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblQuantity = new JLabel();
        lblQuantity.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel sizeLabel = new JLabel("Kích thước:");
        sizeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSize = new JLabel();
        lblSize.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel colorLabel = new JLabel("Màu sắc:");
        colorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblColor = new JLabel();
        lblColor.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        detailsPanel.add(catLabel);
        detailsPanel.add(lblCategory);
        detailsPanel.add(quantityLabel);
        detailsPanel.add(lblQuantity);
        detailsPanel.add(sizeLabel);
        detailsPanel.add(lblSize);
        detailsPanel.add(colorLabel);
        detailsPanel.add(lblColor);

        // Mô tả sản phẩm
        JLabel descLabel = new JLabel("Mô tả sản phẩm:");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtDescription = new JTextArea();
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescription.setEditable(false);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setBackground(new Color(245, 245, 245));
        txtDescription.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollDescription = new JScrollPane(txtDescription);
        scrollDescription.setPreferredSize(new Dimension(300, 80));
        scrollDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollDescription.setBorder(BorderFactory.createEmptyBorder());

        // Button đóng
        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener((ActionEvent e) -> dispose());
        btnClose.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Thêm các component vào panel thông tin
        infoPanel.add(lblName);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(lblPrice);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(detailsPanel);
        infoPanel.add(descLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(scrollDescription);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(btnClose);

        // Thêm các panel vào panel chính
        mainPanel.add(imagePanel, BorderLayout.WEST);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        // Thêm panel chính vào dialog
        add(mainPanel);
    }

    private void loadProductData() {
        if (product == null) {
            return;
        }

        // Tải và hiển thị ảnh sản phẩm
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            ImageIcon productImage = ImageUtils.loadImageFromResources(product.getImageUrl());
            productImage = ImageUtils.resizeImage(productImage, 250, 300);
            imgProduct.setIcon(productImage);
        } else {
            imgProduct.setIcon(ImageUtils.createPlaceholderImage(250, 300));
        }

        // Hiển thị thông tin sản phẩm
        lblName.setText(product.getName());
        lblPrice.setText(FormatUtils.formatCurrency(product.getPrice()));

        if (product.getCategory() != null) {
            lblCategory.setText(product.getCategory().getName());
        } else {
            lblCategory.setText("Không có danh mục");
        }

        lblQuantity.setText(String.valueOf(product.getQuantity()));
        lblSize.setText(product.getSize());
        lblColor.setText(product.getColor());
        txtDescription.setText(product.getDescription());
    }
}
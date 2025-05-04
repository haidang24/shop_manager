package com.shopmanager.ui;

import com.shopmanager.models.Product;
import com.shopmanager.utils.DatabaseConnection;
import com.shopmanager.ui.components.BasePanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.Vector;
import com.shopmanager.utils.ExcelExporter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;

public class ProductPanel extends BasePanel {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField txtName, txtCategory, txtPrice, txtQuantity, txtSize, txtColor;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnExportExcel, btnChooseImage;
    private JTextField txtSearch;
    private JLabel imageLabel;
    private String currentImageUrl = null;
    private final int IMAGE_WIDTH = 150;
    private final int IMAGE_HEIGHT = 150;
    private final String DEFAULT_IMAGE_URL = "src/main/resources/images/products/no_image.png";
    private final String PRODUCT_IMAGES_DIR = "src/main/resources/images/products/";

    public ProductPanel() {
        super();
        initComponents();
        loadProductData();
    }

    private void initComponents() {
        // Header Panel with Search
        JPanel headerPanel = createHeaderPanel("Quản Lý Sản Phẩm");

        // Add search field to header
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(backgroundColor);
        txtSearch = createTextField();
        txtSearch.setPreferredSize(new Dimension(250, 35));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm sản phẩm...");

        JButton btnSearch = createButton("Tìm Kiếm", primaryColor);
        btnSearch.setPreferredSize(new Dimension(100, 35));

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Form Panel with Image
        JPanel formContainer = new JPanel(new BorderLayout(20, 0));
        formContainer.setBackground(backgroundColor);
        formContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Image Panel on the left
        JPanel imagePanel = new JPanel(new BorderLayout(0, 10));
        imagePanel.setBackground(backgroundColor);
        imagePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Image label for displaying product image
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Set default "no image" image
        setDefaultProductImage();

        // Add click event to image for larger preview
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentImageUrl != null && !currentImageUrl.equals(DEFAULT_IMAGE_URL)) {
                    showLargeImagePreview(currentImageUrl);
                }
            }
        });

        // Button to choose an image
        btnChooseImage = createButton("Chọn ảnh", new Color(52, 152, 219));

        // Add components to image panel
        imagePanel.add(new JLabel("Ảnh sản phẩm:"), BorderLayout.NORTH);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.add(btnChooseImage, BorderLayout.SOUTH);

        // Form Panel for other inputs (on the right)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create form fields
        txtName = createTextField();
        txtCategory = createTextField();
        txtPrice = createTextField();
        txtQuantity = createTextField();
        txtSize = createTextField();
        txtColor = createTextField();

        // Add form fields
        addFormField(formPanel, "Tên sản phẩm:", txtName, gbc, 0, 0);
        addFormField(formPanel, "Loại:", txtCategory, gbc, 0, 1);
        addFormField(formPanel, "Giá:", txtPrice, gbc, 0, 2);
        addFormField(formPanel, "Số lượng:", txtQuantity, gbc, 1, 0);
        addFormField(formPanel, "Kích thước:", txtSize, gbc, 1, 1);
        addFormField(formPanel, "Màu sắc:", txtColor, gbc, 1, 2);

        // Combine image panel and form panel
        formContainer.add(imagePanel, BorderLayout.WEST);
        formContainer.add(formPanel, BorderLayout.CENTER);

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

        // Combine form container and button panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(backgroundColor);
        topPanel.add(formContainer, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Table
        String[] columns = { "ID", "Tên sản phẩm", "Loại", "Giá", "Số lượng", "Kích thước", "Màu sắc", "Ảnh" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = createTable();
        productTable.setModel(tableModel);
        productTable.getColumnModel().getColumn(7).setMinWidth(0);
        productTable.getColumnModel().getColumn(7).setMaxWidth(0);
        productTable.getColumnModel().getColumn(7).setWidth(0);

        // Main Layout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(backgroundColor);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(wrapInScrollPane(productTable), BorderLayout.CENTER);

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
        productTable.setRowSorter(sorter);

        if (searchText.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    private void setupActionListeners() {
        btnAdd.addActionListener(e -> addProduct());
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnClear.addActionListener(e -> clearFields());
        btnExportExcel.addActionListener(e -> exportToExcel());

        // Add action listener for image selection
        btnChooseImage.addActionListener(e -> chooseProductImage());

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = productTable.getSelectedRow();
                if (row != -1) {
                    loadProductToFields(row);
                }
            }
        });
    }

    private void chooseProductImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh sản phẩm");
        fileChooser.setFileFilter(
                new FileNameExtensionFilter("Ảnh (*.jpg, *.jpeg, *.png, *.gif)", "jpg", "jpeg", "png", "gif"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Show preview of selected image
                displayProductImage(selectedFile.getAbsolutePath());
                currentImageUrl = selectedFile.getAbsolutePath();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Không thể tải ảnh: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                setDefaultProductImage();
            }
        }
    }

    private void setDefaultProductImage() {
        try {
            // Check if default image exists, if not create it
            File defaultImage = new File(DEFAULT_IMAGE_URL);
            if (!defaultImage.exists()) {
                createNoImagePlaceholder();
            }
            displayProductImage(DEFAULT_IMAGE_URL);
            currentImageUrl = DEFAULT_IMAGE_URL;
        } catch (Exception e) {
            imageLabel.setText("Không có ảnh");
            currentImageUrl = null;
        }
    }

    private void createNoImagePlaceholder() {
        try {
            // Create directory if it doesn't exist
            new File(PRODUCT_IMAGES_DIR).mkdirs();

            // Create a blank image with "No Image" text
            BufferedImage img = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();

            // Fill background with light gray
            g2d.setColor(new Color(240, 240, 240));
            g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

            // Draw border
            g2d.setColor(Color.GRAY);
            g2d.drawRect(0, 0, IMAGE_WIDTH - 1, IMAGE_HEIGHT - 1);

            // Draw "No Image" text
            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g2d.getFontMetrics();
            String text = "Không có ảnh";
            int textWidth = fm.stringWidth(text);
            g2d.drawString(text, (IMAGE_WIDTH - textWidth) / 2, IMAGE_HEIGHT / 2);

            g2d.dispose();

            // Save the image
            File outputFile = new File(DEFAULT_IMAGE_URL);
            ImageIO.write(img, "png", outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayProductImage(String imageUrl) {
        try {
            ImageIcon originalIcon = new ImageIcon(imageUrl);
            Image originalImage = originalIcon.getImage();

            // Scale image to fit the label while maintaining aspect ratio
            Image scaledImage = getScaledImage(originalImage, IMAGE_WIDTH, IMAGE_HEIGHT);

            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            imageLabel.setIcon(scaledIcon);
            imageLabel.setText(""); // Clear any text
        } catch (Exception e) {
            imageLabel.setIcon(null);
            imageLabel.setText("Lỗi tải ảnh");
            e.printStackTrace();
        }
    }

    private Image getScaledImage(Image srcImg, int targetWidth, int targetHeight) {
        int originalWidth = srcImg.getWidth(null);
        int originalHeight = srcImg.getHeight(null);

        // If image is smaller than target size, return original
        if (originalWidth <= targetWidth && originalHeight <= targetHeight) {
            return srcImg;
        }

        // Calculate new dimensions while preserving aspect ratio
        double ratio = Math.min(
                (double) targetWidth / originalWidth,
                (double) targetHeight / originalHeight);

        int scaledWidth = (int) (originalWidth * ratio);
        int scaledHeight = (int) (originalHeight * ratio);

        BufferedImage resizedImg = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImg.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(srcImg, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        return resizedImg;
    }

    private void showLargeImagePreview(String imageUrl) {
        try {
            JDialog previewDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Xem ảnh sản phẩm",
                    true);
            previewDialog.setLayout(new BorderLayout());

            // Load image
            ImageIcon originalIcon = new ImageIcon(imageUrl);
            Image originalImage = originalIcon.getImage();

            // Scale image to reasonable size if too large
            int maxPreviewWidth = 600;
            int maxPreviewHeight = 600;

            int originalWidth = originalImage.getWidth(null);
            int originalHeight = originalImage.getHeight(null);

            Image displayImage;
            if (originalWidth > maxPreviewWidth || originalHeight > maxPreviewHeight) {
                displayImage = getScaledImage(originalImage, maxPreviewWidth, maxPreviewHeight);
            } else {
                displayImage = originalImage;
            }

            JLabel previewLabel = new JLabel(new ImageIcon(displayImage));
            previewLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JButton closeButton = new JButton("Đóng");
            closeButton.addActionListener(e -> previewDialog.dispose());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(closeButton);

            previewDialog.add(previewLabel, BorderLayout.CENTER);
            previewDialog.add(buttonPanel, BorderLayout.SOUTH);
            previewDialog.pack();
            previewDialog.setLocationRelativeTo(this);
            previewDialog.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Không thể mở ảnh xem trước: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadProductData() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT p.*, c.name as category_name FROM products p " +
                                "LEFT JOIN categories c ON p.category_id = c.id " +
                                "ORDER BY p.id DESC")) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("category_name"));
                row.add(String.format("%,.0f VNĐ", rs.getDouble("price")));
                row.add(rs.getInt("quantity"));
                row.add(rs.getString("size"));
                row.add(rs.getString("color"));
                row.add(rs.getString("image_url")); // Add image url to the last column (hidden)
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage());
        }
    }

    private void addProduct() {
        if (!validateInputs())
            return;

        try (Connection conn = DatabaseConnection.getConnection()) {
            // First, save the image and get the url
            String savedImageUrl = saveProductImage();

            // Find category ID for the category name
            int categoryId = getCategoryId(conn, txtCategory.getText().trim());

            // Insert the product with the category ID and image url
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO products (name, category_id, price, quantity, size, color, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                pstmt.setString(1, txtName.getText().trim());
                pstmt.setInt(2, categoryId);
                pstmt.setDouble(3, Double.parseDouble(txtPrice.getText().trim()));
                pstmt.setInt(4, Integer.parseInt(txtQuantity.getText().trim()));
                pstmt.setString(5, txtSize.getText().trim());
                pstmt.setString(6, txtColor.getText().trim());
                pstmt.setString(7, savedImageUrl);

                pstmt.executeUpdate();
                loadProductData();
                clearFields();
                JOptionPane.showMessageDialog(this, "Sản phẩm đã được thêm thành công!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding product: " + e.getMessage());
        }
    }

    private String saveProductImage() {
        // If no image selected or default image, return null
        if (currentImageUrl == null || currentImageUrl.equals(DEFAULT_IMAGE_URL)) {
            return null;
        }

        try {
            // Create timestamp-based unique filename
            String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String originalFilename = new File(currentImageUrl).getName();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = "product_" + timeStamp + fileExtension;
            String destinationPath = PRODUCT_IMAGES_DIR + newFileName;

            // Create directory if it doesn't exist
            new File(PRODUCT_IMAGES_DIR).mkdirs();

            // Copy the file to products directory
            Path source = Paths.get(currentImageUrl);
            Path destination = Paths.get(destinationPath);
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

            return destinationPath;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Không thể lưu ảnh sản phẩm: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void updateProduct() {
        if (!validateInputs())
            return;

        int row = productTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để cập nhật!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // First, save the image if changed
            String savedImageUrl = saveProductImage();

            // Keep existing image if not changed
            if (savedImageUrl == null) {
                row = productTable.convertRowIndexToModel(row);
                savedImageUrl = (String) tableModel.getValueAt(row, 7); // Get existing image url
            }

            // Find category ID for the category name
            int categoryId = getCategoryId(conn, txtCategory.getText().trim());

            // Update the product with the category ID and image url
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE products SET name=?, category_id=?, price=?, quantity=?, size=?, color=?, image_url=? WHERE id=?")) {
                pstmt.setString(1, txtName.getText().trim());
                pstmt.setInt(2, categoryId);
                pstmt.setDouble(3, Double.parseDouble(txtPrice.getText().trim()));
                pstmt.setInt(4, Integer.parseInt(txtQuantity.getText().trim()));
                pstmt.setString(5, txtSize.getText().trim());
                pstmt.setString(6, txtColor.getText().trim());
                pstmt.setString(7, savedImageUrl);
                row = productTable.convertRowIndexToModel(row);
                pstmt.setInt(8, (Integer) tableModel.getValueAt(row, 0));

                pstmt.executeUpdate();
                loadProductData();
                clearFields();
                JOptionPane.showMessageDialog(this, "Sản phẩm đã được cập nhật thành công!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating product: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        int row = productTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa sản phẩm này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement("DELETE FROM products WHERE id=?")) {

                pstmt.setInt(1, (Integer) tableModel.getValueAt(row, 0));
                pstmt.executeUpdate();
                loadProductData();
                clearFields();
                JOptionPane.showMessageDialog(this, "Sản phẩm đã được xóa thành công!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting product: " + e.getMessage());
            }
        }
    }

    private boolean validateInputs() {
        if (txtName.getText().trim().isEmpty() ||
                txtCategory.getText().trim().isEmpty() ||
                txtPrice.getText().trim().isEmpty() ||
                txtQuantity.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin bắt buộc!");
            return false;
        }

        try {
            double price = Double.parseDouble(txtPrice.getText().trim());
            int quantity = Integer.parseInt(txtQuantity.getText().trim());

            if (price < 0 || quantity < 0) {
                JOptionPane.showMessageDialog(this, "Giá và số lượng không được âm!");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá và số lượng phải là số!");
            return false;
        }

        return true;
    }

    private void setStatementParameters(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, txtName.getText().trim());
        pstmt.setString(2, txtCategory.getText().trim());
        pstmt.setDouble(3, Double.parseDouble(txtPrice.getText().trim()));
        pstmt.setInt(4, Integer.parseInt(txtQuantity.getText().trim()));
        pstmt.setString(5, txtSize.getText().trim());
        pstmt.setString(6, txtColor.getText().trim());
    }

    private void loadProductToFields(int row) {
        if (row != -1) {
            row = productTable.convertRowIndexToModel(row);
            txtName.setText((String) tableModel.getValueAt(row, 1));
            txtCategory.setText((String) tableModel.getValueAt(row, 2));
            String priceStr = (String) tableModel.getValueAt(row, 3);
            txtPrice.setText(priceStr.replaceAll("[^\\d.]", ""));
            txtQuantity.setText(String.valueOf(tableModel.getValueAt(row, 4)));
            txtSize.setText((String) tableModel.getValueAt(row, 5));
            txtColor.setText((String) tableModel.getValueAt(row, 6));

            // Load product image
            String imageUrl = (String) tableModel.getValueAt(row, 7);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    File imageFile = new File(imageUrl);
                    if (imageFile.exists()) {
                        displayProductImage(imageUrl);
                        currentImageUrl = imageUrl;
                    } else {
                        setDefaultProductImage();
                    }
                } catch (Exception e) {
                    setDefaultProductImage();
                }
            } else {
                setDefaultProductImage();
            }
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtCategory.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
        txtSize.setText("");
        txtColor.setText("");
        setDefaultProductImage(); // Reset image to default
        productTable.clearSelection();
    }

    /**
     * Get category ID from the category name.
     * If category doesn't exist, create a new one.
     */
    private int getCategoryId(Connection conn, String categoryName) throws SQLException {
        if (categoryName.isEmpty()) {
            return 0; // No category
        }

        // First, try to find the category
        try (PreparedStatement pstmt = conn.prepareStatement(
                "SELECT id FROM categories WHERE name = ?")) {
            pstmt.setString(1, categoryName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        // If not found, create a new category
        try (PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO categories (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, categoryName);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        }
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
            String fileName = directoryPath + File.separator + "DanhSachSanPham";

            // Xuất file Excel
            boolean success = ExcelExporter.exportToExcel(
                    productTable,
                    fileName,
                    "Danh sách sản phẩm",
                    "DANH SÁCH SẢN PHẨM");

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
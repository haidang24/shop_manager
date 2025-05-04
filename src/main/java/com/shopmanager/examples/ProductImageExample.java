package com.shopmanager.examples;

import com.shopmanager.models.Category;
import com.shopmanager.models.Product;
import com.shopmanager.ui.ProductDetailDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Ví dụ về cách sử dụng ảnh sản phẩm với ProductDetailDialog
 */
public class ProductImageExample {

    /**
     * Hàm main để chạy ví dụ
     */
    public static void main(String[] args) {
        // Đảm bảo giao diện đồng nhất với hệ điều hành
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tạo JFrame để chứa các nút ví dụ
        JFrame frame = new JFrame("Ví dụ hiển thị ảnh sản phẩm");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Tạo các nút để mở các dialog sản phẩm khác nhau
        addProductButton(frame, createSampleShirt());
        addProductButton(frame, createSampleJeans());
        addProductButton(frame, createSampleDress());
        addProductButton(frame, createSampleBag());
        addProductButton(frame, createSampleWithoutImage());

        // Hiển thị frame
        frame.setSize(600, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Thêm nút cho một sản phẩm
     */
    private static void addProductButton(JFrame frame, Product product) {
        JButton button = new JButton(product.getName());
        button.addActionListener((ActionEvent e) -> {
            ProductDetailDialog dialog = new ProductDetailDialog(frame, product);
            dialog.setVisible(true);
        });
        frame.add(button);
    }

    /**
     * Tạo sản phẩm mẫu - áo sơ mi
     */
    private static Product createSampleShirt() {
        Product product = new Product();
        product.setId(1);
        product.setName("Áo sơ mi nam trắng");
        product.setPrice(299000);
        product.setQuantity(50);
        product.setSize("M,L,XL");
        product.setColor("Trắng");
        product.setDescription(
                "Áo sơ mi nam dài tay màu trắng, chất liệu cotton cao cấp, form slim fit, phù hợp mặc đi làm, đi chơi.");
        product.setImageUrl("images/products/ao-so-mi-nam.jpg");

        Category category = new Category();
        category.setId(1);
        category.setName("Áo");
        product.setCategory(category);

        return product;
    }

    /**
     * Tạo sản phẩm mẫu - quần jean
     */
    private static Product createSampleJeans() {
        Product product = new Product();
        product.setId(4);
        product.setName("Quần jean nam slim fit");
        product.setPrice(450000);
        product.setQuantity(45);
        product.setSize("30,31,32,33");
        product.setColor("Xanh đậm");
        product.setDescription("Quần jean nam dáng slim fit, co giãn tốt, màu xanh đậm thời trang, dễ phối đồ.");
        product.setImageUrl("images/products/quan-jean-nam.jpg");

        Category category = new Category();
        category.setId(2);
        category.setName("Quần");
        product.setCategory(category);

        return product;
    }

    /**
     * Tạo sản phẩm mẫu - váy công sở
     */
    private static Product createSampleDress() {
        Product product = new Product();
        product.setId(7);
        product.setName("Váy liền công sở");
        product.setPrice(550000);
        product.setQuantity(25);
        product.setSize("S,M,L");
        product.setColor("Xanh navy");
        product.setDescription("Váy liền công sở thanh lịch, sang trọng, thiết kế hiện đại, tôn dáng người mặc.");
        product.setImageUrl("images/products/vay-lien-cong-so.jpg");

        Category category = new Category();
        category.setId(3);
        category.setName("Váy");
        product.setCategory(category);

        return product;
    }

    /**
     * Tạo sản phẩm mẫu - túi xách
     */
    private static Product createSampleBag() {
        Product product = new Product();
        product.setId(12);
        product.setName("Túi xách nữ");
        product.setPrice(550000);
        product.setQuantity(20);
        product.setSize("Free size");
        product.setColor("Đen");
        product.setDescription("Túi xách nữ thời trang, sang trọng, chất liệu da cao cấp, nhiều ngăn tiện dụng.");
        product.setImageUrl("images/products/tui-xach-nu.jpg");

        Category category = new Category();
        category.setId(4);
        category.setName("Phụ kiện");
        product.setCategory(category);

        return product;
    }

    /**
     * Tạo sản phẩm mẫu - không có ảnh
     */
    private static Product createSampleWithoutImage() {
        Product product = new Product();
        product.setId(99);
        product.setName("Sản phẩm không có ảnh");
        product.setPrice(350000);
        product.setQuantity(10);
        product.setSize("S,M,L");
        product.setColor("Đen, Xám");
        product.setDescription("Đây là một sản phẩm mẫu không có ảnh, hệ thống sẽ hiển thị ảnh placeholder.");
        product.setImageUrl(""); // Không có ảnh

        Category category = new Category();
        category.setId(1);
        category.setName("Áo");
        product.setCategory(category);

        return product;
    }
}
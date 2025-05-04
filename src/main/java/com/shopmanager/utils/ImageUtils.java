package com.shopmanager.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Lớp tiện ích để xử lý và hiển thị ảnh trong ứng dụng
 */
public class ImageUtils {

    /**
     * Tải ảnh từ resources
     * 
     * @param path Đường dẫn tới ảnh trong thư mục resources
     * @return ImageIcon nếu tải thành công, null nếu không tìm thấy ảnh
     */
    public static ImageIcon loadImageFromResources(String path) {
        try {
            // Lấy URL từ resources
            URL imageUrl = ImageUtils.class.getClassLoader().getResource(path);
            if (imageUrl == null) {
                System.err.println("Không tìm thấy ảnh: " + path);
                return createPlaceholderImage(150, 150);
            }
            return new ImageIcon(imageUrl);
        } catch (Exception e) {
            System.err.println("Lỗi tải ảnh: " + e.getMessage());
            return createPlaceholderImage(150, 150);
        }
    }

    /**
     * Tạo một ảnh đại diện đơn giản với biểu tượng "No Image"
     * 
     * @param width  Chiều rộng ảnh
     * @param height Chiều cao ảnh
     * @return ImageIcon đại diện
     */
    public static ImageIcon createPlaceholderImage(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Tô màu nền xám nhạt
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, width, height);

        // Vẽ viền
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawRect(0, 0, width - 1, height - 1);

        // Vẽ chữ "No Image"
        g2d.setColor(new Color(150, 150, 150));
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "No Image";
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text, (width - textWidth) / 2, height / 2);

        g2d.dispose();
        return new ImageIcon(img);
    }

    /**
     * Thay đổi kích thước ảnh
     * 
     * @param icon   ImageIcon cần thay đổi kích thước
     * @param width  Chiều rộng mong muốn
     * @param height Chiều cao mong muốn
     * @return ImageIcon đã thay đổi kích thước
     */
    public static ImageIcon resizeImage(ImageIcon icon, int width, int height) {
        if (icon == null) {
            return createPlaceholderImage(width, height);
        }

        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}
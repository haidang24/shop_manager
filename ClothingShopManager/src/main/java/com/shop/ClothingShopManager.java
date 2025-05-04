package com.shop;

import com.shop.ui.MainWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ClothingShopManager {
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
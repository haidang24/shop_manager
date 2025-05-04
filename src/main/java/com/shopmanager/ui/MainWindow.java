package com.shopmanager.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.shopmanager.utils.ImageUtils;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainWindow extends JFrame {
    private JPanel mainPanel;
    private JPanel menuPanel;
    private CardLayout cardLayout;

    // Modern color scheme
    private Color primaryColor = new Color(113, 88, 226); // Purple primary color
    private Color accentColor = new Color(75, 192, 192); // Teal accent color
    private Color menuBackground = new Color(48, 57, 82); // Dark blue background
    private Color menuHoverBackground = new Color(59, 70, 100);
    private Color menuSelectedBackground = new Color(113, 88, 226);
    private Font menuFont = new Font("Segoe UI", Font.PLAIN, 14);

    // Header colors
    private Color headerBackground = new Color(255, 255, 255);
    private Color headerBorderColor = new Color(240, 240, 240);

    // User authentication fields
    private int userId;
    private String userName;
    private String userRole;
    private JLabel userInfoLabel;
    private JLabel dateTimeLabel;
    private Timer clockTimer;
    private JLabel pageTitleLabel;

    // Cache for icons to avoid recreating them
    private Map<String, ImageIcon> iconCache = new HashMap<>();

    // Constructors
    public MainWindow() {
        this(0, "Guest", "guest");
    }

    public MainWindow(int userId, String userName, String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;

        setTitle("Quản Lý Shop Quần Áo");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize components
        initComponents();
        startClock();
    }

    private void startClock() {
        clockTimer = new Timer(1000, e -> {
            if (dateTimeLabel != null) {
                dateTimeLabel.setText(new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy").format(new Date()));
            }
        });
        clockTimer.start();
    }

    private void initComponents() {
        // Main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Menu panel (Left)
        menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(260, 0));
        menuPanel.setBackground(menuBackground);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        // Add logo/title panel with icon
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(new Color(41, 50, 74)); // Darker than menu background
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // Create logo with gradient text
        JLabel logoLabel = new JLabel("SHOP MANAGER");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logoLabel.setForeground(Color.WHITE);

        // Tải logo từ thư mục resources thay vì tạo logo mặc định
        ImageIcon logoIcon = ImageUtils.loadImageFromResources("images/ui/shop_logo.png");
        if (logoIcon != null) {
            logoIcon = ImageUtils.resizeImage(logoIcon, 45, 45);
        } else {
            // Sử dụng logo mặc định nếu không tìm thấy hình ảnh
            logoIcon = createDefaultLogoIcon(45, 45);
        }
        logoLabel.setIcon(logoIcon);
        logoLabel.setIconTextGap(15);

        logoPanel.add(logoLabel);
        menuPanel.add(logoPanel);

        // Add a small separator after logo panel
        JSeparator logoSeparator = new JSeparator();
        logoSeparator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        logoSeparator.setForeground(new Color(70, 80, 110));
        menuPanel.add(logoSeparator);

        // Add space after separator - increased to provide better spacing now that
        // profile is removed
        menuPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Content panel (Center) with CardLayout
        cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Create modern header panel with shadow
        JPanel headerPanel = createModernHeader();

        // Create menu items and panels
        addMenuItem("Trang Chủ", "home", createMenuIcon("home"), contentPanel, new HomePanel());
        addMenuItem("Sản Phẩm", "products", createMenuIcon("product"), contentPanel, new ProductPanel());
        addMenuItem("Danh Mục", "categories", createMenuIcon("category"), contentPanel, new CategoryPanel());
        addMenuItem("Đơn Hàng", "orders", createMenuIcon("order"), contentPanel, new OrderPanel());
        addMenuItem("Khách Hàng", "customers", createMenuIcon("customer"), contentPanel, new CustomerPanel());
        addMenuItem("Nhà Cung Cấp", "suppliers", createMenuIcon("supplier"), contentPanel, new SupplierPanel());
        addMenuItem("Nhập Hàng", "inventory", createMenuIcon("inventory"), contentPanel, new InventoryPanel());
        addMenuItem("Thống Kê", "statistics", createMenuIcon("statistics"), contentPanel, new StatisticsPanel());

        // Only show UserPanel for admin users
        if ("admin".equals(userRole)) {
            addMenuItem("Người Dùng", "users", createMenuIcon("user"), contentPanel, new UserPanel());
        }

        // Add spacer before logout
        menuPanel.add(Box.createVerticalGlue());

        // Add logout to menu
        addLogoutMenuItem();

        // Create and add footer panel with copyright
        JPanel footerPanel = createFooterPanel();

        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        setContentPane(mainPanel);
    }

    private JPanel createModernHeader() {
        // Create main header panel with gradient background
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Create a subtle gradient background for header
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(255, 255, 255),
                        0, getHeight(), new Color(248, 250, 252));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };

        // Add light shadow border at bottom
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(230, 232, 235)),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        headerPanel.setPreferredSize(new Dimension(0, 68));

        // Page title with modern font and styling
        pageTitleLabel = new JLabel("Trang Chủ");
        pageTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));

        // Create gradient effect for page title
        GradientPaint textGradient = new GradientPaint(
                0, 0, new Color(113, 88, 226),
                0, 30, new Color(96, 70, 200));

        pageTitleLabel.setForeground(new Color(113, 88, 226));
        pageTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));

        // Create a modern search bar with rounded corners
        JTextField searchField = new JTextField(22);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm...");
        searchField.setPreferredSize(new Dimension(250, 38));

        // Create rounded search panel with background
        JPanel searchBarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(245, 245, 250));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(new Color(230, 230, 235));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();
            }
        };
        searchBarPanel.setLayout(new BorderLayout());
        searchBarPanel.setOpaque(false);

        // Search icon with better styling
        JLabel searchIcon = new JLabel();
        searchIcon.setIcon(createHeaderIcon("search", 18, new Color(130, 130, 130)));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 5));

        // Add search button with hover effect
        JButton searchButton = new JButton("Tìm");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(primaryColor);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect to search button
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(new Color(100, 77, 210));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                searchButton.setBackground(primaryColor);
            }
        });

        // Make search field transparent to let the rounded panel show through
        searchField.setOpaque(false);
        searchField.setBackground(new Color(0, 0, 0, 0));

        searchBarPanel.add(searchIcon, BorderLayout.WEST);
        searchBarPanel.add(searchField, BorderLayout.CENTER);
        searchBarPanel.add(searchButton, BorderLayout.EAST);

        // Create search container with padding
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
        searchPanel.setOpaque(false);
        searchPanel.add(searchBarPanel);

        // Improved left header panel with better spacing
        JPanel leftHeaderPanel = new JPanel(new BorderLayout(20, 0));
        leftHeaderPanel.setOpaque(false);
        leftHeaderPanel.add(pageTitleLabel, BorderLayout.WEST);
        leftHeaderPanel.add(searchPanel, BorderLayout.CENTER);

        // Right header panel with datetime and user actions - styled better
        JPanel rightHeaderPanel = new JPanel();
        rightHeaderPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 16));
        rightHeaderPanel.setOpaque(false);
        rightHeaderPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 25));

        // Date and time with better styling
        dateTimeLabel = new JLabel(new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy").format(new Date()));
        dateTimeLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        dateTimeLabel.setForeground(new Color(90, 90, 90));

        // Create a panel to hold the dateTimeLabel with a subtle background
        JPanel dateTimePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(245, 245, 250));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
            }
        };
        dateTimePanel.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        dateTimePanel.setOpaque(false);
        dateTimePanel.add(dateTimeLabel);

        // Add separator between datetime and buttons
        JSeparator dateSeparator = new JSeparator(JSeparator.VERTICAL);
        dateSeparator.setPreferredSize(new Dimension(1, 25));
        dateSeparator.setForeground(new Color(220, 220, 220));

        // Improved header buttons - make them more visually appealing
        JButton notificationsBtn = createModernHeaderButton(createHeaderIcon("bell", 18, new Color(80, 80, 80)),
                "Thông báo");
        JButton settingsBtn = createModernHeaderButton(createHeaderIcon("settings", 18, new Color(80, 80, 80)),
                "Cài đặt");

        // Stylish logout button
        JButton userMenuBtn = new JButton("Đăng xuất");
        userMenuBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userMenuBtn.setForeground(Color.WHITE);
        userMenuBtn.setBackground(new Color(113, 88, 226));
        userMenuBtn.setFocusPainted(false);
        userMenuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        userMenuBtn.setPreferredSize(new Dimension(100, 36));

        // Make logout button rounded with modern styling
        userMenuBtn.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        userMenuBtn.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw rounded rectangle button
                g2d.setColor(c.getBackground());
                g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 18, 18);

                super.paint(g2d, c);
                g2d.dispose();
            }
        });

        // Hover effect for logout button
        userMenuBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                userMenuBtn.setBackground(new Color(100, 77, 210));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                userMenuBtn.setBackground(new Color(113, 88, 226));
            }
        });

        userMenuBtn.addActionListener(e -> logout());

        // Add components to right panel
        rightHeaderPanel.add(dateTimePanel);
        rightHeaderPanel.add(dateSeparator);
        rightHeaderPanel.add(notificationsBtn);
        rightHeaderPanel.add(settingsBtn);
        rightHeaderPanel.add(userMenuBtn);

        // Add both panels to header
        headerPanel.add(leftHeaderPanel, BorderLayout.WEST);
        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JButton createModernHeaderButton(ImageIcon icon, String tooltip) {
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add a modern hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(3, 3, 3, 3),
                        BorderFactory.createLineBorder(new Color(230, 230, 235), 1, true)));
                button.setBackground(new Color(245, 245, 250));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(null);
                button.setBackground(null);
            }
        });

        return button;
    }

    private ImageIcon createDefaultLogoIcon(int width, int height) {
        // Create a rounded rectangle image with the app's primary color
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create gradient background
        GradientPaint gradient = new GradientPaint(0, 0, primaryColor, width, height, accentColor);
        g2d.setPaint(gradient);

        // Draw rounded rectangle
        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, 15, 15));

        // Draw "SM" text in the center
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "SM";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g2d.drawString(text, (width - textWidth) / 2, height / 2 + textHeight / 4);

        g2d.dispose();
        return new ImageIcon(img);
    }

    private ImageIcon createCircularAvatar(int width, int height) {
        // Create a circular gradient avatar
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create gradient background
        GradientPaint gradient = new GradientPaint(0, 0, primaryColor, width, height, accentColor);
        g2d.setPaint(gradient);

        // Draw filled circle with border
        g2d.fillOval(0, 0, width, height);

        // Add a subtle white border
        g2d.setColor(new Color(255, 255, 255, 80));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(1, 1, width - 3, height - 3);

        // Draw user silhouette
        g2d.setColor(new Color(255, 255, 255, 180));

        // Head
        g2d.fillOval(width / 3, height / 5, width / 3, width / 3);

        // Body
        g2d.fillOval(width / 4, height / 2, width / 2, height / 2);

        g2d.dispose();
        return new ImageIcon(img);
    }

    private JButton createHeaderButton(ImageIcon icon, String tooltip) {
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void addMenuItem(String text, String name, ImageIcon icon, JPanel contentPanel, JPanel panel) {
        JPanel menuItem = createMenuItem(icon, text);
        menuItem.setName(name);
        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMenuItem(menuItem);
                cardLayout.show(contentPanel, name);
                updatePageTitle(text);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!menuSelectedBackground.equals(menuItem.getBackground())) {
                    menuItem.setBackground(menuHoverBackground);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!menuSelectedBackground.equals(menuItem.getBackground())) {
                    menuItem.setBackground(menuBackground);
                }
            }
        });

        menuPanel.add(menuItem);
        contentPanel.add(panel, name);

        // Select first item by default
        if (name.equals("home")) {
            selectMenuItem(menuItem);
        }
    }

    private void updatePageTitle(String title) {
        if (pageTitleLabel != null) {
            pageTitleLabel.setText(title);
        }
    }

    private JPanel createMenuItem(ImageIcon icon, String text) {
        JPanel menuItem = new JPanel(new BorderLayout());
        menuItem.setBackground(menuBackground);
        menuItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        menuItem.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // Create icon label with increased size
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setPreferredSize(new Dimension(30, 30));
        menuItem.add(iconLabel, BorderLayout.WEST);

        // Create text label
        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textLabel.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
        menuItem.add(textLabel, BorderLayout.CENTER);

        return menuItem;
    }

    private void selectMenuItem(JPanel selectedItem) {
        // Reset all menu items
        for (Component comp : menuPanel.getComponents()) {
            if (comp instanceof JPanel && comp.getName() != null && !comp.getName().equals("logoPanel")) {
                comp.setBackground(menuBackground);
            }
        }

        // Highlight selected item
        selectedItem.setBackground(menuSelectedBackground);
    }

    private void addLogoutMenuItem() {
        JPanel menuItem = createMenuItem(createMenuIcon("logout"), "Đăng Xuất");
        menuItem.setName("logout");
        menuItem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(70, 80, 110)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));
        menuItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logout();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                menuItem.setBackground(menuHoverBackground);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuItem.setBackground(menuBackground);
            }
        });

        // Create padding at bottom
        JPanel paddingPanel = new JPanel();
        paddingPanel.setBackground(menuBackground);
        paddingPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        paddingPanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 20));
        paddingPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 20));

        menuPanel.add(menuItem);
        menuPanel.add(paddingPanel);
    }

    private void logout() {
        if (clockTimer != null) {
            clockTimer.stop();
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            // End user session
            com.shopmanager.utils.SessionManager.endSession();

            dispose(); // Close main window

            // Open login form
            SwingUtilities.invokeLater(() -> {
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            });
        }
    }

    // Helper methods to create custom icons instead of using Unicode characters
    private ImageIcon createMenuIcon(String iconType) {
        // Check if icon is already in cache
        String cacheKey = iconType + "_menu";
        if (iconCache.containsKey(cacheKey)) {
            return iconCache.get(cacheKey);
        }

        int size = 24;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);

        // Draw different icon based on type
        switch (iconType) {
            case "home":
                // Home icon
                int[] xPoints = { 2, 12, 22, 18, 18, 14, 14, 10, 10, 6, 6, 2 };
                int[] yPoints = { 12, 2, 12, 12, 22, 22, 15, 15, 22, 22, 12, 12 };
                g2d.fillPolygon(xPoints, yPoints, xPoints.length);
                break;
            case "product":
                // Product/clothing icon
                g2d.fillRoundRect(4, 2, 16, 4, 2, 2); // Collar
                g2d.fillRect(7, 6, 10, 16); // Body
                g2d.fillRect(2, 6, 5, 10); // Left sleeve
                g2d.fillRect(17, 6, 5, 10); // Right sleeve
                break;
            case "category":
                // Category/folder icon
                g2d.fillRoundRect(2, 7, 20, 15, 3, 3); // Folder body
                g2d.fillRoundRect(5, 3, 10, 6, 2, 2); // Folder tab
                break;
            case "order":
                // Order/document icon
                g2d.fillRoundRect(4, 2, 16, 20, 2, 2); // Document
                g2d.fillRect(8, 8, 12, 1); // Line 1
                g2d.fillRect(8, 12, 12, 1); // Line 2
                g2d.fillRect(8, 16, 8, 1); // Line 3
                break;
            case "customer":
                // Customer/people icon
                g2d.fillOval(6, 3, 6, 6); // Head 1
                g2d.fillRect(6, 9, 6, 9); // Body 1
                g2d.fillOval(12, 3, 6, 6); // Head 2
                g2d.fillRect(12, 9, 6, 9); // Body 2
                break;
            case "supplier":
                // Supplier/store icon
                g2d.fillRect(2, 10, 20, 12); // Building base
                g2d.fillPolygon(new int[] { 2, 12, 22 }, new int[] { 10, 2, 10 }, 3); // Roof
                g2d.setColor(menuBackground);
                g2d.fillRect(8, 14, 8, 8); // Door (negative space)
                g2d.setColor(Color.WHITE);
                g2d.fillRect(11, 14, 2, 8); // Door handle
                break;
            case "inventory":
                // Inventory/box icon
                g2d.fillPolygon(new int[] { 2, 22, 22, 2 }, new int[] { 8, 8, 22, 22 }, 4); // Box body
                g2d.fillPolygon(new int[] { 2, 12, 22, 12 }, new int[] { 8, 2, 8, 14 }, 4); // Box top
                break;
            case "statistics":
                // Statistics/chart icon
                g2d.fillRect(3, 2, 3, 20); // Y-axis
                g2d.fillRect(3, 19, 19, 3); // X-axis
                g2d.fillRect(8, 12, 4, 7); // Bar 1
                g2d.fillRect(14, 8, 4, 11); // Bar 2
                g2d.fillRect(20, 4, 4, 15); // Bar 3
                break;
            case "user":
                // User icon
                g2d.fillOval(8, 2, 8, 8); // Head
                g2d.fillPolygon(new int[] { 8, 16, 20, 4 }, new int[] { 10, 10, 22, 22 }, 4); // Body
                break;
            case "logout":
                // Logout icon
                g2d.fillRect(4, 4, 12, 3); // Top
                g2d.fillRect(4, 4, 3, 16); // Left side
                g2d.fillRect(4, 17, 12, 3); // Bottom
                g2d.fillPolygon(new int[] { 16, 22, 16 }, new int[] { 8, 12, 16 }, 3); // Arrow
                g2d.fillRect(12, 11, 6, 2); // Arrow base
                break;
            default:
                // Default dot icon
                g2d.fillOval(8, 8, 8, 8);
        }

        g2d.dispose();
        ImageIcon icon = new ImageIcon(img);
        iconCache.put(cacheKey, icon);
        return icon;
    }

    private ImageIcon createHeaderIcon(String iconType, int size, Color color) {
        // Check if icon is already in cache
        String cacheKey = iconType + "_header_" + color.getRGB();
        if (iconCache.containsKey(cacheKey)) {
            return iconCache.get(cacheKey);
        }

        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);

        switch (iconType) {
            case "search":
                // Search icon
                g2d.fillOval(2, 2, 8, 8); // Circle
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(9, 9, 14, 14); // Handle
                g2d.setColor(headerBackground);
                g2d.fillOval(3, 3, 6, 6); // Inner circle (to create a ring)
                break;
            case "bell":
                // Notification bell icon
                g2d.fillOval(5, 1, 6, 2); // Top of bell
                g2d.fillOval(3, 3, 10, 10); // Bell body
                g2d.fillRect(3, 8, 10, 5); // Bell bottom
                g2d.fillRect(7, 13, 2, 3); // Bell clapper
                g2d.setColor(headerBackground);
                g2d.fillOval(5, 5, 6, 6); // Inner circle (to create a ring)
                break;
            case "settings":
                // Settings gear icon
                for (int i = 0; i < 8; i++) {
                    double angle = Math.PI * i / 4;
                    int x1 = (int) (8 + 6 * Math.cos(angle));
                    int y1 = (int) (8 + 6 * Math.sin(angle));
                    int x2 = (int) (8 + 7 * Math.cos(angle));
                    int y2 = (int) (8 + 7 * Math.sin(angle));
                    g2d.fillOval(x1 - 2, y1 - 2, 4, 4); // Gear teeth
                }
                g2d.fillOval(5, 5, 6, 6); // Gear center
                g2d.setColor(headerBackground);
                g2d.fillOval(7, 7, 2, 2); // Center hole
                break;
            default:
                // Default dot icon
                g2d.fillOval(4, 4, 8, 8);
        }

        g2d.dispose();
        ImageIcon icon = new ImageIcon(img);
        iconCache.put(cacheKey, icon);
        return icon;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));

        JLabel copyrightLabel = new JLabel("© 2025 AT19N0105_MaiHaiDang. All rights reserved.");
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(120, 120, 120));

        footerPanel.add(copyrightLabel);
        return footerPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Launch the login form as the entry point
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}
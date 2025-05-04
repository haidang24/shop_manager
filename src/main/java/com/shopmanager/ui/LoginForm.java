package com.shopmanager.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.shopmanager.utils.DatabaseConnection;
import com.shopmanager.utils.ImageUtils;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.awt.image.BufferedImage;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblStatus;
    private JLabel lblForgotPassword;
    private JLabel lblAvatar;
    private Timer colorAnimationTimer;
    private final int ANIMATION_DURATION = 10000; // 10 seconds for gradient animation cycle
    private float animationProgress = 0.0f;

    // Modern color scheme
    private final Color primaryColor = new Color(113, 88, 226); // Purple primary color
    private final Color accentColor = new Color(75, 192, 192); // Teal accent color
    private final Color successColor = new Color(76, 175, 80); // Green for login button
    private final Color backgroundColor = Color.WHITE;
    private final Color textFieldBgColor = new Color(245, 245, 247);
    private final Color textColor = new Color(50, 50, 50);
    private final Color shadowColor = new Color(0, 0, 0, 20);
    private final Color shadowColorDarker = new Color(0, 0, 0, 40);

    // Parallax effect properties
    private Point mousePosition = new Point(0, 0);
    private final float PARALLAX_STRENGTH = 0.02f;

    // Gradient colors - we'll cycle through these
    private final Color[] gradientColors = {
            new Color(75, 192, 192), // Teal
            new Color(113, 88, 226), // Purple
            new Color(255, 99, 132), // Pink
            new Color(54, 162, 235), // Blue
            new Color(113, 88, 226) // Back to purple to complete cycle
    };

    public LoginForm() {
        // Set window properties
        setTitle("Đăng Nhập - Shop Manager");
        setSize(480, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true); // Remove window decorations for modern look

        // Set rounded corners for the window with larger radius
        setShape(new RoundRectangle2D.Double(0, 0, 480, 600, 30, 30));

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
        startColorAnimation();
    }

    private void startColorAnimation() {
        // Create a timer for gradient animation
        colorAnimationTimer = new Timer(40, e -> {
            animationProgress = (animationProgress + 0.002f) % 1.0f;
            repaint();
        });
        colorAnimationTimer.start();
    }

    private void initComponents() {
        // Main panel with animated gradient background and parallax effect
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Apply parallax offset based on mouse position
                int parallaxX = (int) ((mousePosition.x - getWidth() / 2) * PARALLAX_STRENGTH);
                int parallaxY = (int) ((mousePosition.y - getHeight() / 2) * PARALLAX_STRENGTH);

                // Calculate color based on animation progress
                Color currentColor = getGradientColor(animationProgress);
                Color nextColor = getGradientColor((animationProgress + 0.25f) % 1.0f);

                // Create animated gradient with parallax
                GradientPaint gp = new GradientPaint(
                        parallaxX, parallaxY, currentColor,
                        getWidth() + parallaxX, getHeight() + parallaxY, nextColor);

                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Add subtle pattern overlay for texture with parallax
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f));
                g2d.setColor(Color.WHITE);

                int dotSize = 2;
                int spacing = 15;
                for (int x = parallaxX % spacing; x < getWidth(); x += spacing) {
                    for (int y = parallaxY % spacing; y < getHeight(); y += spacing) {
                        g2d.fillOval(x, y, dotSize, dotSize);
                    }
                }

                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Login card panel (white card on gradient background)
        JPanel loginCardPanel = new JPanel();
        loginCardPanel.setLayout(new BoxLayout(loginCardPanel, BoxLayout.Y_AXIS));
        loginCardPanel.setBackground(backgroundColor);
        loginCardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCardPanel.setMaximumSize(new Dimension(380, 450));

        // Custom rounded border with shadow effect
        loginCardPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        loginCardPanel.setBorder(new ShadowRoundedBorder(backgroundColor, 15, 10));

        // Shop Logo/Name at the top
        JLabel lblLogo = new JLabel("SHOP MANAGER");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogo.setForeground(primaryColor);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle/Slogan
        JLabel lblSubtitle = new JLabel("Quản lý dễ dàng, kinh doanh hiệu quả");
        lblSubtitle.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblSubtitle.setForeground(new Color(150, 150, 150));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // User Avatar
        lblAvatar = new JLabel();
        ImageIcon avatarIcon = ImageUtils.loadImageFromResources("images/ui/avatar.png");
        if (avatarIcon != null) {
            avatarIcon = ImageUtils.resizeImage(avatarIcon, 120, 120); // Increased size from 100 to 120
            lblAvatar.setIcon(avatarIcon);
        } else {
            // Create a circular avatar placeholder if image is not found
            lblAvatar.setIcon(createCircularAvatar(120, 120)); // Increased size from 100 to 120
        }
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Login Title
        JLabel lblLoginTitle = new JLabel("Đăng nhập");
        lblLoginTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLoginTitle.setForeground(textColor);
        lblLoginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username field with icon
        JPanel usernamePanel = createTextFieldWithIcon("UserName", "Tên đăng nhập", txtUsername = new JTextField());

        // Password field with icon
        JPanel passwordPanel = createTextFieldWithIcon("Password", "Mật khẩu", txtPassword = new JPasswordField());
        txtPassword.setEchoChar('●');

        // Remember me checkbox
        JPanel rememberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rememberPanel.setBackground(backgroundColor);
        rememberPanel.setMaximumSize(new Dimension(350, 30));
        rememberPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JCheckBox rememberMe = new JCheckBox("Nhớ đăng nhập");
        rememberMe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rememberMe.setForeground(new Color(100, 100, 100));
        rememberMe.setBackground(backgroundColor);
        rememberPanel.add(rememberMe);

        // Login button
        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setBackground(successColor);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(350, 48));

        // Make login button rounded with hover effect
        btnLogin.setBorder(new EmptyBorder(12, 20, 12, 20));
        btnLogin.putClientProperty("JButton.buttonType", "roundRect");

        // Add hover effect
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(69, 160, 73)); // Darker green on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(successColor);
            }
        });

        // Forgot password link
        lblForgotPassword = new JLabel("Quên mật khẩu?");
        lblForgotPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblForgotPassword.setForeground(primaryColor);
        lblForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblForgotPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Underline on hover
        lblForgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Font font = lblForgotPassword.getFont();
                Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                lblForgotPassword.setFont(font.deriveFont(attributes));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblForgotPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            }
        });

        // Status label for error messages
        lblStatus = new JLabel("");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setForeground(new Color(255, 89, 89));
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to login card with spacing
        loginCardPanel.add(Box.createVerticalGlue());
        loginCardPanel.add(lblLogo);
        loginCardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        loginCardPanel.add(lblSubtitle);
        loginCardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        loginCardPanel.add(lblAvatar);
        loginCardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        loginCardPanel.add(lblLoginTitle);
        loginCardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        loginCardPanel.add(usernamePanel);
        loginCardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        loginCardPanel.add(passwordPanel);
        loginCardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginCardPanel.add(rememberPanel);
        loginCardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        loginCardPanel.add(btnLogin);
        loginCardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        loginCardPanel.add(lblForgotPassword);
        loginCardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginCardPanel.add(lblStatus);
        loginCardPanel.add(Box.createVerticalGlue());

        // Window control buttons in the top-right corner
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JPanel controlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        controlButtons.setOpaque(false);

        // Minimize button
        JButton minimizeButton = createControlButton("—");
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));

        // Close button
        JButton closeButton = createControlButton("×");
        closeButton.addActionListener(e -> {
            if (colorAnimationTimer != null)
                colorAnimationTimer.stop();
            System.exit(0);
        });

        controlButtons.add(minimizeButton);
        controlButtons.add(closeButton);
        topBar.add(controlButtons, BorderLayout.EAST);

        // Add panels to main panel
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(topBar);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(loginCardPanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Add main panel to frame
        add(mainPanel);

        // Add action listeners
        btnLogin.addActionListener(e -> login());
        lblForgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginForm.this,
                        "Vui lòng liên hệ với quản trị viên để đặt lại mật khẩu.",
                        "Quên mật khẩu",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add Enter key listener to username and password fields
        ActionListener loginAction = e -> login();
        txtUsername.addActionListener(loginAction);
        txtPassword.addActionListener(loginAction);

        // Make the window draggable
        addWindowDragListener(mainPanel);
    }

    private JButton createControlButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(30, 30));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (text.equals("×")) {
                    button.setForeground(new Color(255, 89, 89)); // Red for close
                } else {
                    button.setForeground(new Color(220, 220, 220)); // Light gray for minimize
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }

    private JPanel createTextFieldWithIcon(String icon, String placeholder, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(backgroundColor);
        panel.setMaximumSize(new Dimension(350, 48));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Icon label
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        iconLabel.setForeground(new Color(120, 120, 120));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(40, 40));

        // Style the text field
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textField.setBackground(textFieldBgColor);
        textField.putClientProperty("JTextField.placeholderText", placeholder);

        // Create a rounded panel with hover and focus effects
        JPanel container = new JPanel(new BorderLayout()) {
            private boolean isHovered = false;
            private boolean isFocused = false;
            private final Timer transitionTimer = new Timer(16, null);
            private float alpha = 0.0f;

            {
                setBackground(textFieldBgColor);
                setBorder(new RoundedBorder(textFieldBgColor, 10));

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        startAnimation();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        startAnimation();
                    }
                });

                textField.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        isFocused = true;
                        startAnimation();
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        isFocused = false;
                        startAnimation();
                    }
                });

                transitionTimer.addActionListener(e -> {
                    float targetAlpha = (isHovered || isFocused) ? 1.0f : 0.0f;
                    alpha += (targetAlpha - alpha) * 0.2f;

                    if (Math.abs(targetAlpha - alpha) < 0.01f) {
                        alpha = targetAlpha;
                        transitionTimer.stop();
                    }

                    repaint();
                });
            }

            private void startAnimation() {
                if (!transitionTimer.isRunning()) {
                    transitionTimer.start();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw hover/focus effect
                if (alpha > 0) {
                    g2d.setColor(new Color(primaryColor.getRed(), primaryColor.getGreen(),
                            primaryColor.getBlue(), (int) (alpha * 40)));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                }

                g2d.dispose();
            }
        };

        container.add(iconLabel, BorderLayout.WEST);
        container.add(textField, BorderLayout.CENTER);

        panel.add(container, BorderLayout.CENTER);

        return panel;
    }

    private void addWindowDragListener(JPanel panel) {
        final Point offset = new Point();

        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                offset.setLocation(e.getPoint());
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currentLocation = getLocation();
                setLocation(
                        currentLocation.x + e.getX() - offset.x,
                        currentLocation.y + e.getY() - offset.y);
            }
        });
    }

    private Color getGradientColor(float position) {
        // Find the two colors to interpolate between
        float totalColors = gradientColors.length - 1;
        float scaledPosition = position * totalColors;
        int index = (int) scaledPosition;
        int nextIndex = Math.min(index + 1, gradientColors.length - 1);

        // Calculate interpolation factor between the two colors
        float factor = scaledPosition - index;

        // Interpolate between the two colors
        Color c1 = gradientColors[index];
        Color c2 = gradientColors[nextIndex];

        int red = (int) (c1.getRed() * (1 - factor) + c2.getRed() * factor);
        int green = (int) (c1.getGreen() * (1 - factor) + c2.getGreen() * factor);
        int blue = (int) (c1.getBlue() * (1 - factor) + c2.getBlue() * factor);

        return new Color(red, green, blue);
    }

    private ImageIcon createCircularAvatar(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw gradient background for avatar
        GradientPaint gp = new GradientPaint(
                0, 0, primaryColor,
                width, height, accentColor);
        g2d.setPaint(gp);
        g2d.fillOval(0, 0, width - 1, height - 1);

        // Draw a white border
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(2, 2, width - 5, height - 5);

        // Draw a simple user silhouette in white
        g2d.setColor(new Color(255, 255, 255, 180));

        // Head - positioned slightly higher for better appearance
        int headSize = width / 3;
        g2d.fillOval(width / 2 - headSize / 2, height / 5, headSize, headSize);

        // Body - as a rounded shape
        g2d.fillRoundRect(width / 2 - width / 4, height / 2, width / 2, height / 3, 15, 15);

        g2d.dispose();
        return new ImageIcon(img);
    }

    // Custom border class for rounded text fields
    class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        public RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    // Custom border class for shadow and rounded corners with 3D effect
    class ShadowRoundedBorder extends AbstractBorder {
        private final Color fillColor;
        private final int radius;
        private final int shadowSize;

        public ShadowRoundedBorder(Color fillColor, int radius, int shadowSize) {
            this.fillColor = fillColor;
            this.radius = radius;
            this.shadowSize = shadowSize;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Create multiple shadows for 3D effect with larger radius
            for (int i = shadowSize; i > 0; i--) {
                float alpha = 0.1f - (i * 0.01f);
                g2d.setColor(new Color(0, 0, 0, (int) (alpha * 255)));
                g2d.fill(new RoundRectangle2D.Double(
                        x + i, y + i,
                        width - i * 2, height - i * 2,
                        radius * 3, radius * 3)); // Increased radius multiplier from 2 to 3
            }

            // Create shape for panel background with 3D lighting effect
            RoundRectangle2D.Double shape = new RoundRectangle2D.Double(
                    x, y, width - shadowSize, height - shadowSize, radius * 3, radius * 3); // Increased radius
                                                                                            // multiplier

            // Create gradient for 3D lighting effect
            GradientPaint topLight = new GradientPaint(
                    x, y, new Color(255, 255, 255, 50),
                    x, y + height / 3, new Color(255, 255, 255, 0));

            // Fill panel background
            g2d.setColor(fillColor);
            g2d.fill(shape);

            // Add lighting effect
            g2d.setPaint(topLight);
            g2d.fill(shape);

            // Add subtle inner shadow at the bottom
            GradientPaint bottomShadow = new GradientPaint(
                    x, y + height - height / 4, new Color(0, 0, 0, 0),
                    x, y + height, new Color(0, 0, 0, 20));
            g2d.setPaint(bottomShadow);
            g2d.fill(shape);

            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius + shadowSize, radius + shadowSize);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Vui lòng điền đầy đủ thông tin đăng nhập!");
            return;
        }

        // Show animated "Đang đăng nhập..." message
        showLoading();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT * FROM users WHERE username = ? AND password = ?")) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String role = rs.getString("role");
                    String fullName = rs.getString("full_name");

                    // Update last login time
                    updateLastLogin(userId);

                    // Store session information
                    com.shopmanager.utils.SessionManager.startSession(userId, username, fullName, role);

                    // Stop animation timer
                    if (colorAnimationTimer != null) {
                        colorAnimationTimer.stop();
                    }

                    // Open main window
                    SwingUtilities.invokeLater(() -> {
                        MainWindow mainWindow = new MainWindow(userId, fullName, role);
                        mainWindow.setVisible(true);
                        dispose(); // Close login form
                    });
                } else {
                    showError("Tên đăng nhập hoặc mật khẩu không đúng!");
                    txtPassword.setText("");
                }
            }
        } catch (SQLException e) {
            showError("Lỗi kết nối: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        lblStatus.setText(message);
        lblStatus.setForeground(new Color(255, 89, 89));
        lblStatus.setIcon(null);
    }

    private void showLoading() {
        lblStatus.setText("Đang đăng nhập...");
        lblStatus.setForeground(new Color(100, 100, 100));

        // Could add a loading spinner icon here
    }

    private void updateLastLogin(int userId) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?")) {

            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating last login: " + e.getMessage());
        }
    }

    @Override
    public void dispose() {
        if (colorAnimationTimer != null) {
            colorAnimationTimer.stop();
        }
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}
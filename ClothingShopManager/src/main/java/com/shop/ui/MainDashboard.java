package main.java.com.shop.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainDashboard extends JFrame {
    private JPanel mainPanel;
    private ProductPanel productPanel;
    private InvoicePanel invoicePanel;
    private CustomerPanel customerPanel;
    private EmployeePanel employeePanel;
    private StatisticsPanel statisticsPanel;

    public MainDashboard() {
        setTitle("Hệ thống quản lý shop quần áo");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Thiết lập look and feel hiện đại
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Khởi tạo các panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(245, 245, 250));

        productPanel = new ProductPanel();
        invoicePanel = new InvoicePanel();
        customerPanel = new CustomerPanel();
        employeePanel = new EmployeePanel();
        statisticsPanel = new StatisticsPanel();

        // Thêm các panel vào mainPanel
        mainPanel.add(productPanel, "product");
        mainPanel.add(invoicePanel, "invoice");
        mainPanel.add(customerPanel, "customer");
        mainPanel.add(employeePanel, "employee");
        mainPanel.add(statisticsPanel, "statistics");

        // Tạo menu
        createMenuBar();

        // Thêm panel chính vào frame
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Hiển thị panel sản phẩm mặc định
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "product");

        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(51, 102, 153));

        // Create menus with black text
        JMenu menuSystem = createStyledMenu("Hệ thống", Color.BLACK);
        JMenu menuProduct = createStyledMenu("Sản phẩm", Color.BLACK);
        JMenu menuInvoice = createStyledMenu("Hóa đơn", Color.BLACK);
        JMenu menuCustomer = createStyledMenu("Khách hàng", Color.BLACK);
        JMenu menuStatistics = createStyledMenu("Thống kê", Color.BLACK);

        // Create menu items with black text
        JMenuItem miProduct = createStyledMenuItem("Quản lý sản phẩm");
        miProduct.setForeground(Color.BLACK);

        JMenuItem miInvoice = createStyledMenuItem("Quản lý hóa đơn");
        miInvoice.setForeground(Color.BLACK);

        JMenuItem miCustomer = createStyledMenuItem("Quản lý khách hàng");
        miCustomer.setForeground(Color.BLACK);

        JMenuItem miEmployee = createStyledMenuItem("Quản lý nhân viên");
        miEmployee.setForeground(Color.BLACK);

        JMenuItem miStats = createStyledMenuItem("Xem thống kê");
        miStats.setForeground(Color.BLACK);

        JMenuItem miExit = createStyledMenuItem("Thoát");
        miExit.setForeground(Color.BLACK);

        // Thêm action listener
        miProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "product");
            }
        });

        miInvoice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "invoice");
            }
        });

        miCustomer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "customer");
            }
        });

        miEmployee.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "employee");
            }
        });

        miStats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "statistics");
            }
        });
        miExit.addActionListener(e -> System.exit(0));

        // Thêm menu items vào menu
        menuProduct.add(miProduct);
        menuInvoice.add(miInvoice);
        menuCustomer.add(miCustomer);
        menuSystem.add(miEmployee);
        menuStatistics.add(miStats);
        menuSystem.addSeparator();
        menuSystem.add(miExit);

        menuBar.add(menuSystem);
        menuBar.add(menuProduct);
        menuBar.add(menuInvoice);
        menuBar.add(menuCustomer);
        menuBar.add(menuStatistics);

        setJMenuBar(menuBar);
    }

    private JMenu createStyledMenu(String text, Color textColor) {
        JMenu menu = new JMenu(text);
        menu.setForeground(textColor);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return menu;
    }

    private JMenuItem createStyledMenuItem(String text) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        menuItem.setBackground(Color.WHITE);
        return menuItem;
    }
}

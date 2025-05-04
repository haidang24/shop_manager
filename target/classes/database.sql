-- Create database
CREATE DATABASE IF NOT EXISTS clothing_shop;
USE clothing_shop;

-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create products table
CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    category_id INT,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    size VARCHAR(50),
    color VARCHAR(50),
    image_url VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

-- Create customers table
CREATE TABLE IF NOT EXISTS customers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    email VARCHAR(100),
    address TEXT,
    total_orders INT DEFAULT 0,
    total_spent DECIMAL(12,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('pending', 'processing', 'completed', 'cancelled') DEFAULT 'pending',
    payment_method VARCHAR(50),
    payment_status ENUM('unpaid', 'paid', 'refunded') DEFAULT 'unpaid',
    shipping_address TEXT,
    shipping_fee DECIMAL(10,2) DEFAULT 0,
    notes TEXT,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE SET NULL
);

-- Create order_items table
CREATE TABLE IF NOT EXISTS order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE SET NULL
);

-- Create suppliers table
CREATE TABLE IF NOT EXISTS suppliers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100),
    phone VARCHAR(15),
    email VARCHAR(100),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create inventory_transactions table
CREATE TABLE IF NOT EXISTS inventory_transactions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    supplier_id INT,
    transaction_type ENUM('import', 'export', 'adjustment') NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2),
    total_amount DECIMAL(10,2),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE SET NULL,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL
);

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    role ENUM('admin', 'staff') NOT NULL,
    last_login TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create triggers
DELIMITER //

CREATE TRIGGER after_order_insert
AFTER INSERT ON orders
FOR EACH ROW
BEGIN
    UPDATE customers 
    SET total_orders = total_orders + 1,
        total_spent = total_spent + NEW.total_amount
    WHERE id = NEW.customer_id;
END //

CREATE TRIGGER after_order_delete
AFTER DELETE ON orders
FOR EACH ROW
BEGIN
    UPDATE customers 
    SET total_orders = total_orders - 1,
        total_spent = total_spent - OLD.total_amount
    WHERE id = OLD.customer_id;
END //

CREATE TRIGGER after_order_update
AFTER UPDATE ON orders
FOR EACH ROW
BEGIN
    IF OLD.total_amount != NEW.total_amount THEN
        UPDATE customers 
        SET total_spent = total_spent - OLD.total_amount + NEW.total_amount
        WHERE id = NEW.customer_id;
    END IF;
END //

-- Create trigger to update product quantity after inventory transaction
CREATE TRIGGER after_inventory_transaction_insert
AFTER INSERT ON inventory_transactions
FOR EACH ROW
BEGIN
    IF NEW.transaction_type = 'import' THEN
        UPDATE products 
        SET quantity = quantity + NEW.quantity
        WHERE id = NEW.product_id;
    ELSEIF NEW.transaction_type = 'export' THEN
        UPDATE products 
        SET quantity = quantity - NEW.quantity
        WHERE id = NEW.product_id;
    ELSEIF NEW.transaction_type = 'adjustment' THEN
        UPDATE products 
        SET quantity = NEW.quantity
        WHERE id = NEW.product_id;
    END IF;
END //

-- Create trigger to update product quantity after order item is added
CREATE TRIGGER after_order_item_insert
AFTER INSERT ON order_items
FOR EACH ROW
BEGIN
    UPDATE products 
    SET quantity = quantity - NEW.quantity
    WHERE id = NEW.product_id;
END //

DELIMITER ;

-- Insert sample data
-- Insert categories
INSERT INTO categories (name, description) VALUES
('Áo', 'Tất cả các loại áo: sơ mi, thun, khoác...'),
('Quần', 'Tất cả các loại quần: Jean, Kaki, Tây...'),
('Váy', 'Các loại váy và đầm'),
('Phụ kiện', 'Các loại phụ kiện thời trang');

-- Insert products
INSERT INTO products (name, category_id, price, quantity, size, color, image_url, description) VALUES
('Áo sơ mi nam trắng', 1, 299000, 50, 'S,M,L,XL', 'Trắng', 'images/products/ao-so-mi-nam.jpg', 'Áo sơ mi nam dài tay màu trắng, chất liệu cotton cao cấp'),
('Áo thun nam basic', 1, 199000, 100, 'S,M,L,XL', 'Đen,Trắng,Xám', 'images/products/ao-thun-nam.jpg', 'Áo thun nam tay ngắn, chất liệu cotton thoáng mát'),
('Áo khoác denim nam', 1, 499000, 30, 'M,L,XL', 'Xanh nhạt', 'images/products/ao-khoac-denim.jpg', 'Áo khoác denim nam phong cách, bền đẹp'),
('Quần jean nam slim fit', 2, 450000, 45, '29,30,31,32,33,34', 'Xanh đậm', 'images/products/quan-jean-nam.jpg', 'Quần jean nam dáng slim fit, co giãn tốt'),
('Quần kaki nam', 2, 350000, 60, '29,30,31,32,33,34', 'Đen,Kem,Xanh navy', 'images/products/quan-kaki-nam.jpg', 'Quần kaki nam form regular, vải cao cấp'),
('Quần short nam', 2, 250000, 80, 'S,M,L,XL', 'Đen,Trắng,Xanh navy', 'images/products/quan-short-nam.jpg', 'Quần short nam thể thao, thoáng mát'),
('Váy liền công sở', 3, 550000, 25, 'S,M,L', 'Đen,Xanh navy', 'images/products/vay-lien-cong-so.jpg', 'Váy liền công sở thanh lịch, sang trọng'),
('Váy xòe hoa', 3, 450000, 30, 'S,M,L', 'Nhiều màu', 'images/products/vay-xoe-hoa.jpg', 'Váy xòe hoa nữ tính, nhẹ nhàng'),
('Đầm dạ hội', 3, 1200000, 15, 'S,M,L', 'Đỏ,Đen,Bạc', 'images/products/dam-da-hoi.jpg', 'Đầm dạ hội sang trọng, quyến rũ'),
('Thắt lưng da nam', 4, 250000, 40, 'Free size', 'Đen,Nâu', 'images/products/that-lung-da-nam.jpg', 'Thắt lưng da nam cao cấp, bền đẹp'),
('Ví da nam', 4, 350000, 35, 'Free size', 'Đen,Nâu', 'images/products/vi-da-nam.jpg', 'Ví da nam cao cấp, nhiều ngăn tiện dụng'),
('Túi xách nữ', 4, 550000, 20, 'Free size', 'Đen,Nâu,Hồng', 'images/products/tui-xach-nu.jpg', 'Túi xách nữ thời trang, sang trọng');

-- Insert customers
INSERT INTO customers (name, phone, email, address) VALUES
('Nguyễn Văn An', '0901234567', 'nguyenvanan@email.com', 'Quận 1, TP. Hồ Chí Minh'),
('Trần Thị Bình', '0902345678', 'tranthib@email.com', 'Quận 3, TP. Hồ Chí Minh'),
('Lê Văn Cường', '0903456789', 'levanc@email.com', 'Quận Cầu Giấy, Hà Nội'),
('Phạm Thị Dung', '0904567890', 'phamthid@email.com', 'Quận Hai Bà Trưng, Hà Nội'),
('Hoàng Văn Em', '0905678901', 'hoangvane@email.com', 'Quận Hải Châu, Đà Nẵng');

-- Insert suppliers
INSERT INTO suppliers (name, contact_person, phone, email, address) VALUES
('Công ty TNHH May mặc Việt Thành', 'Nguyễn Minh Tâm', '0987654321', 'contact@vietthanh.com', 'Quận 12, TP. Hồ Chí Minh'),
('Xưởng may Thành Công', 'Trần Văn Minh', '0976543210', 'contact@thanhcong.com', 'Quận Thanh Xuân, Hà Nội'),
('Công ty CP Thời trang An Phước', 'Lê Thu Trang', '0965432109', 'contact@anphuoc.com', 'Quận Bình Thạnh, TP. Hồ Chí Minh'),
('Nhập khẩu Phong Phú', 'Hoàng Văn Nam', '0954321098', 'contact@phongphu.com', 'Quận Tân Bình, TP. Hồ Chí Minh');

-- Insert users
INSERT INTO users (username, password, full_name, role) VALUES
('admin', 'admin123', 'Administrator', 'admin'),
('staff1', 'staff123', 'Nhân viên bán hàng', 'staff'),
('manager', 'manager123', 'Quản lý cửa hàng', 'admin'),
('staff2', 'staff456', 'Nhân viên kho', 'staff');

-- Insert inventory transactions
INSERT INTO inventory_transactions (product_id, supplier_id, transaction_type, quantity, unit_price, total_amount, notes) VALUES
(1, 1, 'import', 50, 200000, 10000000, 'Nhập hàng đợt 1'),
(2, 1, 'import', 100, 150000, 15000000, 'Nhập hàng đợt 1'),
(3, 2, 'import', 30, 350000, 10500000, 'Nhập hàng đợt 1'),
(4, 2, 'import', 45, 300000, 13500000, 'Nhập hàng đợt 1'),
(5, 3, 'import', 60, 250000, 15000000, 'Nhập hàng đợt 1'),
(6, 3, 'import', 80, 180000, 14400000, 'Nhập hàng đợt 1'),
(7, 1, 'import', 25, 400000, 10000000, 'Nhập hàng đợt 2'),
(8, 1, 'import', 30, 350000, 10500000, 'Nhập hàng đợt 2'),
(9, 2, 'import', 15, 900000, 13500000, 'Nhập hàng đợt 2'),
(10, 4, 'import', 40, 150000, 6000000, 'Nhập hàng đợt 2'),
(11, 4, 'import', 35, 250000, 8750000, 'Nhập hàng đợt 2'),
(12, 4, 'import', 20, 400000, 8000000, 'Nhập hàng đợt 2');

-- Insert orders and order items
-- Order 1
INSERT INTO orders (customer_id, total_amount, status, payment_method, payment_status, shipping_address, shipping_fee, notes)
VALUES (1, 748000, 'completed', 'Tiền mặt', 'paid', 'Quận 1, TP. Hồ Chí Minh', 30000, 'Giao hàng trong giờ hành chính');

INSERT INTO order_items (order_id, product_id, quantity, price, subtotal)
VALUES 
(1, 1, 2, 299000, 598000),
(1, 10, 1, 250000, 250000);

-- Order 2
INSERT INTO orders (customer_id, total_amount, status, payment_method, payment_status, shipping_address, shipping_fee, notes)
VALUES (2, 928000, 'completed', 'Chuyển khoản', 'paid', 'Quận 3, TP. Hồ Chí Minh', 30000, 'Giao hàng buổi tối sau 18h');

INSERT INTO order_items (order_id, product_id, quantity, price, subtotal)
VALUES 
(2, 2, 2, 199000, 398000),
(2, 5, 1, 350000, 350000),
(2, 10, 1, 250000, 250000);

-- Order 3
INSERT INTO orders (customer_id, total_amount, status, payment_method, payment_status, shipping_address, shipping_fee, notes)
VALUES (3, 1298000, 'processing', 'Chuyển khoản', 'paid', 'Quận Cầu Giấy, Hà Nội', 50000, 'Gói quà tặng');

INSERT INTO order_items (order_id, product_id, quantity, price, subtotal)
VALUES 
(3, 3, 1, 499000, 499000),
(3, 4, 1, 450000, 450000),
(3, 11, 1, 350000, 350000);

-- Order 4
INSERT INTO orders (customer_id, total_amount, status, payment_method, payment_status, shipping_address, shipping_fee, notes)
VALUES (4, 1280000, 'pending', 'Tiền mặt', 'unpaid', 'Quận Hai Bà Trưng, Hà Nội', 50000, 'Gọi trước khi giao');

INSERT INTO order_items (order_id, product_id, quantity, price, subtotal)
VALUES 
(4, 8, 2, 450000, 900000),
(4, 10, 1, 250000, 250000),
(4, 6, 1, 250000, 250000);

-- Order 5
INSERT INTO orders (customer_id, total_amount, status, payment_method, payment_status, shipping_address, shipping_fee, notes)
VALUES (5, 2430000, 'pending', 'Ví MoMo', 'unpaid', 'Quận Hải Châu, Đà Nẵng', 80000, 'Giao hàng nhanh');

INSERT INTO order_items (order_id, product_id, quantity, price, subtotal)
VALUES 
(5, 9, 1, 1200000, 1200000),
(5, 7, 1, 550000, 550000),
(5, 12, 1, 550000, 550000),
(5, 11, 1, 350000, 350000); 
-- Add image_path column to products table if it doesn't exist
ALTER TABLE products ADD COLUMN IF NOT EXISTS image_path VARCHAR(255); 
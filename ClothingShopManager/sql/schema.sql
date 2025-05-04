-- Tạo database
CREATE DATABASE IF NOT EXISTS clothing_shop;
USE clothing_shop;

-- Tạo bảng users
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Tạo bảng products
CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL
);

-- Tạo bảng customers
CREATE TABLE IF NOT EXISTS customers (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100)
);

-- Tạo bảng employees
CREATE TABLE IF NOT EXISTS employees (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Tạo bảng invoices
CREATE TABLE IF NOT EXISTS invoices (
    id VARCHAR(10) PRIMARY KEY,
    customer_id VARCHAR(10),
    date DATE NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Tạo bảng invoice_items
CREATE TABLE IF NOT EXISTS invoice_items (
    invoice_id VARCHAR(10),
    product_id VARCHAR(10),
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (invoice_id, product_id),
    FOREIGN KEY (invoice_id) REFERENCES invoices(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Thêm tài khoản admin mặc định
INSERT INTO users (username, password, role) 
VALUES ('admin', 'admin123', 'ADMIN');

-- Thêm một số dữ liệu mẫu
INSERT INTO products (id, name, type, price, quantity) VALUES
('P001', 'Áo thun nam basic', 'Áo thun', 199000, 50),
('P002', 'Quần jean nam slim fit', 'Quần jean', 499000, 30),
('P003', 'Áo sơ mi nữ trắng', 'Áo sơ mi', 299000, 40),
('P004', 'Váy liền công sở', 'Váy', 599000, 25),
('P005', 'Áo khoác denim', 'Áo khoác', 799000, 20);

INSERT INTO customers (id, name, phone, email) VALUES
('C001', 'Nguyễn Văn A', '0901234567', 'nguyenvana@email.com'),
('C002', 'Trần Thị B', '0912345678', 'tranthib@email.com'),
('C003', 'Lê Văn C', '0923456789', 'levanc@email.com');

INSERT INTO employees (id, name, role) VALUES
('E001', 'Phạm Thị D', 'Nhân viên bán hàng'),
('E002', 'Hoàng Văn E', 'Quản lý kho'),
('E003', 'Trương Thị F', 'Thu ngân');
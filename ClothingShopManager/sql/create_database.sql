-- Create database
CREATE DATABASE IF NOT EXISTS clothing_shop;
USE clothing_shop;

-- Create products table
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    size VARCHAR(10) NOT NULL,
    color VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample data
INSERT INTO products (name, category, price, quantity, size, color) VALUES
('Basic T-Shirt', 'T-Shirts', 19.99, 100, 'M', 'White'),
('Slim Fit Jeans', 'Pants', 49.99, 50, '32', 'Blue'),
('Summer Dress', 'Dresses', 39.99, 30, 'S', 'Floral'),
('Sports Jacket', 'Outerwear', 79.99, 25, 'L', 'Black'),
('Cotton Socks', 'Accessories', 9.99, 200, 'One Size', 'Grey'); 
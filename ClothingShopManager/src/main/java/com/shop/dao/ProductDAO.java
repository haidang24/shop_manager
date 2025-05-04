package com.shop.dao;

import com.shop.models.Product;
import java.util.List;

public interface ProductDAO {
    void add(Product product);

    void update(Product product);

    void delete(int id);

    Product getById(int id);

    List<Product> getAll();

    List<Product> searchByName(String name);

    List<Product> searchByCategory(String category);
}
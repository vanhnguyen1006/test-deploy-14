package com.example.testdeploy14.service;

import com.example.testdeploy14.production.Category;
import com.example.testdeploy14.production.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Iterable<Product> findAll();

    Optional<Product> findOne(long id);

    List<Product> search(String q);

    void save(Product product);

    void delete(Product product);

    List<Product> findProductByCategory(Category category);
}

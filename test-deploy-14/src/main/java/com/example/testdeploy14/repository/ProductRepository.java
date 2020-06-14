package com.example.testdeploy14.repository;

import com.example.testdeploy14.production.Category;
import com.example.testdeploy14.production.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    List<Product> findByNameContaining(String q);
    List<Product> findProductByCategory(Category category);
}

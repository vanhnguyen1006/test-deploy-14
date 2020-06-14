package com.example.testdeploy14.repository;

import com.example.testdeploy14.production.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAll();
    List<Category> findByNameContaining(String p);
}

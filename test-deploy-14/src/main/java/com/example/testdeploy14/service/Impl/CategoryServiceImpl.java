package com.example.testdeploy14.service.Impl;

import com.example.testdeploy14.production.Category;
import com.example.testdeploy14.repository.CategoryRepository;
import com.example.testdeploy14.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findOne(long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> search(String p) {
        return categoryRepository.findByNameContaining(p);
    }

    @Override
    public void save(Category cate) {
        categoryRepository.save(cate);
    }

    @Override
    public void delete(Category category) {
        categoryRepository.delete(category);
    }
}

package com.example.testdeploy14.controller.ProductController;

import com.example.testdeploy14.production.Category;
import com.example.testdeploy14.repository.ProductRepository;
import com.example.testdeploy14.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class CategoryController {
    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final ProductRepository productRepository;


    public CategoryController(CategoryService categoryService,
                              ProductRepository productRepository) {
        this.categoryService = categoryService;
        this.productRepository = productRepository;
    }

    // Category
    @GetMapping("admin/categories")
    public String showSignUpForm(Model model){
        model.addAttribute("categories", categoryService.findAll());
        return "category/index";
    }

    @GetMapping("admin/categories/add-category")
    public String addCat(Model model){
        model.addAttribute("categories", categoryService.findAll());
        return "category/add-category";
    }

    @PostMapping("admin/categories/add-category")
    public String addCategory(@Valid Category category,
                              BindingResult result,
                              Model model){
        if (result.hasErrors()){
            return "category/add-category";
        }
        categoryService.save(category);
        model.addAttribute("categories", categoryService.findAll());
        return "redirect:/admin/categories/";
    }

    @GetMapping("admin/categories/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Category category = categoryService.findOne(id).orElseThrow(()
                -> new IllegalArgumentException("Invalid category Id: " + id));
        model.addAttribute("category", category);
        return "category/update-category";
    }

    @PostMapping("admin/categories/update-category/{id}")
    public String updateCategory(@PathVariable("id") long id, @Valid Category category,
                                 BindingResult result, Model model){
        if (result.hasErrors()){
            category.setId(id);
            return "category/update-category";
        }
        categoryService.save(category);
        model.addAttribute("categories", categoryService.findAll());
        return "redirect:/admin/categories/";
    }

    @GetMapping("admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") long id, Model model) {
        Category category = categoryService.findOne(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid category Id: " + id));
        categoryService.delete(category);
        model.addAttribute("categories", categoryService.findAll());
        return "redirect:/admin/categories/";
    }
}

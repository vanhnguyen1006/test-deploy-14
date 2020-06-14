package com.example.testdeploy14.controller.ProductController;

import com.example.testdeploy14.production.Brand;
import com.example.testdeploy14.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class BrandController {
    @Autowired
    private final BrandRepository brandRepository;


    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @GetMapping("admin/brands")
    public String showBrand(Model model){
        model.addAttribute("brands", brandRepository.findAll());
        return "brand/index";
    }

    @GetMapping("admin/brands/add-brand")
    public String addBr(Model model){
        model.addAttribute("brands", brandRepository.findAll());
        return "brand/add-brand";
    }

    @PostMapping("admin/brands/add-brand")
    public String addBrand(@Valid Brand brand,
                           BindingResult result,
                           Model model){
        if (result.hasErrors()){
            return "brand/add-brand";
        }
        brandRepository.save(brand);
        model.addAttribute("brands", brandRepository.findAll());
        return "redirect:/admin/brands/";
    }

    @GetMapping("admin/brands/edit/{id}")
    public String editBrand(@PathVariable("id") long id, Model model){
        Brand brand = brandRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Valid Brand Id: " + id));
        model.addAttribute("brand", brand);
        return "brand/update-brand";
    }

    @PostMapping("admin/brands/update-brand/{id}")
    public String updateBrand(@PathVariable("id") long id, @Valid Brand brand,
                              BindingResult result, Model model){
        if (result.hasErrors()){
            return "brand/update-brand";
        }
        brandRepository.save(brand);
        model.addAttribute("brands", brandRepository.findAll());
        return "redirect:/admin/brands/";
    }

    @GetMapping("admin/brands/delete/{id}")
    public String deleteBrand(Model model, @PathVariable("id") long id){
        Brand brand = brandRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Valid Brand Id: " + id));
        brandRepository.delete(brand);
        model.addAttribute("brands", brandRepository.findAll());
        return "redirect:/admin/brands/";
    }
}

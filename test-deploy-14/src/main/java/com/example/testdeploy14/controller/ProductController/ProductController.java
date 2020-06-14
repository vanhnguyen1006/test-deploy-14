package com.example.testdeploy14.controller.ProductController;

import com.example.testdeploy14.production.Brand;
import com.example.testdeploy14.production.Category;
import com.example.testdeploy14.production.Product;
import com.example.testdeploy14.repository.BrandRepository;
import com.example.testdeploy14.repository.CategoryRepository;
import com.example.testdeploy14.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private final ProductService productService;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final BrandRepository brandRepository;



    public ProductController(ProductService productService,
                             CategoryRepository categoryRepository,
                             BrandRepository brandRepository){
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;


    }

    @GetMapping("/admin/products")
    public String home() {
        return "redirect:/admin/products/";
    }

    @GetMapping("/admin/products/add-product")
    public String addPro(Model model){
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("brands", brandRepository.findAll());
        return "product/add-product";
    }

    @PostMapping("/admin/products/add-product")
    public String setPro(@Valid Product product, BindingResult result,
                         @RequestParam("category_id") long categoryId,
                         @RequestParam("brand_id") long brandId,
                         Model model){
        if (result.hasErrors()){
            return "product/add-product";
        }
        Category category = (categoryRepository.findById(categoryId)).get();
        product.setCategory(category);
        Brand brand = (brandRepository.findById(brandId)).get();
        product.setBrand(brand);
        productService.save(product);
        model.addAttribute("product", product);
        return "image/uploadForm";
    }

    @GetMapping("/admin/products/edit/{id}")
    public String editPro(@PathVariable("id") long id,
                          Model model){
        Product product = productService.findOne(id).orElseThrow(()
                -> new IllegalArgumentException("Invalid product Id: " + id));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("product", product);
        return "product/update-product";
    }

    @PostMapping("/admin/products/update-product/{id}")
    public String updatePro(@PathVariable("id") long id, @Valid Product product,
                            BindingResult result, Model model,
                            @RequestParam("category_id") long categoryId,
                            @RequestParam("brand_id") long brandId){
        if (result.hasErrors()){
            product.setId(id);
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("brands", brandRepository.findAll());
            return "product/update-product";
        }
        product.setId(id);
        Category category = (categoryRepository.findById(categoryId)).get();
        product.setCategory(category);
        Brand brand = (brandRepository.findById(brandId)).get();
        product.setBrand(brand);
        productService.save(product);
        model.addAttribute("products", productService.findAll());
        return "image/uploadForm";
    }

    @GetMapping("/admin/products/delete/{id}")
    public String delPro(@PathVariable("id") long id, Model model){
        Product product = productService.findOne(id).orElseThrow(()
                -> new IllegalArgumentException("Invalid Product Id: " +id));

        productService.delete(product);
        model.addAttribute("products", productService.findAll());
        return "redirect:/admin/products/";
    }

    @GetMapping("/admin/products/")
    public String signUpProduct(Model model,
                                HttpServletRequest request,
                                RedirectAttributes redirect) {
        request.getSession().setAttribute("productList", null);
        Iterable<Product> products = productService.findAll();
        if(model.asMap().get("success") != null)
            redirect.addFlashAttribute("success",model.asMap().get("success").toString());
        return "redirect:/admin/products/page/1";
    }

    @GetMapping("/admin/products/page/{pageNumber}")
    public String pagination(Model model,
                             @PathVariable("pageNumber") int pageNumber,
                             HttpServletRequest request) {

        PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productList");
        int pagesize = 6;
        List<Product> list = (List<Product>) productService.findAll();
        System.out.println(list.size());
        if (pages == null) {
            pages = new PagedListHolder<>(list);
            pages.setPageSize(pagesize);
        }
        else {
            final int goToPage = pageNumber - 1;
            if (goToPage <= pages.getPageCount() && goToPage >= 0) {
                pages.setPage(goToPage);
            }
        }

        request.getSession().setAttribute("productList", pages);
        int current = pages.getPage() + 1;
        int begin = Math.max(1, current - list.size());
        int end = Math.min(begin + 5, pages.getPageCount());
        int totalPageCount = pages.getPageCount();
        String baseUrl = "/admin/products/page/";

        List<Brand> brands = brandRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("products", pages);
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        return "product/index";
    }

    @GetMapping("/admin/products/search/{pageNumber}")
    public String search(@RequestParam("search") String search,
                         Model model, @PathVariable("pageNumber") int pageNumber,
                         HttpServletRequest request){
        if (search.equals("")){
            return "redirect:/admin/products";
        }
        List<Product> list = productService.search(search);
        if (list == null){
            return "redirect:/admin/products";
        }
        PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productList");
        int pagesize = 6;
        pages = new PagedListHolder<>(list);
        pages.setPageSize(pagesize);

        final int goToPage = pageNumber - 1;
        if (goToPage <= pages.getPageCount() && goToPage >= 0) {
            pages.setPage(goToPage);
        }
        request.getSession().setAttribute("productList", pages);
        int current = pages.getPage() + 1;
        int begin = Math.max(1, current - list.size());
        int end = Math.min(begin + 5, pages.getPageCount());
        int totalPageCount = pages.getPageCount();
        String baseUrl = "/admin/products/page/";

        List<Brand> brands = brandRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("products", pages);
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);

        return "product/index";
    }
}

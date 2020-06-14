package com.example.testdeploy14.controller;

import com.example.testdeploy14.production.Category;
import com.example.testdeploy14.production.Product;
import com.example.testdeploy14.service.CategoryService;
import com.example.testdeploy14.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private final ProductService productService;
    @Autowired
    private final CategoryService categoryService;


    public HomeController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @RequestMapping("")
    public String showHomeView(){
        return "redirect:/";
    }

    @GetMapping("/")
    public String showHome(Model model,
                           HttpServletRequest request,
                           RedirectAttributes redirect){

        request.getSession().setAttribute("productList", null);
        if(model.asMap().get("success") != null)
            redirect.addFlashAttribute("success",model.asMap().get("success").toString());

        return "redirect:/page/1";

    }

    @GetMapping("/page/{pageNumber}")
    public String pagination(Model model,
                             @PathVariable("pageNumber") int pageNumber,
                             HttpServletRequest request) {
        PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productList");
        int pagesize = 6;
        List<Product> list = (List<Product>) productService.findAll();
        System.out.println(list.size());
        if (pages == null){
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
        String baseUrl = "/page/";

        List<Category> categories = categoryService.findAll();

        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("products", pages);
        model.addAttribute("categories", categories);
        return "home/index";
    }

    @GetMapping("/product/{id}")
    public String showProduct(Model model,
                              @PathVariable("id") long id){
        Product product = productService.findOne(id).get();
        model.addAttribute("product", product);
        return "home/product";
    }

    @GetMapping("/cart/{id}")
    public String cartPro(Model model,
                          @PathVariable("id") long id){
        Product product = productService.findOne(id).get();
        model.addAttribute("product", product);
        return "home/cart";

    }

    // Category - Thể loại - pagination
    @GetMapping("/category/{id}")
    public String pagePro(Model model, HttpServletRequest request,
                          RedirectAttributes redirect,
                          @PathVariable("id") long id){
        request.getSession().setAttribute("productList", null);
        Category category = categoryService.findOne(id).get();
        List<Product> products = productService.findProductByCategory(category);
        if (model.asMap().get("success") != null){
            redirect.addFlashAttribute("success", model.asMap().get("success").toString());
        }
        model.addAttribute("products", products);
        return "redirect:/category/{id}/page/1";
    }

    @GetMapping("/category/{id}/page/{pageNumber}")
    public String pagiProductByCate(Model model,
                                    @PathVariable("pageNumber") int pageNumber,
                                    HttpServletRequest request,
                                    @PathVariable("id") long id) {
        Category category = categoryService.findOne(id).get();
        PagedListHolder<?> pagesList = (PagedListHolder<?>) request.getSession().getAttribute("productList");
        int pagesize = 6;
        List<Product> list = (List<Product>) productService.findProductByCategory(category);
        System.out.println(list.size());
        if (pagesList == null){
            pagesList = new PagedListHolder<>(list);
            pagesList.setPageSize(pagesize);
        }
        else {
            final int goToPage = pageNumber - 1;
            if (goToPage <= pagesList.getPageCount() && goToPage >= 0) {
                pagesList.setPage(goToPage);
            }
        }

        request.getSession().setAttribute("productList", pagesList);
        int current = pagesList.getPage() + 1;
        int begin = Math.max(1, current - list.size());
        int end = Math.min(begin + 5, pagesList.getPageCount());
        int totalPageCount = pagesList.getPageCount();
        String baseUrl = "/category/" + category.getId() + "/page/";

        List<Category> categories = categoryService.findAll();

        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("products", pagesList);
        model.addAttribute("categories", categories);
        if(list.size() < 1){
            model.addAttribute("empty",true);
        }
        return "home/category";
    }

    @GetMapping("/search/page/{pageNumber}")
    public String searchPro(@RequestParam("search") String search,
                            Model model, @PathVariable("pageNumber") int pageNumber,
                            HttpServletRequest request){
        if (search.equals("")){
            return "redirect:/";
        }
        List<Product> list = productService.search(search);
        if (list == null){
            return "redirect:/";
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
        String baseUrl = "/page/";

        List<Category> categories = categoryService.findAll();

        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("products", pages);
        model.addAttribute("categories", categories);
        return "home/index";
    }
}

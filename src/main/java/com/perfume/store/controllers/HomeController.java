package com.perfume.store.controllers;

import com.perfume.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        // Show only first 3 products as featured
        List products = productService.getAllProducts();
        int limit = Math.min(3, products.size());
        model.addAttribute("featuredProducts", products.subList(0, limit));
        return "pages/home";
    }
}
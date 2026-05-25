package com.perfume.store.controllers;

import com.perfume.store.models.Product;
import com.perfume.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        List<Product> products = productService.getAllProducts();
        List<Product> womensPerfumes = productService.getWomensPerfumes();
        List<Product> giftSets = productService.getGiftSets();

        List<Product> featured = !womensPerfumes.isEmpty() ? womensPerfumes : products;
        int featuredLimit = Math.min(6, featured.size());
        model.addAttribute("featuredProducts",
                featuredLimit > 0 ? featured.subList(0, featuredLimit) : List.of());
        model.addAttribute("womensPerfumes", womensPerfumes);
        model.addAttribute("giftSets", giftSets);
        model.addAttribute("bestOverall", productService.findByName("Chanel Coco Mademoiselle"));

        List<String> heroImages = products.stream()
                .map(Product::getImageUrl)
                .filter(url -> url != null && !url.isBlank())
                .limit(6)
                .collect(Collectors.toList());
        model.addAttribute("heroImages", heroImages);

        return "pages/home";
    }
}
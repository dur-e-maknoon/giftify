package com.perfume.store.controllers;

import com.perfume.store.models.Product;
import com.perfume.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Value("${upload.dir}")
    private String uploadDir;

    @GetMapping
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("title", "All Products");
        return "pages/products";
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "pages/product-detail";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/add-product";
    }

    @PostMapping("/add")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                product.setImageUrl("/images/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "admin/edit-product";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute Product product,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        product.setId(id);
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                product.setImageUrl("/images/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Product existing = productService.getProductById(id);
            product.setImageUrl(existing.getImageUrl());
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @GetMapping("/category/{gender}")
    public String getByGender(@PathVariable String gender, Model model) {
        model.addAttribute("products", productService.getProductsByGender(gender));
        String label = gender.charAt(0) + gender.substring(1).toLowerCase();
        model.addAttribute("title", "WOMEN".equalsIgnoreCase(gender)
                ? "Women's Perfumes"
                : label + " Collection");
        return "pages/products";
    }

    @GetMapping("/mood/{mood}")
    public String getByMood(@PathVariable String mood, Model model) {
        model.addAttribute("products", productService.getProductsByMood(mood));
        model.addAttribute("title", mood.charAt(0) + mood.substring(1).toLowerCase() + " Collection");
        return "pages/products";
    }

    @GetMapping("/type/{mainCategory}")
    public String getByMainCategory(@PathVariable String mainCategory, Model model) {
        model.addAttribute("products", productService.getProductsByMainCategory(mainCategory));
        String title = switch (mainCategory.toUpperCase()) {
            case "GIFTSET" -> "Luxury Gift Sets";
            case "PERFUME" -> "Perfumes";
            default -> mainCategory.charAt(0) + mainCategory.substring(1).toLowerCase() + " Collection";
        };
        model.addAttribute("title", title);
        return "pages/products";
    }
    @GetMapping("/search")
    public String searchProducts(@RequestParam String keyword, Model model) {
        model.addAttribute("products", productService.searchProducts(keyword));
        model.addAttribute("title", "Search results for: " + keyword);
        model.addAttribute("keyword", keyword);
        return "pages/products";
    }
}
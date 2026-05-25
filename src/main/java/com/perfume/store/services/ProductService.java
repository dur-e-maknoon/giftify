package com.perfume.store.services;

import com.perfume.store.models.Product;
import com.perfume.store.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getProductsByGender(String gender) {
        return productRepository.findByGender(gender);
    }

    public List<Product> getProductsByMood(String mood) {
        return productRepository.findByMood(mood);
    }

    public List<Product> getProductsByMainCategory(String mainCategory) {
        return productRepository.findByMainCategory(mainCategory);
    }

    private static final List<String> WOMENS_PERFUME_RANK = List.of(
            "Chanel Coco Mademoiselle",
            "Yves Saint Laurent Libre",
            "Dior Miss Dior",
            "Lancôme La Vie Est Belle",
            "Maison Francis Kurkdjian Baccarat Rouge 540",
            "YSL Black Opium",
            "Tom Ford Black Orchid",
            "Dior J'adore",
            "Jo Malone English Pear & Freesia",
            "Gucci Bloom"
    );

    public List<Product> getWomensPerfumes() {
        List<Product> perfumes = productRepository.findByGenderAndMainCategory("WOMEN", "PERFUME");
        perfumes.sort(Comparator.comparingInt(p -> {
            int index = WOMENS_PERFUME_RANK.indexOf(p.getName());
            return index >= 0 ? index : Integer.MAX_VALUE;
        }));
        return perfumes;
    }

    public List<Product> getGiftSets() {
        return productRepository.findByMainCategory("GIFTSET");
    }

    public Product findByName(String name) {
        return productRepository.findByName(name).orElse(null);
    }
}
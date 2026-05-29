package com.perfume.store.services;

import com.perfume.store.models.Product;
import com.perfume.store.models.User;
import com.perfume.store.models.Wishlist;
import com.perfume.store.repositories.ProductRepository;
import com.perfume.store.repositories.UserRepository;
import com.perfume.store.repositories.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Wishlist getWishlistForUser(String email) {
        User user = userRepository.findByEmail(email);
        Wishlist wishlist = wishlistRepository.findByUser(user);
        if (wishlist == null) {
            wishlist = new Wishlist(user);
            wishlistRepository.save(wishlist);
        }
        return wishlist;
    }

    public void addToWishlist(String email, Long productId) {
        Wishlist wishlist = getWishlistForUser(email);
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return;
        if (!wishlist.getProducts().contains(product)) {
            wishlist.getProducts().add(product);
            wishlistRepository.save(wishlist);
        }
    }

    public void removeFromWishlist(String email, Long productId) {
        Wishlist wishlist = getWishlistForUser(email);
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return;
        wishlist.getProducts().remove(product);
        wishlistRepository.save(wishlist);
    }

    public boolean isInWishlist(String email, Long productId) {
        Wishlist wishlist = getWishlistForUser(email);
        return wishlist.getProducts().stream()
                .anyMatch(p -> p.getId().equals(productId));
    }
}
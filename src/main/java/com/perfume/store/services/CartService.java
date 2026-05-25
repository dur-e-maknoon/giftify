package com.perfume.store.services;

import com.perfume.store.models.*;
import com.perfume.store.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Get or create cart for user
    public Cart getCartForUser(String email) {
        User user = userRepository.findByEmail(email);
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart(user);
            cartRepository.save(cart);
        }
        return cart;
    }

    // Add product to cart
    public void addToCart(String email, Long productId) {
        Cart cart = getCartForUser(email);
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return;

        // Check if already in cart
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + 1);
                cartItemRepository.save(item);
                return;
            }
        }

        // Add new item
        CartItem item = new CartItem(cart, product, 1);
        cartItemRepository.save(item);
    }

    // Remove item from cart
    public void removeFromCart(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    // Get total price
    public BigDecimal getCartTotal(String email) {
        Cart cart = getCartForUser(email);
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            total = total.add(item.getTotalPrice());
        }
        return total;
    }

    // Clear cart
    public void clearCart(String email) {
        Cart cart = getCartForUser(email);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
    public void increaseQuantity(Long itemId) {
        CartItem item = cartItemRepository.findById(itemId).orElse(null);
        if (item != null) {
            item.setQuantity(item.getQuantity() + 1);
            cartItemRepository.save(item);
        }
    }

    public void decreaseQuantity(Long itemId) {
        CartItem item = cartItemRepository.findById(itemId).orElse(null);
        if (item != null) {
            if (item.getQuantity() <= 1) {
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(item.getQuantity() - 1);
                cartItemRepository.save(item);
            }
        }
    }
}
package com.perfume.store.controllers;

import com.perfume.store.models.Cart;
import com.perfume.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // View cart
    @GetMapping
    public String viewCart(Authentication auth, Model model) {
        String email = auth.getName();
        Cart cart = cartService.getCartForUser(email);
        model.addAttribute("cart", cart);
        model.addAttribute("total", cartService.getCartTotal(email));
        return "pages/cart";
    }

    // Add to cart
    @GetMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, Authentication auth) {
        cartService.addToCart(auth.getName(), productId);
        return "redirect:/cart";
    }

    // Remove from cart
    @GetMapping("/remove/{itemId}")
    public String removeFromCart(@PathVariable Long itemId) {
        cartService.removeFromCart(itemId);
        return "redirect:/cart";
    }
    // Increase quantity
    @GetMapping("/increase/{itemId}")
    public String increaseQuantity(@PathVariable Long itemId) {
        cartService.increaseQuantity(itemId);
        return "redirect:/cart";
    }

    // Decrease quantity
    @GetMapping("/decrease/{itemId}")
    public String decreaseQuantity(@PathVariable Long itemId) {
        cartService.decreaseQuantity(itemId);
        return "redirect:/cart";
    }
}
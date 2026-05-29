package com.perfume.store.controllers;

import com.perfume.store.models.Wishlist;
import com.perfume.store.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping
    public String viewWishlist(Authentication auth, Model model) {
        Wishlist wishlist = wishlistService.getWishlistForUser(auth.getName());
        model.addAttribute("wishlist", wishlist);
        return "pages/wishlist";
    }

    @GetMapping("/add/{productId}")
    public String addToWishlist(@PathVariable Long productId,
                                Authentication auth) {
        wishlistService.addToWishlist(auth.getName(), productId);
        return "redirect:/products/" + productId;
    }

    @GetMapping("/remove/{productId}")
    public String removeFromWishlist(@PathVariable Long productId,
                                     Authentication auth) {
        wishlistService.removeFromWishlist(auth.getName(), productId);
        return "redirect:/wishlist";
    }
}
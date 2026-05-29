package com.perfume.store.controllers;

import com.perfume.store.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add/{productId}")
    public String addReview(@PathVariable Long productId,
                            @RequestParam Integer rating,
                            @RequestParam String comment,
                            Authentication auth) {
        if (auth != null) {
            reviewService.addReview(productId, auth.getName(), rating, comment);
        }
        return "redirect:/products/" + productId;
    }

    @GetMapping("/delete/{reviewId}/{productId}")
    public String deleteReview(@PathVariable Long reviewId,
                               @PathVariable Long productId) {
        reviewService.deleteReview(reviewId);
        return "redirect:/products/" + productId;
    }
}
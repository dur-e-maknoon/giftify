package com.perfume.store.services;

import com.perfume.store.models.Product;
import com.perfume.store.models.Review;
import com.perfume.store.models.User;
import com.perfume.store.repositories.ProductRepository;
import com.perfume.store.repositories.ReviewRepository;
import com.perfume.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Review> getReviewsForProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public Double getAverageRating(Long productId) {
        Double avg = reviewRepository.getAverageRating(productId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    public boolean hasUserReviewed(Long productId, Long userId) {
        return reviewRepository.existsByProductIdAndUserId(productId, userId);
    }

    public void addReview(Long productId, String email, Integer rating, String comment) {
        User user = userRepository.findByEmail(email);
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null || user == null) return;
        if (hasUserReviewed(productId, user.getId())) return;
        Review review = new Review(product, user, rating, comment);
        reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
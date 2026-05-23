package com.perfume.store.repositories;

import com.perfume.store.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByGender(String gender);
    List<Product> findByMood(String mood);
    List<Product> findByMainCategory(String mainCategory);

}
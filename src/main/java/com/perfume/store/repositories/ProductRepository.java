package com.perfume.store.repositories;

import com.perfume.store.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByGender(String gender);
    List<Product> findByMood(String mood);
    List<Product> findByMainCategory(String mainCategory);
    List<Product> findByGenderAndMainCategory(String gender, String mainCategory);

    boolean existsByName(String name);
    Optional<Product> findByName(String name);

}
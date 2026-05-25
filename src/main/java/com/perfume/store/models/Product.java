package com.perfume.store.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String brand;

    @Column
    private BigDecimal price;

    @Column(length = 2000)
    private String description;

    private String category;

    private String gender;

    private String mood;

    private String mainCategory;
    private String notes;         // "Bergamot, Sichuan pepper, Ambroxan"
    private String bestFor;       // "Everyday wear, office, dates"
    private String longevity;     // "8-10 hours"
    private String season;        // "All seasons"
    private String vibe;          // "Clean, confident, modern"

    @Column(name = "image_url")
    private String imageUrl;

    private Integer stockQuantity;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // --- Constructors ---
    public Product() {}

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getBestFor() { return bestFor; }
    public void setBestFor(String bestFor) { this.bestFor = bestFor; }

    public String getLongevity() { return longevity; }
    public void setLongevity(String longevity) { this.longevity = longevity; }

    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }

    public String getVibe() { return vibe; }
    public void setVibe(String vibe) { this.vibe = vibe; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public String getMainCategory() { return mainCategory; }
    public void setMainCategory(String mainCategory) { this.mainCategory = mainCategory; }


    public String getImageUrl() {
        return imageUrl;
    }public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
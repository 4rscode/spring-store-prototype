package com.example.webtutors.repositories;

import com.example.webtutors.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteAllByProductId(Long productId);
}

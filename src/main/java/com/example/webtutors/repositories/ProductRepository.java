package com.example.webtutors.repositories;

import com.example.webtutors.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitle(String title);
    Optional<Product> findById(Long id);
    List<Product> findByCity(String city);

}

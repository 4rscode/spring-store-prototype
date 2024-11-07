package com.example.webtutors.services;

import com.example.webtutors.models.Image;
import com.example.webtutors.models.Product;
import com.example.webtutors.models.User;
import com.example.webtutors.repositories.ProductRepository;
import com.example.webtutors.repositories.ImageRepository;
import com.example.webtutors.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public List<Product> listProducts(String title) {
        if (title != null) {
            return productRepository.findByTitle(title);
        }
        return productRepository.findAll();
    }

    public List<Product> listAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public void saveProduct(Principal principal, Product product, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        product.setUser(getUserByPrincipal(principal));

        if (file1 != null && !file1.isEmpty()) {
            Image image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }
        if (file2 != null && !file2.isEmpty()) {
            Image image2 = toImageEntity(file2);
            product.addImageToProduct(image2);
        }
        if (file3 != null && !file3.isEmpty()) {
            Image image3 = toImageEntity(file3);
            product.addImageToProduct(image3);
        }

        log.info("Saving new Product. Title: {}; Author email: {}", product.getTitle(), product.getUser().getEmail());

        Product savedProduct = productRepository.save(product);

        if (!savedProduct.getImages().isEmpty()) {
            savedProduct.setPreviewImageId(savedProduct.getImages().get(0).getId());
            productRepository.save(savedProduct);
        }
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) {
            return null;
        }
        return userRepository.findByEmail(principal.getName());
    }

    @Transactional
    public void deleteProduct(User user, Long productId) {
        log.info("Attempting to delete product with id = {} by user {}", productId, user.getEmail());
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            if (product.get().getUser().equals(user)) {
                log.info("User {} is the owner of the product with id = {}. Deleting the product.", user.getEmail(), productId);
                productRepository.delete(product.get());
                productRepository.flush(); // Добавить принудительную запись изменений в базу данных
            } else {
                log.warn("User {} is not the owner of the product with id = {}. Cannot delete the product.", user.getEmail(), productId);
            }
        } else {
            log.warn("Product with id = {} not found", productId);
        }
    }


    @Cacheable(value = "products", key = "#productId")
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Product> listProductsByCity(String city) {
        return productRepository.findByCity(city);
    }

    @Transactional
    public Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }
}

